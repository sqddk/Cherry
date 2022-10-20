package cn.hdudragonking.cherry.engine.base.struct;


/**
 * ָ�������Ĭ��ʵ����
 *
 * @since 2022/10/17
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
     * ��ĩ�˲���һ���µĽڵ�
     *
     * @param item �ڵ�ֵ
     */
    @Override
    public void add(E item) {
        Node<E> node = new Node<>(item);
        if (this.size == 0) {
            this.size++;
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
        this.size++;
        this.tail = node;
    }

    /**
     * ɾ��������ָ�봦�Ľڵ�ֵ
     *
     * @return �����Ľڵ�ֵ
     */
    @Override
    public E remove() {
        if (this.position == -1) {
            return null;
        }
        Node<E> prev = this.pointer.prev,
             next = this.pointer.next,
             pointer = this.pointer;
        if (this.position == 0) {
            if (this.size == 1) this.position = -1;
            else {
                if (this.size == 2) this.tail = null;
                next.prev = null;
                pointer.next = null;
            }
            this.head = next;
            this.pointer = next;
        } else {
            if (this.position + 1 == this.size) {
                this.tail = this.size == 2 ? null : prev;
                this.pointer = prev;
                this.position--;
            } else {
                next.prev = prev;
                this.pointer = next;
            }
            pointer.prev = null;
            pointer.next = null;
            prev.next = next;
        }
        this.size--;
        return pointer.item;
    }

    /**
     * �ƶ�ָ�뵽��һ���ڵ�
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
     * �ƶ�ָ�뵽��һ���ڵ�
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
     * ����ָ�뵽ͷ�ڵ�
     */
    @Override
    public void resetHead() {
        this.pointer = this.head;
        this.position = 0;
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
            this.position = this.size - 1;
        }
    }

    /**
     * ��ȡָ����ָ�Ľڵ�
     *
     * @return �ڵ�
     */
    @Override
    public E getPointer() {
        return this.pointer.item;
    }

    /**
     * ��ȡָ����ָ�ڵ��λ��
     *
     * @return λ��
     */
    @Override
    public int getPosition() {
        return this.position;
    }

    /**
     * ��ȡ����Ĵ�С
     *
     * @return ����Ĵ�С
     */
    @Override
    public int size() {
        return this.size;
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

    /**
     * ��������Ľڵ�
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
