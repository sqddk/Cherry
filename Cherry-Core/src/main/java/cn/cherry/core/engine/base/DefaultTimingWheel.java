package cn.cherry.core.engine.base;

import cn.cherry.core.engine.base.struct.PointerLinkedList;
import cn.cherry.core.infra.utils.BaseUtils;
import cn.cherry.core.infra.utils.TimeUtils;
import cn.cherry.core.engine.base.struct.DefaultPointerLinkedRing;
import cn.cherry.core.engine.base.struct.TaskList;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 时间轮的默认实现类，进行任务调度
 *
 * @since 2022/10/17
 * @author realDragonKing
 */
public class DefaultTimingWheel extends AbstractTimingWheel {

    /**
     * 当前时间点
     */
    private TimePoint currentTimePoint = TimePoint.getCurrentTimePoint();

    /**
     * 具体执行定时任务的线程池
     */
    private final ExecutorService executor;

    /**
     * 时间轮的轻量级操作锁
     */
    private final AtomicBoolean monitor = new AtomicBoolean(false);

    /**
     * 时间轮的数据结构实现，环形链表
     */
    private final PointerLinkedList<Map<Integer, TaskList>> linkedRing;

    public DefaultTimingWheel(long interval, int totalTicks, long waitTimeout) {
        super(interval, totalTicks, waitTimeout);

        this.executor = BaseUtils.createWorkerThreadPool(
                2,
                Runtime.getRuntime().availableProcessors() * 2,
                1000);
        this.linkedRing = new DefaultPointerLinkedRing(this.getTotalTicks());
    }

    /**
     * 提交一个新的定时任务
     * <p>
     * 如果时间轮锁已经被其他线程占用了，那么自旋等待锁空闲；如果自旋超出一定时间，就不再尝试获取锁，返回提交失败结果。
     *
     * @param task 定时任务
     * @return 提交是否成功 | 任务ID
     */
    public long submit(Task task) {
        int difference = TimeUtils.calDifference(this.currentTimePoint, task.getTimePoint(), this.getInterval());
        if (difference <= 0) {
            return -1;
        }
        int round = difference / this.getTotalTicks();
        int ticks = difference % this.getTotalTicks();

        if (!this.tryLock(true)) {
            return -1;
        }
        Map<Integer, TaskList> bucket = this.getSpecBucket(ticks);
        if (bucket == null) {
            return -1;
        }
        TaskList taskList = bucket.get(round);
        if (taskList == null) {
            taskList = new TaskList();
            bucket.put(round, taskList);
        }
        taskList.add(task);
        this.unLock();
        return task.getTaskID();
    }

    /**
     * 删除一个任务
     *
     * @param taskId 任务的id
     * @return 任务是否删除成功（成功返回 1， 失败返回 0）
     */
    @Override
    public int remove(int taskId) {
        return 0;
    }

    /**
     * 根据任务ID，移除一个定时任务
     * <p>
     * 如果时间轮锁已经被其他线程占用了，那么自旋等待锁空闲；如果自旋超出一定时间，就不再尝试获取锁，返回删除失败结果。
     *
     * @param timePoint 任务时间点
     * @param id 任务ID
     * @return 任务是否删除成功
     */
    public boolean remove(TimePoint timePoint, int id) {
        int difference = TimeUtils.calDifference(this.currentTimePoint, timePoint, this.interval);
        if (difference <= 0) {
            return false;
        }
        int round = difference / this.totalTicks;
        int ticks = difference % this.totalTicks;

        if (!this.tryLock(true)) {
            return false;
        }
        Map<Integer, TaskList> bucket = this.getSpecBucket(ticks);
        if (bucket == null) {
            return false;
        }
        TaskList taskList = bucket.get(round);
        if (taskList == null) {
            return false;
        }
        boolean result = taskList.removeTask(id);
        this.unLock();
        return result;
    }

    /**
     * 时间轮进行一次转动，这里坚持尝试获取锁，如果超时可以采用时间矫正机制减少损失和恢复其它任务的执行
     */
    @Override
    public void turn() {
        this.tryLock(false);
        // TODO 超时矫正机制等待完善
        this.linkedRing.moveNext();
        Map<Integer, TaskList> map = this.linkedRing.getPointer();
        this.currentTimePoint = TimePoint.getCurrentTimePoint();
        for (Integer round : map.keySet()) {
            TaskList list = map.remove(round);
            if (round == 0) {
                for (int i = 0; i < list.size(); i++) {
                    Task task = list.getPointer();
                    this.executor.submit(task::execute);
                    list.moveNext();
                }
            } else {
                map.put(round - 1, list);
            }
        }
        this.unLock();
    }

    /**
     * 尝试获取到时间轮的操作锁，若没获得则进行自旋，自旋超过一定时间则返回锁获取失败的信息
     *
     * @param stopNeed 是否超时停止
     * @return 最终是否成功获取
     */
    private boolean tryLock(boolean stopNeed) {
        if (stopNeed) {
            long startWaitingTime = System.nanoTime();
            while (!this.monitor.compareAndSet(false, true)) {
                if (System.nanoTime() - startWaitingTime > this.waitTimeout) return false;
            }
        } else {
            while (!this.monitor.compareAndSet(false, true)) {}
        }
        return true;
    }

    /**
     * 释放时间轮的操作锁
     */
    private void unLock() {
        this.monitor.set(false);
    }

    /**
     * 根据相差的刻度数，获取到对应刻度的任务链表容器
     *
     * @param ticks 目标时间点与当前时间点，相差的刻度数
     * @return 对应刻度的任务链表容器
     */
    private Map<Integer, TaskList> getSpecBucket(int ticks) {
        Map<Integer, TaskList> bucket;
        for (int i = 0; i < ticks; i++) {
            this.linkedRing.moveNext();
        }
        bucket = this.linkedRing.getPointer();
        for (int i = 0; i < ticks; i++) {
            this.linkedRing.movePrevious();
        }
        return bucket;
    }

    /**
     * 启用时间矫正机制，计算超时时间，对于延期的任务恢复执行，并且按照超时时间削减所有round
     *
     * @param wasteTicks 损失的刻度数
     */
    private void recover(int wasteTicks) {

    }

}