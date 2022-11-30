package cn.cherry.core.engine.base.struct;

/**
 * 这个接口表示一种带有指针的线性数据结构，由双指向的节点{@link Node}连结而成，拥有一个pointer指针来指向其中的一个节点
 *
 * @author realDragonKing
 */
public interface PointerLinked<E> {

    /**
     * @return 指针所指的节点值
     */
    E getPoint();

    /**
     * @return 指针所指的位置索引
     */
    int getPosition();

    /**
     * 移动指针到下一个节点
     */
    void moveNext();

    /**
     * 移动指针到上一个节点
     */
    void movePrevious();

}
