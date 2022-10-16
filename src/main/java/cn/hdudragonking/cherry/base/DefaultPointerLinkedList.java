package cn.hdudragonking.cherry.base;

/**
 * ָ�������Ĭ��ʵ����
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
     * ��ָ�봦����һ���µĽڵ�
     *
     * @param item �ڵ�ֵ
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
     * ɾ��������ָ�봦�Ľڵ�
     *
     * @return �����Ľڵ�
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
            this.position = this.size() - 1;
        }
    }

    /**
     * ��ȡָ����ָ�Ľڵ�
     *
     * @return �ڵ�
     */
    @Override
    public Node<E> getPointer() {
        return this.pointer;
    }

    /**
     * ��ȡָ����ָ�ڵ��λ��
     *
     * @return λ��
     */
    @Override
    public int getPointerPosition() {
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
}
