package cn.cherry.core.engine.base.struct;

import java.util.List;

/**
 * {@link PointerLinkedRing}是一种环形链表。<br/>
 * 在{@link PointerLinkedRing}中，不断地{@link #moveNext()}将回到起点处的节点位置。
 * 在{@link PointerLinkedRing}中，所有的节点{@link Node}应当在构造方法中就被初始化完成并建立指向关系。
 * 在这之后，所有节点都无法被修改。
 *
 * @author realDragonKing
 */
public class PointerLinkedRing<E> implements PointerLinked<E> {

    private final int size;
    private Node<E> pointer;
    private Node<E> first;
    public PointerLinkedRing(List<E> values) {
        if (values.size() == 0) {
            throw new IllegalArgumentException("至少要有一个节点值来完成初始化！");
        }
        first = new Node<>(values.get(0));
        Node<E> prev = first;
        Node<E> next;
        for (E value : values) {
            next = new Node<>(value);
            prev.setNext(next);
            next.setPrev(prev);
            prev = next;
        }
        this.pointer = first;
        prev.setNext(first);
        first.setPrev(this.pointer);
        this.size = values.size();
    }

    /**
     * @return pointer指针所指的节点的值
     */
    @Override
    public E getValue() {
        return this.pointer.getValue();
    }

    /**
     * 移动指针到下一个节点
     */
    @Override
    public void moveNext() {
        Node<E> pointer = this.pointer;
        this.pointer = pointer.getNext();
    }

    /**
     * 移动指针到上一个节点
     */
    @Override
    public void movePrev() {
        Node<E> pointer = this.pointer;
        this.pointer = pointer.getNext();
    }

    /**
     * @return 大小（有多少个{@link Node}节点加入了{@link PointerLinked}）
     */
    @Override
    public int getSize() {
        return this.size;
    }

}