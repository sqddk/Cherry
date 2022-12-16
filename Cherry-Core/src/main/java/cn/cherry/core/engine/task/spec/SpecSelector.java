package cn.cherry.core.engine.task.spec;

import cn.cherry.core.engine.task.Task;
import cn.cherry.core.engine.task.TaskKeeper;

import java.util.function.Consumer;

/**
 * 特征检索者，有着自己独特且高效的数据结构来保存{@link SpecNode}，可以检索特征为给定int值或是给定int区间范围的
 * {@link SpecNode}进而获取到其它{@link SpecNode}和{@link Task}
 *
 * @param <E> 任务特征值
 * @author realDragonKing
 */
public interface SpecSelector<E> {

    /**
     * 如果 o1 > o2，返回 1<br/>
     * 如果 o1 = o2，返回 0<br/>
     * 如果 o1 < o2，返回 -1<br/>
     *
     * @param o1 比较者
     * @param o2 被比较者
     * @return 比较结果
     */
    int compare(E o1, E o2);

    /**
     * 添加一个任务特征节点
     *
     * @param specValue 任务特征值
     * @param keeper 任务保存者
     */
    void addSpecNode(E specValue, TaskKeeper keeper);

    /**
     * 检索具体是某个值的{@link SpecNode}，提供一个{@link Consumer}来遍历消费每一个{@link SpecNode}
     *
     * @param value 特征值
     * @param consumer 任务遍历消费者
     * @return 遍历消费了多少个任务
     */
    int selectSpecNode(E value, Consumer<SpecNode<E>> consumer);

    /**
     * 检索具体是某个值的{@link SpecNode}，提供一个{@link Consumer}来遍历消费每一个{@link SpecNode}
     *
     * @param leftValue 特征值左区间
     * @param rightValue 特征值右区间
     * @param consumer 任务遍历消费者
     * @return 遍历消费了多少个任务
     */
    int selectSpecNode(E leftValue, E rightValue, Consumer<SpecNode<E>> consumer);

    /**
     * 消费所有的{@link SpecNode}
     *
     * @param consumer 任务遍历消费者
     */
    void selectAllSpecNode(Consumer<SpecNode<E>> consumer);

    /**
     * 清空自己的所有内容，还原到原始状态
     */
    void clear();

}
