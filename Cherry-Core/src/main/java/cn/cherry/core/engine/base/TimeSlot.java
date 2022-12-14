package cn.cherry.core.engine.base;

import cn.cherry.core.engine.base.task.DefaultTaskGroup;
import cn.cherry.core.engine.base.task.Task;
import cn.cherry.core.engine.base.task.TaskGroup;
import cn.cherry.core.engine.base.task.spec.Spec;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;

/**
 * 时间轮的槽位，被时间轮的默认实现{@link DefaultTimingWheel}所使用。
 * {@link TimeSlot}结合{@link SpinLocker}自旋锁，提供了在单个槽位上的线程安全的读写{@link TaskGroup}的方法
 *
 * @author realDragonKing、liehu3
 */
public final class TimeSlot {

    private final TimingWheel timingWheel;
    private final Map<Integer, TaskGroup> map;
    private final SpinLocker locker;

    public TimeSlot(TimingWheel timingWheel) {
        requireNonNull(timingWheel, "timingWheel");

        this.map = new HashMap<>();
        this.timingWheel = timingWheel;

        long waitTimeout = timingWheel.getWaitTimeout();
        this.locker = new SpinLocker(waitTimeout);
    }

    /**
     * 在这个时间槽位上，线程安全地提交一个任务
     *
     * @param task     任务
     * @param distance 已经处理好的相对时间距离
     * @return 任务的id（若提交失败则返回-1）
     */
    public long submitTask(Task task, int distance) {
        int totalTicks = this.timingWheel.getTotalTicks();
        int round = distance / totalTicks;

        if (this.locker.lock()) {
            TaskGroup group = this.map.get(round);
            if (group == null) {
                group = new DefaultTaskGroup(this.timingWheel);
                this.map.put(round, group);
            }

            long id = group.addTask(task);
            this.locker.unLock();
            return id;
        } else
            return -1;
    }

    /**
     * 在这个时间槽位上，线程安全地删除一个任务
     *
     * @param taskId   任务的id
     * @param distance 已经处理好的相对时间距离
     * @return 任务是否删除成功
     */
    public boolean removeTask(long taskId, int distance) {
        int totalTicks = this.timingWheel.getTotalTicks();
        int round = distance / totalTicks;

        if (this.locker.lock()) {
            TaskGroup group = this.map.get(round);
            if (group == null) {
                return false;
            }

            int removeNum = group.removeTask(Spec.TaskId, taskId);
            this.locker.unLock();
            return removeNum > 0;
        } else
            return false;
    }

    /**
     * 在这个时间槽位上，通过{@link Map#keySet()}遍历所有的key和{@link TaskGroup}，对key也就是“还需要转多少圈”进行减 1 操作再放回，
     * 若key为 0，则对这个{@link TaskGroup}上的所有{@link Task}调用{@link Task#execute()}执行（具体执行将移交线程池）
     */
    public void decAndExecute() {
        Map<Integer, TaskGroup> map = this.map;
        TaskGroup group;
        if (this.locker.lock()) {
            for (int round : map.keySet()) {
                group = map.get(round);
                round--;
                map.put(round, group);
                if (round == 0 && group != null) {
                    group.executeAll();
                }
            }
            this.locker.unLock();
        }
    }

}
