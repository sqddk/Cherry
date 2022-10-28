package cn.hdudragonking.cherry.engine.base.struct;

/**
 * 指针链表的默认实现类
 *
 * @since 2022/10/17
 * @author realDragonKing
 */
public class DefaultPointerLinkedList<E> implements PointerLinkedList<E> {

    private int size;
    private int position;
    private Node<E> pointer;
    private final Node<E> head;
    private final Node<E> tail;

    public DefaultPointerLinkedList(){
        this.size = 0;
        this.position = -1;
        this.head = new Node<>(null);
        this.tail = new Node<>(null);
    }

    /**
     * 在末端插入一个新的节点
     *
     * @param item 节点值
     */
    @Override
    public void add(E item) {
        if (item == null) return;
        Node<E> node = new Node<>(item);
        this.size++;
        if (this.size == 1) {
            this.head.next = node;
            node.prev = this.head;
            this.pointer = node;
            this.position++;
        } else {
            Node<E> prev = this.tail.prev;
            prev.next = node;
            node.prev = prev;
        }
        this.tail.prev = node;
        node.next = this.tail;
    }

    /**
     * 删除并弹出指针处的节点值
     *
     * @return 弹出的节点值
     */
    @Override
    public E remove() {
        if (this.position == -1) {
            return null;
        }
        Node<E> prev = this.pointer.prev,
                next = this.pointer.next,
                node = this.pointer;
        prev.next = next;
        next.prev = prev;
        this.size--;
        if (this.size == 0) {
            this.pointer = null;
            this.position--;
        } else {
            this.pointer = next;
        }
        node.next = null;
        node.prev = null;
        return node.item;
    }

    /**
     * 移动指针到下一个节点
     */
    @Override
    public void moveNext() {
        if (this.position + 1 < this.size && this.position > -1) {
            this.pointer = this.pointer.next;
            this.position++;
        }
    }

    /**
     * 移动指针到上一个节点
     */
    @Override
    public void movePrevious() {
        if (this.position > 0) {
            this.pointer = this.pointer.prev;
            this.position--;
        }
    }

    /**
     * 重置指针到头节点
     */
    @Override
    public void resetHead() {
        if (this.size > 0) {
            this.pointer = this.head.next;
            this.position = 0;
        }
    }

    /**
     * 重置指针到尾节点
     */
    @Override
    public void resetTail() {
        if (this.size > 0) {
            this.pointer = this.tail.prev;
            this.position = this.size - 1;
        }
    }

    /**
     * 获取指针所指的节点
     *
     * @return 节点
     */
    @Override
    public E getPointer() {
        return this.pointer.item;
    }

    /**
     * 获取指针所指节点的位置
     *
     * @return 位置
     */
    @Override
    public int getPosition() {
        return this.position;
    }

    /**
     * 获取链表的大小
     *
     * @return 链表的大小
     */
    @Override
    public int size() {
        return this.size;
    }

    /**
     * 返回字符串型链表
     *
     * @return 字符串型的链表
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder().append('[');
        if (this.size() == 0) {
            return builder.append(']').toString();
        }
        Node<E> pointer = this.head.next;
        int position = 0;
        while (position < this.size) {
            builder.append(pointer.item.toString());
            pointer = pointer.next;
            position++;
            if (position < this.size) {
                builder.append(',').append(' ');
            }
        }
        builder.append(']');
        return builder.toString();
    }

    /**
     * 环形链表的节点
     * @author realDragonKing
     */
    private static class Node<E> {

        private Node<E> next;
        private Node<E> prev;
        private final E item;

        private Node(E item){
            this.item = item;
        }

    }

}
