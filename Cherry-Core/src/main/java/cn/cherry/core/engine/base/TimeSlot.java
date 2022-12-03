package cn.cherry.core.engine.base;

import java.util.HashMap;
import java.util.Map;

/**
 * 时间轮的槽位，被时间轮的默认实现{@link DefaultTimingWheel}所使用。
 * {@link TimeSlot}结合{@link SpinLocker}自旋锁，提供了在单个槽位上的线程安全的读写{@link TaskList}的方法
 *
 * @author realDragonKing
 */
public final class TimeSlot {

    private final Map<Integer, TaskList> map;
    private final SpinLocker locker;

    public TimeSlot(SpinLocker locker) {
        this.map = new HashMap<>();
        this.locker = locker;
    }

}
