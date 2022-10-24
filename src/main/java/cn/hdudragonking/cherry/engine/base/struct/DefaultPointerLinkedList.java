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
    private final Node<E> head;
    private final Node<E> tail;

    public DefaultPointerLinkedList(){
        this.size = 0;
        this.position = -1;
        this.head = new Node<>(null);
        this.tail = new Node<>(null);
    }

    /**
     * ��ĩ�˲���һ���µĽڵ�
     *
     * @param item �ڵ�ֵ
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
     * �ƶ�ָ�뵽��һ���ڵ�
     */
    @Override
    public void moveNext() {
        if (this.position + 1 < this.size && this.position > -1) {
            this.pointer = this.pointer.next;
            this.position++;
        }
    }

    /**
     * �ƶ�ָ�뵽��һ���ڵ�
     */
    @Override
    public void movePrevious() {
        if (this.position > 0) {
            this.pointer = this.pointer.prev;
            this.position--;
        }
    }

    /**
     * ����ָ�뵽ͷ�ڵ�
     */
    @Override
    public void resetHead() {
        if (this.size > 0) {
            this.pointer = this.head.next;
            this.position = 0;
        }
    }

    /**
     * ����ָ�뵽β�ڵ�
     */
    @Override
    public void resetTail() {
        if (this.size > 0) {
            this.pointer = this.tail.prev;
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
