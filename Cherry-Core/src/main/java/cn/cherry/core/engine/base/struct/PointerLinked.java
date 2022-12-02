package cn.cherry.core.engine.base.struct;

/**
 * {@link PointerLinked}表示一种带有指针的线性数据结构，由双指向的节点{@link Node}连结而成，拥有一个pointer指针来指向其中的一个节点。
 * 应当使用{@link #moveNext()}和{@link #movePrev()}来控制pointer指针的移动
 *
 * @author realDragonKing
 */
public interface PointerLinked<E> {

    /**
     * @return pointer指针所指的节点的值
     */
    E getValue();

    /**
     * 移动指针到下一个节点
     */
    void moveNext();

    /**
     * 移动指针到上一个节点
     */
    void movePrev();

    /**
     * @return 大小（有多少个{@link Node}节点加入了{@link PointerLinked}）
     */
    int getSize();

}
