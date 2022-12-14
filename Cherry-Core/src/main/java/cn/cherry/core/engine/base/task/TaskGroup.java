package cn.cherry.core.engine.base.task;

import cn.cherry.core.engine.base.task.spec.Spec;

import java.util.function.Consumer;

/**
 * 被定义为管理{@link Task}的任务集群，提供根据不同的{@link Spec}、快速检索符合条件的{@link Task}的方法，和添加、删除{@link Task}的方法<br/>
 * {@link TaskGroup}应当有一个基本容器和许多不同维度的检索器
 *
 * @author realDragonKing
 */
public interface TaskGroup {

    /**
     * 添加一个任务
     *
     * @param task 任务
     * @return 任务的id
     */
    long addTask(Task task);

    /**
     * 对于{@link Spec}检索项，检索具体是某个值的{@link Task}，提供一个{@link Consumer}来遍历消费每一个{@link Task}
     *
     * @param spec 检索项
     * @param value 特征值
     * @param consumer 任务遍历消费者
     * @return 遍历消费了多少个任务
     */
    <E> int selectTask(Spec spec, E value, Consumer<Task> consumer);

    /**
     * 对于{@link Spec}检索项，检索具体是某个区间的{@link Task}，提供一个{@link Consumer}来遍历消费每一个{@link Task}
     *
     * @param spec 检索项
     * @param leftValue 特征值左区间
     * @param rightValue 特征值右区间
     * @param consumer 任务遍历消费者
     * @return 遍历消费了多少个任务
     */
    <E> int selectTask(Spec spec, E leftValue, E rightValue, Consumer<Task> consumer);

    /**
     * 对于{@link Spec}检索项，删除具体是某个值的{@link Task}
     *
     * @param spec 检索项
     * @param value 特征值
     * @return 删除了多少个任务
     */
    <E> int removeTask(Spec spec, E value);

    /**
     * 对于{@link Spec}检索项，删除具体是某个区间的{@link Task}
     *
     * @param spec 检索项
     * @param leftValue 特征值左区间
     * @param rightValue 特征值右区间
     * @return 删除了多少个任务
     */
    <E> int removeTask(Spec spec, E leftValue, E rightValue);

    /**
     * 消费所有的任务，然后把{@link TaskGroup}恢复到原始的状态
     */
    void executeAll();

}
