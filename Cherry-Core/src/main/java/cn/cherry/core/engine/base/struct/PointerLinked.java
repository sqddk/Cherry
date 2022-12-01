package cn.cherry.core.engine.base.struct;

/**
 * {@link PointerLinked}表示一种带有指针的线性数据结构，
 * 由双指向的节点{@link Node}连结而成，拥有一个pointer指针来指向其中的一个节点
 *
 * @author realDragonKing
 */
public interface PointerLinked<E> {

    /**
     * @return 指针所指的节点值
     */
    E getPoint();

    /**
     * 移动指针到下一个节点
     */
    void moveNext();

    /**
     * 移动指针到上一个节点
     */
    void movePrevious();

    /**
     * @return 大小（有多少个{@link Node}节点加入了{@link PointerLinked}）
     */
    int getSize();

}
