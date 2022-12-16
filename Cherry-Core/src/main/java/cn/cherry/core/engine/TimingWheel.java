package cn.cherry.core.engine;

import cn.cherry.core.engine.task.Task;

import static io.netty.util.internal.ObjectUtil.checkPositive;

/**
 * {@link TimingWheel}是{@link Rotatable}接口的抽象实现，被定义为时间轮算法的实现者
 *
 * @author realDragonKing
 */
public abstract class TimingWheel implements Rotatable {

    private long currentTimeValue; // 单位为毫秒（ms）
    private final long interval; // 单位为毫秒（ms）
    private final int totalTicks;
    private final long waitTimeout; // 单位为纳秒（ns）
    private final int maxTaskSize;

    public TimingWheel(long interval, int totalTicks, long waitTimeout, int maxTaskSize) {
        this.interval = checkPositive(interval, "interval");
        this.totalTicks = checkPositive(totalTicks, "totalTicks");
        this.waitTimeout = checkPositive(waitTimeout, "waitTimeout");
        this.maxTaskSize = maxTaskSize;
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
     * @return 单次转动可以执行的最大任务数量
     */
    public final int getMaxTaskSize() {
        return this.maxTaskSize;
    }

    /**
     * 设置时间轮持有的绝对时间
     *
     * @param timeValue 绝对时间值
     */
    public final void setCurrentTimeValue(long timeValue) {
        this.currentTimeValue = timeValue;
    }

    /**
     * 增加时间轮持有的绝对时间值
     */
    public final void addCurrentTimeValue() {
        this.currentTimeValue += this.interval;
    }

    /**
     * @return 时间轮持有的绝对时间值
     */
    public final long getCurrentTimeValue() {
        return this.currentTimeValue;
    }

    /**
     * 结合当前的{@link #currentTimeValue}和{@link #interval}计算相对时间距离
     *
     * @param timeValue 绝对时间值
     * @return 相对时间距离
     */
    public final long calDistance(long timeValue) {
        return (timeValue - this.currentTimeValue) / this.interval;
    }

    /**
     * 执行一个任务
     *
     * @param task 任务
     */
    public abstract void executeTask(Task task);

    /**
     * 根据执行时间点和当前时间点的相对时间距离，取到对应的槽位{@link TimeSlot}
     *
     * @param distance 相对时间距离
     * @return 时间轮槽位
     */
    public abstract TimeSlot getSlot(long distance);

}
