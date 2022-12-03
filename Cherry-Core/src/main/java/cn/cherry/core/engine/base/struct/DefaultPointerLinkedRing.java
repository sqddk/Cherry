package cn.cherry.core.engine.base.struct;


import cn.cherry.core.engine.base.TaskList;

import java.util.List;
import java.util.Map;

public class DefaultPointerLinkedRing extends PointerLinkedRing<Map<Integer, TaskList>> {
    public DefaultPointerLinkedRing(List<Map<Integer, TaskList>> values) {
        super(values);
    }

    /**
     * 返回指定下标的节点
     *
     * @param position 下标
     */
    private Node<Map<Integer, TaskList>> nodeAt(int position) {
        Node<Map<Integer, TaskList>> node = getFirst();
        for (int i = 0; i < getSize(); i++) {
            node = node.getNext();
        }
        return node;
    }

    /**
     * 添加TaskList
     * 第一个Integer节点下标，第二个Integer为圈数
     */
    public void add(Map<Integer, Map<Integer, TaskList>> map) {
        Node<Map<Integer, TaskList>> node;
        for (int i : map.keySet()) {//i为节点
            node=nodeAt(i);
            Map<Integer, TaskList> newValue = map.get(i);
            Map<Integer, TaskList> nodeValue = node.getValue();
            for (int round:newValue.keySet()) {//round为圈数
                if(!nodeValue.containsKey(round))
                    nodeValue.put(round, newValue.get(round));
                else
                    nodeValue.get(round).add(newValue.get(round));
            }

        }
    }

    /**
     * 将时间轮中所有节点中圈数为0的TaskList删除
     */
    public void clear(){
        Node<Map<Integer, TaskList>> node=getFirst();
        for (int i = 0; i < getSize(); i++) {
            node.getValue().remove(0);
            node=node.getNext();
        }
    }
}
