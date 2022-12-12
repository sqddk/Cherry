package cn.cherry.core.engine.base.spec;

import cn.cherry.core.infra.Task;

import java.util.function.Consumer;

/**
 * 被定义为管理{@link Task}的任务集群，提供根据不同的{@link SpecType}、
 * 快速检索符合条件的{@link Task}的方法，和添加、删除{@link Task}的方法
 *
 * @author realDragonKing
 */
public interface TaskGroup {

    /**
     * 添加一个任务
     *
     * @param task 任务
     */
    void addTask(Task task);

    /**
     * 对于{@link SpecType}检索项，检索具体是某个值的{@link Task}，
     * 提供一个{@link Consumer}来遍历消费每一个{@link Task}
     *
     * @param type 检索项
     * @param value 检索值
     * @param consumer 任务遍历消费者
     * @return 遍历消费了多少个任务
     */
    int selectTask(SpecType type, int value, Consumer<SpecNodeLink> consumer);

    /**
     * 对于{@link SpecType}检索项，检索具体是某个区间的{@link Task}，
     * 提供一个{@link Consumer}来遍历消费每一个{@link Task}
     *
     * @param type 检索项
     * @param leftValue 左区间
     * @param rightValue 右区间
     * @param consumer 任务遍历消费者
     * @return 遍历消费了多少个任务
     */
    int selectTask(SpecType type, int leftValue, int rightValue, Consumer<SpecNodeLink> consumer);

    /**
     * 消费所有的任务，然后把{@link TaskGroup}恢复到原始的状态
     */
    void executeAll();

}
