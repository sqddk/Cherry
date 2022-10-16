package cn.hdudragonking.cherry.base;

/**
 * ˫������Ľӿ�
 * @author realDragonKing
 */
public interface LinkedList {

    /**
     * ��ĩ�˲���һ���µĽڵ�
     *
     * @param node �ڵ�
     */
    void add(Node node);

    /**
     * ��ָ�봦����һ���µĽڵ�
     *
     * @param node �ڵ�
     */
    void insert(Node node);

    /**
     * ɾ��������ָ�봦�Ľڵ�
     *
     * @return �����Ľڵ�
     */
    Node remove();

    /**
     * �ƶ�ָ�뵽��һ���ڵ�
     */
    void moveNext();

    /**
     * �ƶ�ָ�뵽��һ���ڵ�
     */
    void movePrevious();

    /**
     * ����ָ�뵽ͷ�ڵ�
     */
    void resetHead();

    /**
     * ����ָ�뵽β�ڵ�
     */
    void resetTail();

    /**
     * ��ȡָ����ָ�Ľڵ�
     *
     * @return �ڵ�
     */
    Node getPointer();

    /**
     * ��ȡָ����ָ�ڵ��λ��
     *
     * @return λ��
     */
    int getPointerPosition();

    /**
     * ��ȡ����Ĵ�С
     *
     * @return ����Ĵ�С
     */
    int size();

}
