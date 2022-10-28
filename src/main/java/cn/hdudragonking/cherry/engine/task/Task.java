package cn.hdudragonking.cherry.engine.task;

import cn.hdudragonking.cherry.engine.base.TimePoint;

/**
 * 可被执行的定时任务的接口
 *
 * @since 2022/10/17
 * @author realDragonKing
 */
public interface Task {

    /**
     * 获取执行的时间点
     *
     * @return 时间点
     */
    TimePoint getTimePoint();

    /**
     * 设置任务的ID
     *
     * @param id 任务ID
     */
    void setTaskID(int id);

    /**
     * 获取任务的ID
     *
     * @return 任务ID
     */
    int getTaskID();

    /**
     * 执行任务
     */
    void execute();

}
