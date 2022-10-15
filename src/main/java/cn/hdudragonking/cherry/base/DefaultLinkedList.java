package cn.hdudragonking.cherry.base;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 单向链表的默认实现类
 * @author realDragonKing
 */
public class DefaultLinkedList implements LinkedList {

    private final AtomicInteger atomicInteger;
    private Node pointer;
    private Node head;
    private Node tail;
    public DefaultLinkedList(){
        this.atomicInteger = new AtomicInteger(0);
    }

    /**
     * 在尾部插入一个新的节点
     *
     * @param node 节点
     */
    @Override
    public void insertTail(Node node) {

    }

    /**
     * 在头部插入一个新的节点
     *
     * @param node 节点
     */
    @Override
    public void insertHead(Node node) {

    }

    /**
     * 在指定位置插入一个新的节点
     *
     * @param node     节点
     * @param position 指定位置
     */
    @Override
    public void insert(Node node, int position) {

    }

    /**
     * 删除并弹出尾部的节点
     *
     * @return 弹出的节点
     */
    @Override
    public Node deleteTail() {
        return null;
    }

    /**
     * 删除并弹出头部的节点
     *
     * @return 弹出的节点
     */
    @Override
    public Node deleteHead() {
        return null;
    }

    /**
     * 删除并弹出指定位置的节点
     *
     * @param position 指定位置
     * @return 弹出的节点
     */
    @Override
    public Node delete(int position) {
        return null;
    }

    /**
     * 获取并移动到下一个节点
     *
     * @return 下一个节点
     */
    @Override
    public Node next() {
        return null;
    }

    /**
     * 获取并移动到上一个节点
     *
     * @return 上一个节点
     */
    @Override
    public Node last() {
        return null;
    }

    /**
     * 获取链表的大小
     *
     * @return 链表的大小
     */
    @Override
    public int size() {
        return this.atomicInteger.get();
    }

}
