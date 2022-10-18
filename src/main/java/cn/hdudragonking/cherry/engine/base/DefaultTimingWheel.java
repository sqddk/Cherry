package cn.hdudragonking.cherry.engine.base;

import cn.hdudragonking.cherry.engine.base.struct.DefaultPointerLinkedList;
import cn.hdudragonking.cherry.engine.base.struct.DefaultPointerLinkedRing;
import cn.hdudragonking.cherry.engine.base.struct.PointerLinkedList;
import cn.hdudragonking.cherry.engine.task.Task;
import cn.hdudragonking.cherry.engine.utils.BaseUtils;
import cn.hdudragonking.cherry.engine.utils.TimeUtils;

import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * 时间轮的默认实现类，进行任务调度
 *
 * @since 2022/10/17
 * @author realDragonKing
 */
public class DefaultTimingWheel implements TimingWheel {

    private TimePoint currentTimePoint;
    private final int totalTicks;
    private final int interval;
    private final ExecutorService executor;
    private final static int DEFAULT_TOTAL_TICKS = 10;
    private final PointerLinkedList<Map<Integer, PointerLinkedList<Task>>> timingWheel;

    public DefaultTimingWheel(int interval) {
        this(interval, DEFAULT_TOTAL_TICKS);
    }

    public DefaultTimingWheel(int interval, int totalTicks) {
        this.totalTicks = totalTicks;
        this.interval = interval;
        int coreSize = Runtime.getRuntime().availableProcessors();
        this.executor = BaseUtils.createWorkerThreadPool(2, coreSize * 2, 1000);
        this.timingWheel = new DefaultPointerLinkedRing(this.totalTicks);
        this.currentTimePoint = TimePoint.getCurrentTimePoint();
    }

    /**
     * 提交一个新的定时任务
     *
     * @param task 定时任务
     */
    @Override
    public void submit(Task task) {
        int difference = TimeUtils.calDifference(this.currentTimePoint, task.getTimePoint(), this.interval);
        if (difference <= 0) {
            return;
        }
        int round = difference / this.totalTicks;
        int ticks = difference % this.totalTicks;
        Map<Integer, PointerLinkedList<Task>> map;
        synchronized (this.timingWheel) {
            for (int i = 0; i < ticks; i++) {
                this.timingWheel.moveNext();
            }
            map = this.timingWheel.getPointer();
            for (int i = 0; i < ticks; i++) {
                this.timingWheel.movePrevious();
            }
        }
        PointerLinkedList<Task> taskList = map.get(round);
        if (taskList == null) {
            taskList = new DefaultPointerLinkedList<>();
        }
        taskList.add(task);
        map.put(round, taskList);
    }

    /**
     * 时间轮进行一次转动
     */
    @Override
    public void turn() {
        synchronized (this.timingWheel) {
            this.timingWheel.moveNext();
            this.currentTimePoint = TimePoint.getCurrentTimePoint();
            Map<Integer, PointerLinkedList<Task>> map = this.timingWheel.getPointer();
            for (Map.Entry<Integer, PointerLinkedList<Task>> entry : map.entrySet()) {
                int round = entry.getKey();
                PointerLinkedList<Task> list = entry.getValue();
                map.remove(round);
                if (round == 0) {
                    for (int i = 0; i < list.size(); i++) {
                        Task task = list.getPointer();
                        this.executor.submit(task::execute);
                        list.moveNext();
                    }
                } else {
                    map.put(round - 1, list);
                }
            }
        }
    }

}
