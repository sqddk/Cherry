package cn.hdudragonking.cherry.base;

import java.util.concurrent.atomic.AtomicInteger;

import static cn.hdudragonking.cherry.utils.BaseUtils.*;

/**
 * 双向链表的默认实现类
 * @author realDragonKing
 */
public class DefaultLinkedList implements LinkedList {

    private final AtomicInteger size;
    private final AtomicInteger position;
    private Node pointer;
    private Node head;
    private Node tail;

    public DefaultLinkedList(){
        this.size = new AtomicInteger(0);
        this.position = new AtomicInteger(-1);
    }

    /**
     * 在末端插入一个新的节点
     *
     * @param node 节点
     */
    @Override
    public void add(Node node) {
        int size = this.size.getAndAdd(1);
        node.id = size;
        if (size == 0) {
            this.head = node;
            this.resetHead();
            return;
        }
        bind(size == 1 ? this.head : this.tail, node);
        this.tail = node;
    }

    /**
     * 在指针处插入一个新的节点
     *
     * @param node 节点
     */
    @Override
    public void insert(Node node) {
        node.id = this.size();
        int position = this.getPointerPosition();
        if (position == -1) return;
        this.size.getAndAdd(1);
        Node previous = this.pointer.getPrevious();
        bind(previous, node);
        bind(node, pointer);
        this.pointer = node;
    }

    /**
     * 删除并弹出指针处的节点
     *
     * @return 弹出的节点
     */
    @Override
    public Node remove() {
        int position = this.getPointerPosition();
        if (position == -1) return null;
        this.size.getAndAdd(-1);
        Node previous = this.pointer.getPrevious(),
             next = this.pointer.getNext(),
             pointer = this.pointer;
        if (previous != null) unBind(previous, pointer);
        if (next == null) {
            this.pointer = previous;
            this.position.getAndAdd(-1);
        } else {
            unBind(pointer, next);
            if (previous != null) bind(previous, next);
            this.pointer = next;
        }
        return pointer;
    }

    /**
     * 移动指针到下一个节点
     */
    @Override
    public void moveNext() {
        if (this.pointer != null) {
            Node next = this.pointer.getNext();
            if (next != null) {
                this.pointer = next;
                this.position.getAndAdd(1);
            }
        }
    }

    /**
     * 移动指针到上一个节点
     */
    @Override
    public void movePrevious() {
        if (this.pointer != null) {
            Node previous = this.pointer.getPrevious();
            if (previous != null) {
                this.pointer = previous;
                this.position.getAndAdd(-1);
            }
        }
    }

    /**
     * 重置指针到头节点
     */
    @Override
    public void resetHead() {
        this.pointer = this.head;
        this.position.set(0);
    }

    /**
     * 重置指针到尾节点
     */
    @Override
    public void resetTail() {
        if (size() <= 1) {
            this.resetHead();
        } else {
            this.pointer = this.tail;
            this.position.set(this.size() - 1);
        }
    }

    /**
     * 获取指针所指的节点
     *
     * @return 节点
     */
    @Override
    public Node getPointer() {
        return this.pointer;
    }

    /**
     * 获取指针所指节点的位置
     *
     * @return 位置
     */
    @Override
    public int getPointerPosition() {
        return this.position.get();
    }

    /**
     * 获取链表的大小
     *
     * @return 链表的大小
     */
    @Override
    public int size() {
        return this.size.get();
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
        Node pointer = this.head;
        while (pointer != null) {
            builder.append(pointer.id);
            if (pointer.getNext() != null) {
                builder.append(',').append(' ');
            }
            pointer = pointer.getNext();
        }
        builder.append(']');
        return builder.toString();
    }
}
