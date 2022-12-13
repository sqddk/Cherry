package cn.cherry.core.engine.base.task.spec;

import cn.cherry.core.engine.base.task.TaskKeeper;
import cn.cherry.core.engine.base.task.Task;

import static java.util.Objects.*;
import java.util.function.Consumer;

/**
 * 将{@link Task}的{@link Long}类型字段"taskId"作为特征<br/>
 * 这里先用链表实现一套
 *
 * @author realDragonKing
 */
public class TaskIdSelector implements SpecSelector<Long> {

    private final TaskIdNode head;
    private final TaskIdNode tail;
    private int size;

    public TaskIdSelector() {
        TaskIdNode head = new TaskIdNode(null, null, this);
        TaskIdNode tail = new TaskIdNode(null, null, this);
        head.next = tail;
        tail.prev = head;

        this.head = head;
        this.tail = tail;
        this.size = 0;
    }

    /**
     * 如果 o1 > o2，返回 1<br/>
     * 如果 o1 = o2，返回 0<br/>
     * 如果 o1 < o2，返回 -1<br/>
     *
     * @param o1 比较者
     * @param o2 被比较者
     * @return 比较结果
     */
    @Override
    public int compare(Long o1, Long o2) {
        return Long.compare(o1, o2);
    }

    /**
     * 添加一个任务特征节点
     *
     * @param value 任务特征值
     * @param keeper 任务保存者
     */
    @Override
    public void addSpecNode(Long value, TaskKeeper keeper) {
        requireNonNull(value, "value:TaskId");
        requireNonNull(keeper, "keeper");

        TaskIdNode node = new TaskIdNode(value, keeper, this);
        TaskIdNode prev = this.head;
        TaskIdNode next;

        for (int i = 0; i < this.size; i++) {
            prev = prev.next;
            if (compare(value, prev.getSpecValue()) > -1)
                break;
        }
        next = prev.next;

        next.prev = node;
        node.next = next;

        prev.next = node;
        node.prev = prev;

        this.size += 1;
    }

    /**
     * 检索具体是某个值的{@link SpecNode}，提供一个{@link Consumer}来遍历消费每一个{@link SpecNode}
     *
     * @param value    特征值
     * @param consumer 任务遍历消费者
     * @return 遍历消费了多少个任务
     */
    @Override
    public int selectSpecNode(Long value, Consumer<SpecNode<Long>> consumer) {
        requireNonNull(value, "value:TaskId");
        requireNonNull(consumer, "consumer");

        TaskIdNode node = this.head;

        for (int i = 0; i < this.size; i++) {
            node = node.next;
            if (compare(value, node.getSpecValue()) == 0) {
                int sum = 0;
                while (node != this.tail && compare(value, node.getSpecValue()) == 0) {
                    node = node.next;
                    sum += 1;
                    consumer.accept(node);
                }
                return sum;
            }
        }
        return 0;
    }

    /**
     * 检索具体是某个值的{@link SpecNode}，提供一个{@link Consumer}来遍历消费每一个{@link SpecNode}
     *
     * @param leftValue  特征值左区间
     * @param rightValue 特征值右区间
     * @param consumer   任务遍历消费者
     * @return 遍历消费了多少个任务
     */
    @Override
    public int selectSpecNode(Long leftValue, Long rightValue, Consumer<SpecNode<Long>> consumer) {
        requireNonNull(leftValue, "leftValue:TaskId");
        requireNonNull(rightValue, "rightValue:TaskId");

        int res = compare(leftValue, rightValue);
        if (res == 0)
            return this.selectSpecNode(leftValue, consumer);
        if (res > 1)
            throw new IllegalArgumentException("leftValue 大于 rightValue");

        TaskIdNode node = this.head;

        for (int i = 0; i < this.size; i++) {
            node = node.next;
            if (compare(leftValue, node.getSpecValue()) == 0) {
                int sum = 0;
                while (node != this.tail && compare(rightValue, node.getSpecValue()) < 0) {
                    node = node.next;
                    sum += 1;
                    consumer.accept(node);
                }
                return sum;
            }
        }
        return 0;
    }

    /**
     * 移除一个{@link SpecNode}，如果在存储结构中没有搜索到这个{@link SpecNode}（我们采用比较内存地址的方法），那么返回错误结果
     *
     * @param specNode 任务特征节点
     * @return 是否在存储结构中成功找到这个节点
     */
    @Override
    public boolean removeSpecNode(SpecNode<Long> specNode) {
        requireNonNull(specNode, "specNode");

        TaskIdNode node = this.head;

        for (int i = 0; i < this.size; i++) {
            node = node.next;
            if (node == specNode) {
                TaskIdNode prev = node.prev;
                TaskIdNode next = node.next;
                prev.next = next;
                next.prev = prev;
                this.size -= 1;
                return true;
            }
        }
        return false;
    }

    /**
     * 清空自己的所有内容，还原到原始状态
     */
    @Override
    public void clear() {
        this.head.next = this.tail;
        this.tail.prev = this.head;
        this.size = 0;
    }

    private static class TaskIdNode extends SpecNode<Long> {

        private TaskIdNode prev;
        private TaskIdNode next;

        public TaskIdNode(Long spec, TaskKeeper keeper, SpecSelector<Long> specSelector) {
            super(spec, keeper, specSelector);
        }

    }

}
