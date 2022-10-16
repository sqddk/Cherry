package cn.hdudragonking.cherry.base;

import cn.hdudragonking.cherry.task.Task;

/**
 * ��������Ľڵ�
 * @author realDragonKing
 */
public class Node {

    private Node next;
    private Node previous;
    private LinkedList linkedList;
    private Task task;
    public int id;

    public Node(){}

    /**
     * ��������
     *
     * @param linkedList ����
     */
    public void setLinkedList(LinkedList linkedList) {
        this.linkedList = linkedList;
    }

    /**
     * ���Ի�ȡ����
     *
     * @return ���� or null
     */
    public LinkedList getLinkedList(){
        return this.linkedList;
    }

    /**
     * ���붨ʱ����
     *
     * @param task ��ʱ����
     */
    public void setTask(Task task) {
        this.task = task;
    }

    /**
     * ���Ի�ȡ��ʱ����
     *
     * @return ��ʱ���� or null
     */
    public Task getTask() {
        return task;
    }

    /**
     * ��ȡ��һ���ڵ�
     *
     * @return ��һ���ڵ�
     */
    public Node getNext(){
        return this.next;
    }

    /**
     * ��ȡ��һ���ڵ�
     *
     * @return ��һ���ڵ�
     */
    public Node getPrevious() {
        return previous;
    }

    /**
     * ������һ���ڵ������ָ��
     *
     * @param previous ��һ���ڵ�
     */
    public void setPrevious(Node previous) {
        this.previous = previous;
    }

    /**
     * ������һ���ڵ������ָ��
     *
     * @param next ��һ���ڵ�
     */
    public void setNext(Node next) {
        this.next = next;
    }

}
