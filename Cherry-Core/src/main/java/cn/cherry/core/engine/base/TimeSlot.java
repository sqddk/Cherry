package cn.cherry.core.engine.base;

import cn.cherry.core.engine.base.task.TaskGroup;
import cn.cherry.core.engine.base.task.Task;

import java.util.HashMap;
import java.util.Map;

/**
 * 时间轮的槽位，被时间轮的默认实现{@link DefaultTimingWheel}所使用。
 * {@link TimeSlot}结合{@link SpinLocker}自旋锁，提供了在单个槽位上的线程安全的读写{@link TaskGroup}的方法
 *
 * @author realDragonKing
 */
public final class TimeSlot {

    private final Map<Integer, TaskGroup> map;
    private final SpinLocker locker;

    public TimeSlot(TimingWheel timingWheel) {
        this.map = new HashMap<>();
        long waitTimeout = timingWheel.getWaitTimeout();
        this.locker = new SpinLocker(waitTimeout);
    }

    /**
     * 在这个时间槽位上，线程安全地提交一个任务
     *
     * @param task 任务
     * @return 任务的id（若提交失败则返回-1）
     */
    public long invokeSubmit(Task task) {
        return 0;
    }

    /**
     * 在这个时间槽位上，线程安全地删除一个任务
     *
     * @param taskId 任务的id
     * @return 任务是否删除成功
     */
    public boolean invokeRemove(long taskId) {
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
