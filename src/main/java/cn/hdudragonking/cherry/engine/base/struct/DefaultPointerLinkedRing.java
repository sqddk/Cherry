package cn.hdudragonking.cherry.engine.base.struct;

import cn.hdudragonking.cherry.engine.task.Task;

import java.util.HashMap;
import java.util.Map;

/**
 * 默认的时间轮的数据结构实现，环形链表（继承于指针链表的默认实现类）
 *
 * @since 2022/10/17
 * @author realDragonKing
 */
public class DefaultPointerLinkedRing extends DefaultPointerLinkedList<Map<Integer, PointerLinkedList<Task>>> {

    public DefaultPointerLinkedRing(int totalTicks) {
        super();
        for (int i = 0; i < totalTicks; i++) {
            this.add(new HashMap<>());
        }
    }

    /**
     * 移动指针到下一个节点，当指针已经处于尾节点时，会回到头节点
     */
    @Override
    public void moveNext() {
        if (this.getPosition() == this.size() - 1) {
            this.resetHead();
        } else {
            super.moveNext();
        }
    }

    /**
     * 移动指针到上一个节点，当指针已经处于头节点时，会回到尾节点
     */
    @Override
    public void movePrevious() {
        if (this.getPosition() == 0) {
            this.resetTail();
        } else {
            super.movePrevious();
        }
    }

}
