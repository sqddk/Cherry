package cn.hdudragonking.cherry.task;

import cn.hdudragonking.cherry.base.TimePoint;

/**
 * 可被执行的定时任务的接口
 * @author realDragonKing
 */
public interface Task {

    /**
     * 设置执行的时间点
     *
     * @param timePoint 时间点
     */
    void setTimePoint(TimePoint timePoint);

    /**
     * 获取执行的时间点
     *
     * @return 时间点
     */
    TimePoint getTimePoint();

    /**
     * 执行任务
     */
    void execute();

}
