package cn.cherry.core.engine.base.struct;

/**
 * {@link PointerLinkedRing}是一种环形链表。<br/>
 * 在{@link PointerLinkedRing}的具体实现类中，不断地{@link #moveNext()}将回到起点处的节点位置。
 * 在{@link PointerLinkedRing}中，所有的节点{@link Node}应当在构造方法中就被初始化完成并建立指向关系。
 *
 * @author realDragonKing
 */
public interface PointerLinkedRing<E> extends PointerLinked<E> {}
