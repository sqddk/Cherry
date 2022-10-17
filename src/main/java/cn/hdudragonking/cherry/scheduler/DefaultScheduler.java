package cn.hdudragonking.cherry.scheduler;

import cn.hdudragonking.cherry.base.DefaultPointerLinkedList;
import cn.hdudragonking.cherry.base.PointerLinkedList;
import cn.hdudragonking.cherry.base.DefaultPointerLinkedRing;
import cn.hdudragonking.cherry.base.TimePoint;
import cn.hdudragonking.cherry.task.Task;
import cn.hdudragonking.cherry.utils.TimeUtils;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 调度者的默认实现类，使用时间轮算法进行任务调度
 * @author realDragonKing
 */
public class DefaultScheduler implements Scheduler {

    private TimePoint currentTimePoint;
    private final int totalTicks;
    private final ExecutorService executor;
    private final static int DEFAULT_TOTAL_TICKS = 60;
    private final PointerLinkedList<Map<Integer, PointerLinkedList<Task>>> timingWheel;

    public DefaultScheduler () {
        this(DEFAULT_TOTAL_TICKS);
    }

    public DefaultScheduler (int totalTicks) {
        this.totalTicks = totalTicks;
        int coreSize = Runtime.getRuntime().availableProcessors();
        this.executor = Executors.newFixedThreadPool(coreSize * 2);
        this.timingWheel = new DefaultPointerLinkedRing(totalTicks);
        this.updateCurrentTimePoint();
    }

    /**
     * 提交一个新的定时任务
     *
     * @param task 定时任务
     */
    @Override
    public synchronized void submit(Task task) {
        int difference = TimeUtils.calDifference(this.currentTimePoint, task.getTimePoint());
        int round = difference / this.totalTicks;
        int ticks = difference % this.totalTicks;
        for (int i = 0; i < round; i++) {
            this.timingWheel.moveNext();
        }
        Map<Integer, PointerLinkedList<Task>> map = this.timingWheel.getPointer();
        for (int i = 0; i < round; i++) {
            this.timingWheel.movePrevious();
        }
        PointerLinkedList<Task> taskList = map.getOrDefault(ticks, new DefaultPointerLinkedList<>());
        taskList.add(task);
    }

    /**
     * 时间轮进行一次转动
     */
    @Override
    public synchronized void turn() {
        this.timingWheel.moveNext();
        Map<Integer, PointerLinkedList<Task>> map = this.timingWheel.getPointer();
        for (Map.Entry<Integer, PointerLinkedList<Task>> entry : map.entrySet()) {
            int round = entry.getKey();
            PointerLinkedList<Task> list = entry.getValue();
            map.remove(round);
            round--;
            if (round == 0) {
                for (int i = 0; i < list.size(); i++) {
                    Task task = list.getPointer();
                    this.executor.submit(task::execute);
                    list.moveNext();
                }
                list.resetHead();
            } else {
                map.put(round, list);
            }
        }
    }

    /**
     * 对当前时间点进行更新
     */
    private void updateCurrentTimePoint() {
        Calendar calendar = new GregorianCalendar();
        String currentTime = String.format("%04d%02d%02d%02d%02d",
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR),
                calendar.get(Calendar.MINUTE));
        this.currentTimePoint = TimePoint.parse(currentTime);
    }

}
