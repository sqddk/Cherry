package cn.hdudragonking.cherry.base;

import java.util.concurrent.atomic.AtomicInteger;

import static cn.hdudragonking.cherry.utils.BaseUtils.*;

/**
 * ˫�������Ĭ��ʵ����
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
     * ��ĩ�˲���һ���µĽڵ�
     *
     * @param node �ڵ�
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
     * ��ָ�봦����һ���µĽڵ�
     *
     * @param node �ڵ�
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
     * ɾ��������ָ�봦�Ľڵ�
     *
     * @return �����Ľڵ�
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
     * �ƶ�ָ�뵽��һ���ڵ�
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
     * �ƶ�ָ�뵽��һ���ڵ�
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
     * ����ָ�뵽ͷ�ڵ�
     */
    @Override
    public void resetHead() {
        this.pointer = this.head;
        this.position.set(0);
    }

    /**
     * ����ָ�뵽β�ڵ�
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
     * ��ȡָ����ָ�Ľڵ�
     *
     * @return �ڵ�
     */
    @Override
    public Node getPointer() {
        return this.pointer;
    }

    /**
     * ��ȡָ����ָ�ڵ��λ��
     *
     * @return λ��
     */
    @Override
    public int getPointerPosition() {
        return this.position.get();
    }

    /**
     * ��ȡ����Ĵ�С
     *
     * @return ����Ĵ�С
     */
    @Override
    public int size() {
        return this.size.get();
    }

    /**
     * �����ַ���������
     *
     * @return �ַ����͵�����
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
