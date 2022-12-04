package cn.cherry.core.engine.base;

import cn.cherry.core.engine.base.struct.PointerLinked;
import cn.cherry.core.engine.base.struct.PointerLinkedRing;
import cn.cherry.core.infra.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static cn.cherry.core.engine.utils.BaseUtils.*;

/**
 * 时间轮{@link TimingWheel}的默认具体实现。{@link DefaultTimingWheel}结合{@link SpinLocker}自旋锁，
 * 提供了{@link TimeSlot}槽位获取方法{@link #getSlot(int distance)}和转动方法{@link #turn()}实现
 *
 * @author realDragonKing
 */
public class DefaultTimingWheel extends TimingWheel {

    private int position;
    private final PointerLinked<TimeSlot> ring;
    private final Map<Integer, TimeSlot> slotMap;

    public DefaultTimingWheel(long interval, int totalTicks, long waitTimeout, int threadNumber, int taskListSize) {
        super(interval, totalTicks, waitTimeout);
        this.position = 0;
        this.slotMap = new HashMap<>(getTotalTicks());

        List<TimeSlot> list = new ArrayList<>(getTotalTicks());
        int coreSize = Runtime.getRuntime().availableProcessors() << 1;
        checkPositive(threadNumber, "threadNumber");
        Executor executor = new ThreadPoolExecutor(
                Math.min(coreSize, threadNumber),
                Math.max(coreSize, threadNumber),
                2L,
                TimeUnit.MINUTES,
                new LinkedBlockingQueue<>(taskListSize),
                (r, executor1) -> {});

        for (int i = 0; i < getTotalTicks(); i++) {
            SpinLocker locker = new SpinLocker(getWaitTimeout());
            TimeSlot slot = new TimeSlot(locker, executor);
            this.slotMap.put(i, slot);
            list.add(slot);
        }
        this.ring = new PointerLinkedRing<>(list);
    }

    /**
     * 提交一个任务
     *
     * @param task 任务
     * @return 任务的id（提交失败返回-1）
     */
    @Override
    public long submit(Task task) {
        return 0;
    }

    /**
     * 删除一个任务
     *
     * @param taskId 任务的id
     * @return 任务是否删除成功
     */
    @Override
    public boolean remove(long taskId) {
        return false;
    }

    /**
     * 时间轮进行一次转动
     */
    @Override
    public void turn() {
        this.ring.moveNext();
        this.position++;
        if (this.position == getTotalTicks()) {
            this.position = 0;
        }
        TimeSlot slot = this.ring.getValue();
        slot.decAndExecute();
    }

    /**
     * 根据相对时间距离和interval计算得到，取到对应的槽位{@link TimeSlot}
     *
     * @param distance 相对时间距离
     * @return 时间轮槽位
     */
    private TimeSlot getSlot(int distance) {
        int rawIndex = distance + this.position;
        int index = rawIndex >= getTotalTicks() ? rawIndex % getTotalTicks() : rawIndex;
        return this.slotMap.get(index);
    }

}
