package cn.hdudragonking.cherry.base;


/**
 * ��������Ľڵ�
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
