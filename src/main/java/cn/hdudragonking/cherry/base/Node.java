package cn.hdudragonking.cherry.base;


/**
 * 环形链表的节点
 * @author realDragonKing
 */
public class Node<E> {

    public Node<E> next;
    public Node<E> prev;
    public E item;

    public Node(E item){
        this.item = item;
    }
}
