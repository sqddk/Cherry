package cn.hdudragonking.cherry.engine.base.struct;


/**
 * ָ������Ľӿ�
 *
 * @since 2022/10/17
 * @author realDragonKing
 */
public interface PointerLinkedList<E> {

    /**
     * ��ĩ�˲���һ���µĽڵ�
     *
     * @param item �ڵ�ֵ
     */
    void add(E item);

    /**
     * ɾ��������ָ�봦�Ľڵ�ֵ
     *
     * @return �����Ľڵ�ֵ
     */
    E remove();

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
     * ��ȡָ����ָ�Ľڵ�ֵ
     *
     * @return �ڵ�ֵ
     */
    E getPointer();

    /**
     * ��ȡָ����ָ�ڵ��λ��
     *
     * @return λ��
     */
    int getPosition();

    /**
     * ��ȡ����Ĵ�С
     *
     * @return ����Ĵ�С
     */
    int size();

}
