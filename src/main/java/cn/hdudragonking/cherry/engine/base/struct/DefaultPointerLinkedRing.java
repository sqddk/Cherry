package cn.hdudragonking.cherry.engine.base.struct;

import cn.hdudragonking.cherry.engine.task.Task;

import java.util.HashMap;
import java.util.Map;

/**
 * Ĭ�ϵ�ʱ���ֵ����ݽṹʵ�֣����������̳���ָ�������Ĭ��ʵ���ࣩ
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
     * �ƶ�ָ�뵽��һ���ڵ㣬��ָ���Ѿ�����β�ڵ�ʱ����ص�ͷ�ڵ�
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
     * �ƶ�ָ�뵽��һ���ڵ㣬��ָ���Ѿ�����ͷ�ڵ�ʱ����ص�β�ڵ�
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
