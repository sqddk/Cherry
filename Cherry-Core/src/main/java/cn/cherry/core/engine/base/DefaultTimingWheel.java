package cn.cherry.core.engine.base;

import cn.cherry.core.infra.Task;

/**
 * 时间轮{@link Rotatable}和{@link TimingWheel}的默认具体实现
 *
 * @author realDragonKing
 */
public class DefaultTimingWheel extends TimingWheel {

    public DefaultTimingWheel(long interval, int totalTicks, long waitTimeout) {
        super(interval, totalTicks, waitTimeout);
    }

    /**
     * 提交一个任务
     *
     * @param task 任务
     * @return 任务的id
     */
    @Override
    public long submit(Task task) {
        return 0;
    }

    /**
     * 删除一个任务
     *
     * @param taskId 任务的id
     * @return 任务是否删除成功（成功返回 1， 失败返回 0）
     */
    @Override
    public int remove(long taskId) {
        return 0;
    }

    /**
     * 时间轮进行一次转动
     */
    @Override
    public void turn() {

    }

}
