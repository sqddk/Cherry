package cn.cherry.core.engine.base.struct;


/**
 * 指针链表的顶层抽象类
 *
 * @since 2022/10/17
 * @author realDragonKing
 */
public abstract class PointerLinkedList<E> {

    private int size;
    private int position;

    public PointerLinkedList() {
        this.size = 0;
        this.position = -1;
    }

    /**
     * 在末端插入一个新的节点
     *
     * @param item 节点值
     */
    public abstract void add(E item);

    /**
     * 删除并弹出指针处的节点值
     */
    public abstract void remove();

    /**
     * 移动指针到下一个节点
     */
    public abstract void moveNext();

    /**
     * 移动指针到上一个节点
     */
    public abstract void movePrevious();

    /**
     * 重置指针到头节点
     */
    public abstract void resetHead();

    /**
     * 重置指针到尾节点
     */
    public abstract void resetTail();

    /**
     * 获取指针所指的节点值
     *
     * @return 节点值
     */
    public abstract E getPointer();

    /**
     * 获取指针所指节点的位置索引
     *
     * @return 位置
     */
    public int position() {
        return this.position;
    }

    /**
     * 指针所指节点的位置索引自增
     */
    protected void positionAdd() {
        this.position++;
    }

    /**
     * 指针所指节点的位置索引自减
     */
    protected void positionDec() {
        this.position--;
    }

    /**
     * 移动指针位置索引到链表头部
     */
    protected void setPositionAtHead() {
        this.position = 0;
    }

    /**
     * 移动指针位置索引到链表尾部
     */
    protected void setPositionAtTail() {
        this.position = this.size - 1;
    }

    /**
     * 获取链表的大小
     *
     * @return 链表的大小
     */
    public int size() {
        return this.size;
    }

    /**
     * 链表大小自增
     */
    protected void sizeAdd() {
        this.size++;
    }

    /**
     * 链表大小自减
     */
    protected void sizeDec() {
        this.size--;
    }

}
