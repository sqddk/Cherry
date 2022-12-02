package cn.cherry.core.engine.base.struct;

/**
 * 可以被{@link PointerLinked}接口实现类使用的节点数据结构，提供访问、修改前一个节点{@link #prev}和后一个节点{@link #next}的方法<br/>
 * 注意，{@link Node}的值{@link #value}一旦被设置后将无法更改！
 *
 * @author realDragonKing、liehu3
 */
public class Node<E> {

    private Node<E> next;
    private Node<E> prev;
    private final E value;

    public Node(E value) {
        this(value, null, null);
    }

    public Node(E value, Node<E> prev, Node<E> next) {
        this.value = value;
        this.prev = prev;
        this.next = next;
    }

    /**
     * @return 后一个节点
     */
    public Node<E> getNext() {
        return this.next;
    }

    /**
     * @return 前一个节点
     */
    public Node<E> getPrev() {
        return this.prev;
    }

    /**
     * @param next 后一个节点
     */
    public void setNext(Node<E> next) {
        this.next = next;
    }

    /**
     * @param prev 前一个节点
     */
    public void setPrev(Node<E> prev) {
        this.prev = prev;
    }

    /**
     * 设置前、后节点
     *
     * @param prev 前一个节点
     * @param next 后一个节点
     */
    public void set(Node<E> prev, Node<E> next) {
        this.prev = prev;
        this.next = next;
    }

    /**
     * @return 当前节点的值
     */
    public E getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return this.value.toString();
    }

}
