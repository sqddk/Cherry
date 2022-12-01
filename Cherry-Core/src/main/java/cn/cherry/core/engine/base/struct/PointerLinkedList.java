package cn.cherry.core.engine.base.struct;

/**
 * {@link PointerLinkedList}是一种非环链表。<br/>
 * 作为{@link PointerLinked}的子接口，增加了诸如{@link #add(E value)}和{@link #remove()}的方法，
 * 借助pointer指针来修改自己的节点数量
 *
 * @author realDragonKing
 */
public interface PointerLinkedList<E> extends PointerLinked<E> {

    /**
     * 在指针指向位置添加一个节点
     *
     * @param value 节点值
     */
    void add(E value);

    /**
     * 在指针指向位置删除一个节点
     *
     * @return 节点值
     */
    E remove();

    /**
     * 重置指针到头节点
     */
    void resetHead();

    /**
     * 重置指针到尾节点
     */
    void resetTail();

}
