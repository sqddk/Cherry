package cn.cherry.core.engine.task.spec;

import cn.cherry.core.engine.task.Task;
import cn.cherry.core.engine.task.TaskKeeper;

/**
 * 装载了{@link Task}任务一种特征的节点<br/>
 * 有多个{@link SpecNode}被保存在不同的{@link SpecSelector}特征检索者的数据结构当中，
 * 找到任何一个{@link SpecNode}都可以通过{@link TaskKeeper}间接影响到其它的{@link SpecNode}
 *
 * @author realDragonKing
 */
public abstract class SpecNode<E> {

    private final E value;
    private final TaskKeeper keeper;

    public SpecNode(E value, TaskKeeper keeper) {
        this.value = value;
        this.keeper = keeper;
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
     * 把自己从保存自己的{@link SpecSelector}当中移除
     */
    public abstract void removeSelf();

}
