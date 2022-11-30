package cn.cherry.core.engine.base;

import cn.cherry.core.infra.Task;
import java.util.concurrent.atomic.AtomicBoolean;

import static cn.cherry.core.engine.utils.BaseUtils.*;

/**
 * 时间轮{@link TimingWheel}的抽象实现，提供了一系列操作接口。并且为了保证线程安全，通过CAS操作类{@link AtomicBoolean}提供了一种自旋锁实现
 *
 * @author realDragonKing
 */
public abstract class AbstractTimingWheel implements TimingWheel {

    private final long interval; // 单位为毫秒（ms）
    private final int totalTicks;
    private final long waitTimeout; // 单位为纳秒（ns）
    private final AtomicBoolean monitor;

    public AbstractTimingWheel(long interval, int totalTicks, long waitTimeout) {
        this.interval = checkPositive(interval, "interval");
        this.totalTicks = checkPositive(totalTicks, "totalTicks");
        this.waitTimeout = checkPositive(waitTimeout, "waitTimeout");
        this.monitor = new AtomicBoolean(false);
    }

    /**
     * @return 单次转动的时间间隔
     */
    public final long getInterval() {
        return this.interval;
    }

    /**
     * @return 一轮转动的刻度总数
     */
    public final int getTotalTicks() {
        return this.totalTicks;
    }

    /**
     * @return 自旋锁超时时间
     */
    public final long getWaitTimeout() {
        return this.waitTimeout;
    }

    /**
     * 提交一个任务
     *
     * @param task 任务
     * @return 任务的id
     */
    public abstract long submit(Task task);

    /**
     * 删除一个任务
     *
     * @param taskId 任务的id
     * @return 任务是否删除成功（成功返回 1， 失败返回 0）
     */
    public abstract int remove(long taskId);

    /**
     * 尝试获取到时间轮的操作锁，若没获得则进行自旋，自旋超过时间{@link #waitTimeout}则返回锁获取失败的信息
     *
     * @return 是否获取锁成功
     */
    public final boolean lock() {
        long endWaitingTime = System.nanoTime() + this.getWaitTimeout();
        AtomicBoolean monitor = this.monitor;
        while (!monitor.compareAndSet(false, true)) {
            if (System.nanoTime() > endWaitingTime) {
                return false;
            }
        }
        return true;
    }

    /**
     * 释放时间轮的操作锁
     */
    public final void unLock() {
        this.monitor.set(false);
    }

}
