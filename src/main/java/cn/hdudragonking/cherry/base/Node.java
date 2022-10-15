package cn.hdudragonking.cherry.base;

import cn.hdudragonking.cherry.task.Task;

/**
 * 环形链表的节点
 * @author realDragonKing
 */
public class Node {

    private Node next;
    private Node last;
    private LinkedList linkedList;
    private Task task;

    public Node(){}

    /**
     * 放入链表
     *
     * @param linkedList 链表
     */
    public void setLinkedList(LinkedList linkedList) {
        this.linkedList = linkedList;
    }

    /**
     * 尝试获取链表
     *
     * @return 链表 or null
     */
    public LinkedList getLinkedList(){
        return this.linkedList;
    }

    /**
     * 放入定时任务
     *
     * @param task 定时任务
     */
    public void setTask(Task task) {
        this.task = task;
    }

    /**
     * 尝试获取定时任务
     *
     * @return 定时任务 or null
     */
    public Task getTask() {
        return task;
    }

    /**
     * 获取下一个节点
     *
     * @return 下一个节点
     */
    public Node getNext(){
        return this.next;
    }

    /**
     * 获取上一个节点
     *
     * @return 上一个节点
     */
    public Node getLast() {
        return last;
    }

    /**
     * 设置上一个节点的引用指向
     *
     * @param last 上一个节点
     */
    public void setLast(Node last) {
        this.last = last;
    }

    /**
     * 设置下一个节点的引用指向
     *
     * @param next 下一个节点
     */
    public void setNext(Node next) {
        this.next = next;
    }

}
