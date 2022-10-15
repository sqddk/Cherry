package cn.hdudragonking.cherry.task;

import cn.hdudragonking.cherry.base.TimePoint;

/**
 * 执行提醒任务，提醒网络上的定时任务提交者可以执行
 * @author realDragonKing
 */
public class ReminderTask implements Task {

    private TimePoint timePoint;

    /**
     * 设置执行的时间点
     *
     * @param timePoint 时间点
     * @return 时间点是否有效
     */
    @Override
    public void setTimePoint(TimePoint timePoint) {
        this.timePoint = timePoint;
    }

    /**
     * 获取执行的时间点
     *
     * @return 时间点
     */
    @Override
    public TimePoint getTimePoint() {
        return this.timePoint;
    }

    /**
     * 执行任务
     */
    @Override
    public void execute() {

    }
}
