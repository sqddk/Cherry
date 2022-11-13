package cn.cherry.core.engine.base;

import cn.cherry.core.engine.base.TimePoint;

/**
 * 可被执行的定时任务的接口
 *
 * @since 2022/10/17
 * @author realDragonKing
 */
public abstract class Task {

    private TimePoint timePoint;
    private int taskId;

    /**
     * 设置执行的时间点
     *
     * @param timePoint 时间点
     */
    public void setTimePoint(TimePoint timePoint) {
        this.timePoint = timePoint;
    }

    /**
     * 获取执行的时间点
     *
     * @return 时间点
     */
    public TimePoint getTimePoint() {
        return this.timePoint;
    }

    /**
     * 设置任务的ID
     *
     * @param taskId 任务ID
     */
    public void setTaskID(int taskId) {
        this.taskId = taskId;
    }

    /**
     * 获取任务的ID
     *
     * @return 任务ID
     */
    public int getTaskID() {
        return this.taskId;
    }

    /**
     * 执行任务
     */
    public abstract void execute();

}
