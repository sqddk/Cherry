package cn.cherry.core.engine.base.struct;

/**
 * 指针链表的默认实现类
 *
 * @author realDragonKing、liehu1
 */
public class DefaultPointerLinkedList<E> implements PointerLinkedList<E> {

    private Node<E> pointer;
    private final Node<E> head = new Node<>(null);
    private final Node<E> tail = new Node<>(null);
    private int size = 0;

    /**
     * 在末端插入一个新的节点
     *
     * @param value 节点值
     */
    @Override
    public void add(E value) {
        if (value == null) return;
        Node<E> node = new Node<>(value);
        this.size++;
        if (this.size == 1) {
            this.head.setNext(node);
            node.setPrev(this.head);
            this.pointer = node;
        } else {
            Node<E> prev = this.tail.getPrev();
            prev.setNext(node);
            node.setPrev(prev);
        }
        this.tail.setPrev(node);
        node.setNext(tail);
    }

    /**
     * 删除并弹出指针处的节点值
     */
    @Override
    public E remove() {
        if (this.size == 0) {
            return null;
        }
        Node<E> prev = this.pointer.getPrev(),
                next = this.pointer.getNext(),
                node = this.pointer;
        prev.setNext(next);
        next.setPrev(prev);
        this.size--;
        if (this.size == 0) {
            this.pointer = null;
        } else {
            this.pointer = next == this.tail ? prev : next;
        }
        node.set(null, null);
        return node.getValue();
    }

    @Override
    public E getValue() {
        return this.pointer.getValue();
    }

    /**
     * 移动指针到下一个节点
     */
    @Override
    public void moveNext() {
        if (this.pointer.getNext() != this.tail ) {
            this.pointer = this.pointer.getNext();
        }
    }

    /**
     * 移动指针到上一个节点
     */
    @Override
    public void movePrev() {
        if (this.pointer.getPrev() != this.head) {
            this.pointer = this.pointer.getPrev();
        }
    }

    @Override
    public int getSize() {
        return this.size;
    }

    /**
     * 重置指针到头节点
     */
    @Override
    public void resetHead() {
        if (this.pointer.getPrev() != this.head) {
            this.pointer = this.head.getNext();
        }
    }

    /**
     * 重置指针到尾节点
     */
    @Override
    public void resetTail() {
        if (this.pointer.getNext() != this.tail) {
            this.pointer = this.tail.getNext();
        }
    }

    /**
     * 返回字符串型链表
     *
     * @return 字符串型的链表
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder().append('[');
        if (this.size == 0) {
            return builder.append(']').toString();
        }
        Node<E> pointer = this.head.getNext();
        int position = 0;
        while (position < this.size) {
            builder.append(pointer.getValue().toString());
            pointer = pointer.getNext();
            position++;
            if (position < this.size) {
                builder.append(',').append(' ');
            }
        }
        builder.append(']');
        return builder.toString();
    }


}
