package cn.hdudragonking.cherry.base;

/**
 * 指针链表的接口
 * @author realDragonKing
 */
public interface PointerLinkedList<E> {

    /**
     * 在末端插入一个新的节点
     *
     * @param item 节点值
     */
    void add(E item);

    /**
     * 删除并弹出指针处的节点
     *
     * @return 弹出的节点
     */
    Node<E> remove();

    /**
     * 移动指针到下一个节点
     */
    void moveNext();

    /**
     * 移动指针到上一个节点
     */
    void movePrevious();

    /**
     * 重置指针到头节点
     */
    void resetHead();

    /**
     * 重置指针到尾节点
     */
    void resetTail();

    /**
     * 获取指针所指的节点
     *
     * @return 节点
     */
    Node<E> getPointer();

    /**
     * 获取指针所指节点的位置
     *
     * @return 位置
     */
    int getPointerPosition();

    /**
     * 获取链表的大小
     *
     * @return 链表的大小
     */
    int size();

}
