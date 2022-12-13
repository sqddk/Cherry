package cn.cherry.core.engine.base.task.spec;

import cn.cherry.core.engine.base.task.Task;
import cn.cherry.core.engine.base.task.TaskKeeper;

/**
 * 装载了{@link Task}任务一种特征的节点<br/>
 * 有多个{@link SpecNode}被保存在不同的{@link SpecSelector}特征检索者的数据结构当中，
 * 找到任何一个{@link SpecNode}都可以通过{@link TaskKeeper}感知到其它的{@link SpecNode}
 *
 * @author realDragonKing
 */
public class SpecNode<E> {

    private final E value;
    private final TaskKeeper keeper;
    private final SpecSelector<E> specSelector;

    public SpecNode(E value, TaskKeeper keeper, SpecSelector<E> specSelector) {
        this.value = value;
        this.keeper = keeper;
        this.specSelector = specSelector;
    }

    /**
     * @return 任务特征值
     */
    public final E getSpecValue() {
        return value;
    }

    /**
     * @return 所属的任务保存者
     */
    public final TaskKeeper getTaskKeeper() {
        return this.keeper;
    }

    /**
     * @return 特征搜寻者
     */
    public SpecSelector<E> getSpecSelector() {
        return specSelector;
    }
}
