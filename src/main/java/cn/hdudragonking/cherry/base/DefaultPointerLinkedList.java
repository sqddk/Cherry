package cn.hdudragonking.cherry.base;

/**
 * 指针链表的默认实现类
 * @author realDragonKing
 */
public class DefaultPointerLinkedList<E> implements PointerLinkedList<E> {

    private int size;
    private int position;
    private Node<E> pointer;
    private Node<E> head;
    private Node<E> tail;

    public DefaultPointerLinkedList(){
        this.size = 0;
        this.position = -1;
    }

    /**
     * 在末端插入一个新的节点
     *
     * @param item 节点值
     */
    @Override
    public void add(E item) {
        Node<E> node = new Node<>(item);
        if (this.size == 0) {
            this.head = node;
            this.resetHead();
            return;
        }
        if (size == 1) {
            this.head.next = node;
            node.prev = this.head;
        } else {
            this.tail.next = node;
            node.prev = this.tail;
        }
        this.tail = node;
    }

    /**
     * 在指针处插入一个新的节点
     *
     * @param item 节点值
     */
    @Override
    public void insert(E item) {
        int position = this.getPointerPosition();
        if (position == -1) return;
        this.size++;
        Node<E> prev = this.pointer.prev,
                node = new Node<>(item);
        prev.next = node;
        node.prev = prev;
        node.next = this.pointer;
        this.pointer.prev = node;
        this.pointer = node;
    }

    /**
     * 删除并弹出指针处的节点
     *
     * @return 弹出的节点
     */
    @Override
    public Node<E> remove() {
        if (this.getPointerPosition() == -1) {
            return null;
        }
        this.size--;
        Node<E> prev = this.pointer.prev,
             next = this.pointer.next,
             pointer = this.pointer;
        if (prev != null) {
            prev.next = null;
            pointer.prev = null;
        }
        if (next == null) {
            this.pointer = prev;
            this.position--;
        } else {
            pointer.next = null;
            next.prev = null;
            if (prev != null) {
                prev.next = next;
                next.prev = prev;
            }
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
            Node<E> next = this.pointer.next;
            if (next != null) {
                this.pointer = next;
                this.position++;
            }
        }
    }

    /**
     * 移动指针到上一个节点
     */
    @Override
    public void movePrevious() {
        if (this.pointer != null) {
            Node<E> prev = this.pointer.prev;
            if (prev != null) {
                this.pointer = prev;
                this.position--;
            }
        }
    }

    /**
     * 重置指针到头节点
     */
    @Override
    public void resetHead() {
        this.pointer = this.head;
        this.position = 0;
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
            this.position = this.size() - 1;
        }
    }

    /**
     * 获取指针所指的节点
     *
     * @return 节点
     */
    @Override
    public Node<E> getPointer() {
        return this.pointer;
    }

    /**
     * 获取指针所指节点的位置
     *
     * @return 位置
     */
    @Override
    public int getPointerPosition() {
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
        Node<E> pointer = this.head;
        while (pointer != null) {
            builder.append(pointer.item.toString());
            if (pointer.next != null) {
                builder.append(',').append(' ');
            }
            pointer = pointer.next;
        }
        builder.append(']');
        return builder.toString();
    }
}
