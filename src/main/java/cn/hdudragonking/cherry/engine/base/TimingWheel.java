package cn.hdudragonking.cherry.engine.base;

import cn.hdudragonking.cherry.engine.task.Task;

/**
 * 时间轮的接口
 *
 * @since 2022/10/17
 * @author realDragonKing
 */
public interface TimingWheel {

    /**
     * 提交一个新的定时任务
     *
     * @param task 定时任务
     */
    void submit(Task task);

    /**
     * 时间轮进行一次转动
     */
    void turn();

}
