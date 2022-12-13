package cn.cherry.core.engine.base.task;

/**
 * 装载了{@link Task}任务一种特征的节点<br/>
 * 有多个{@link SpecNode}被保存在不同的{@link SpecSelector}特征检索者的数据结构当中，
 * 找到任何一个{@link SpecNode}都可以通过{@link TaskKeeper}感知到其它的{@link SpecNode}
 *
 * @author realDragonKing
 */
public class SpecNode<E> {

    private final TaskKeeper keeper;
    private final E spec;

    public SpecNode(E spec, TaskKeeper keeper) {
        this.spec = spec;
        this.keeper = keeper;
    }

    /**
     * @return 任务特征值
     */
    public final E getSpec() {
        return spec;
    }

    /**
     * @return 所属的任务保存者
     */
    public final TaskKeeper getTaskKeeper() {
        return this.keeper;
    }

}
