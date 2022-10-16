package cn.hdudragonking.cherry.scheduler;

import cn.hdudragonking.cherry.task.Task;

/**
 * 调度者的默认实现类，使用时间轮算法进行任务调度
 * @author realDragonKing
 */
public class DefaultScheduler implements Scheduler {

    /**
     * 提交一个新的定时任务
     *
     * @param task 定时任务
     */
    @Override
    public void submit(Task task) {

    }
}
