package cn.hdudragonking.cherry.base;

/**
 * 单向链表的接口
 * @author realDragonKing
 */
public interface LinkedList {

    /**
     * 在尾部插入一个新的节点
     *
     * @param node 节点
     */
    void insertTail(Node node);

    /**
     * 在头部插入一个新的节点
     *
     * @param node 节点
     */
    void insertHead(Node node);

    /**
     * 在指定位置插入一个新的节点
     *
     * @param node 节点
     * @param position 指定位置
     */
    void insert(Node node, int position);

    /**
     * 删除并弹出尾部的节点
     *
     * @return 弹出的节点
     */
    Node deleteTail();

    /**
     * 删除并弹出头部的节点
     *
     * @return 弹出的节点
     */
    Node deleteHead();

    /**
     * 删除并弹出指定位置的节点
     *
     * @param position 指定位置
     * @return 弹出的节点
     */
    Node delete(int position);

    /**
     * 获取并移动到下一个节点
     *
     * @return 下一个节点
     */
    Node next();

    /**
     * 获取并移动到上一个节点
     *
     * @return 上一个节点
     */
    Node last();

    /**
     * 获取链表的大小
     *
     * @return 链表的大小
     */
    int size();

}
