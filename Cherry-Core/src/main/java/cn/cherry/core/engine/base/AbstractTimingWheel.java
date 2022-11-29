package cn.cherry.core.engine.base;

import static cn.cherry.core.infra.utils.BaseUtils.*;

/**
 * 时间轮的主要抽象实现，提供了一系列操作接口
 *
 * @author realDragonKing
 */
public abstract class AbstractTimingWheel implements TimingWheel {

    private final long interval;
    private final int totalTicks;
    private final long waitTimeout;

    public AbstractTimingWheel(long interval, int totalTicks, long waitTimeout) {
        this.interval = checkPositive(interval, "interval");
        this.totalTicks = checkPositive(totalTicks, "totalTicks");
        this.waitTimeout = checkPositive(waitTimeout, "waitTimeout");
    }

    /**
     * @return 单次转动的时间间隔
     */
    public long getInterval() {
        return this.interval;
    }

    /**
     * @return 一轮转动的刻度总数
     */
    public int getTotalTicks() {
        return this.totalTicks;
    }

    /**
     * @return 自旋锁超时时间
     */
    public long getWaitTimeout() {
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
    public abstract int remove(int taskId);

}
