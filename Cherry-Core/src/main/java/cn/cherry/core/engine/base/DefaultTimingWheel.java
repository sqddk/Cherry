package cn.cherry.core.engine.base;

import cn.cherry.core.engine.base.struct.PointerLinked;
import cn.cherry.core.engine.base.struct.PointerLinkedRing;
import cn.cherry.core.infra.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 时间轮{@link Rotatable}和{@link TimingWheel}的默认具体实现
 *
 * @author realDragonKing
 */
public class DefaultTimingWheel extends TimingWheel {

    private final PointerLinked<Map<Integer, TaskList>> ring;

    public DefaultTimingWheel(long interval, int totalTicks, long waitTimeout) {
        super(interval, totalTicks, waitTimeout);
        List<Map<Integer, TaskList>> list = new ArrayList<>(this.getTotalTicks());
        for (int i = 0; i < this.getTotalTicks(); i++) {
            list.add(new HashMap<>());
        }
        this.ring = new PointerLinkedRing<>(list);
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
