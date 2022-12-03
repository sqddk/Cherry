package cn.cherry.core.engine.base;

import cn.cherry.core.infra.Task;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * 时间轮的槽位，被时间轮的默认实现{@link DefaultTimingWheel}所使用。
 * {@link TimeSlot}结合{@link SpinLocker}自旋锁，提供了在单个槽位上的线程安全的读写{@link TaskList}的方法
 *
 * @author realDragonKing
 */
public final class TimeSlot {

    private final Map<Integer, TaskList> map;
    private final SpinLocker locker;
    private final Executor executor;

    public TimeSlot(SpinLocker locker, Executor executor) {
        this.map = new HashMap<>();
        this.locker = locker;
        this.executor = executor;
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
     * 在这个时间槽位上，通过{@link Map#keySet()}遍历所有的key和{@link TaskList}，对key也就是“还需要转多少圈”进行减 1 操作再放回，
     * 若key为 0，则对这个{@link TaskList}上的所有{@link Task}调用{@link TaskList#remove()}，
     * 对返回的{@link Task}调用{@link Task#execute()}执行（具体执行将移交线程池）
     */
    public void decAndExecute() {
        Map<Integer, TaskList> map = this.map;
        TaskList taskList;
        if (this.locker.lock()) {
            for (int round : map.keySet()) {
                taskList = map.get(round);
                round--;
                map.put(round, taskList);
                if (round == 0 && taskList.getSize() > 0) {
                    taskList.resetTail();
                    for (int i = 0; i < taskList.getSize(); i++) {
                        Task task = taskList.remove();
                        this.executor.execute(task::execute);
                    }
                }
            }
            this.locker.unLock();
        }
    }

}
