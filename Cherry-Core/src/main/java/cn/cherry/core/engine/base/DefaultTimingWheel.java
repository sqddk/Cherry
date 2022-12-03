package cn.cherry.core.engine.base;

import cn.cherry.core.engine.base.struct.PointerLinked;
import cn.cherry.core.engine.base.struct.PointerLinkedRing;
import cn.cherry.core.infra.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public DefaultTimingWheel(long interval, int totalTicks, long waitTimeout) {
        super(interval, totalTicks, waitTimeout);
        List<TimeSlot> list = new ArrayList<>(getTotalTicks());
        this.slotMap = new HashMap<>(getTotalTicks());
        for (int i = 0; i < getTotalTicks(); i++) {
            SpinLocker locker = new SpinLocker(getWaitTimeout());
            TimeSlot slot = new TimeSlot(locker);
            this.slotMap.put(i, slot);
            list.add(slot);
        }
        this.position = 0;
        this.ring = new PointerLinkedRing<>(list);
    }

    /**
     * 提交一个任务
     *
     * @param task 任务
     * @return 任务的id
     */
    @Override
    public long submit(Task task) {
        return 0;
    }

    /**
     * 删除一个任务
     *
     * @param taskId 任务的id
     * @return 任务是否删除成功（成功返回 1， 失败返回 0）
     */
    @Override
    public int remove(long taskId) {
        return 0;
    }

    /**
     * 时间轮进行一次转动
     */
    @Override
    public void turn() {
        this.ring.moveNext();
        this.position++;
        if (this.position == getTotalTicks()) this.position = 0;
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
