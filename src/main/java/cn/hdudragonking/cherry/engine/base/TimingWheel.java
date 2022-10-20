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
    boolean remove(TimePoint timePoint, String id);

    /**
     * 时间轮进行一次转动
     */
    void turn();

    /**
     * 尝试获取到时间轮的操作锁，若没获得则进行自旋，自旋超过一定时间则返回锁获取失败的信息
     *
     * @param stopNeed 是否超时停止
     * @return 最终是否成功获取
     */
    boolean tryLock(boolean stopNeed);

    /**
     * 释放时间轮的操作锁
     */
    void unLock();

    /**
     * 启用时间矫正机制，计算超时时间，对于延期的任务恢复执行，并且按照超时时间削减所有round
     *
     * @param wasteTicks 损失的刻度数
     */
    void recover(int wasteTicks);

}
