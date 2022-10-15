package cn.hdudragonking.cherry.scheduler;

import cn.hdudragonking.cherry.task.Task;

/**
 * 定时调度者的接口
 * @author realDragonKing
 */
public interface Scheduler {

    /**
     * 提交一个新的定时任务
     *
     * @param task 定时任务
     */
    void submit(Task task);

}
