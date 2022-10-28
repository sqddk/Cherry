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
     * @return 提交是否成功 | 任务ID
     */
    int[] submit(Task task);

    /**
     * 根据任务ID，移除一个定时任务
     *
     * @param timePoint 任务时间点
     * @param id 任务ID
     * @return 任务是否删除成功
     */
    boolean remove(TimePoint timePoint, int id);

    /**
     * 时间轮进行一次转动
     */
    void turn();

}
