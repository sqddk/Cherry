package cn.cherry.core.engine.base.task;

import cn.cherry.core.engine.base.TimingWheel;
import cn.cherry.core.engine.base.task.spec.Spec;
import cn.cherry.core.engine.base.task.spec.SpecSelector;
import cn.cherry.core.engine.base.task.spec.TaskIdSelector;

import java.util.Map;
import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

/**
 * {@link TaskGroup}任务管理集群的默认实现
 *
 * @author realDragonKing
 */
public class DefaultTaskGroup implements TaskGroup{

    private long count;
    private final TimingWheel timingWheel;
    private final SpecSelector<Long> taskIdSelector;

    public DefaultTaskGroup(TimingWheel timingWheel) {
        this.count = 0;
        this.timingWheel = timingWheel;
        this.taskIdSelector = new TaskIdSelector();
    }

    /**
     * 添加一个任务
     *
     * @param task 任务
     * @return 任务的id
     */
    @Override
    public long addTask(Task task) {
        long taskId = this.count++;
        long publishTimeValue = System.currentTimeMillis();

        Map<String, Object> taskConfig = task.getTaskConfig();
        taskConfig.put("taskId", taskId);
        taskConfig.put("publishTimeValue", publishTimeValue);

        TaskKeeper keeper = new TaskKeeper(); // 这里以后要把TaskKeeper做成可以池化复用的，clear方法已经铺好路了
        keeper.setTask(task);

        this.taskIdSelector.addSpecNode(taskId, keeper);

        return taskId;
    }

    /**
     * 对于{@link Spec}检索项，检索具体是某个值的{@link Task}，提供一个{@link Consumer}来遍历消费每一个{@link Task}
     *
     * @param spec     检索项
     * @param value    特征值
     * @param consumer 任务遍历消费者
     * @return 遍历消费了多少个任务
     */
    @Override
    public <E> int selectTask(Spec spec, E value, Consumer<Task> consumer) {
        return 0;
    }

    /**
     * 对于{@link Spec}检索项，检索具体是某个区间的{@link Task}，提供一个{@link Consumer}来遍历消费每一个{@link Task}
     *
     * @param spec       检索项
     * @param leftValue  特征值左区间
     * @param rightValue 特征值右区间
     * @param consumer   任务遍历消费者
     * @return 遍历消费了多少个任务
     */
    @Override
    public <E> int selectTask(Spec spec, E leftValue, E rightValue, Consumer<Task> consumer) {
        return 0;
    }

    /**
     * 对于{@link Spec}检索项，删除具体是某个值的{@link Task}
     *
     * @param spec  检索项
     * @param value 特征值
     * @return 删除了多少个任务
     */
    @Override
    public <E> int removeTask(Spec spec, E value) {
        requireNonNull(spec);
        if (spec == Spec.TaskId) {
            return this.taskIdSelector.selectSpecNode((Long) value, specNode -> {
                TaskKeeper keeper = specNode.getTaskKeeper();
                keeper.clear();
            });
        }
        return 0;
    }

    /**
     * 对于{@link Spec}检索项，删除具体是某个区间的{@link Task}
     *
     * @param spec       检索项
     * @param leftValue  特征值左区间
     * @param rightValue 特征值右区间
     * @return 删除了多少个任务
     */
    @Override
    public <E> int removeTask(Spec spec, E leftValue, E rightValue) {
        return 0;
    }

    /**
     * 消费所有的任务，然后把{@link TaskGroup}恢复到原始的状态
     */
    @Override
    public void executeAll() {
        TimingWheel timingWheel = this.timingWheel;
        this.taskIdSelector.selectAllSpecNode(specNode -> {
            TaskKeeper keeper = specNode.getTaskKeeper();
            timingWheel.executeTask(keeper.getTask());
        });
        this.taskIdSelector.clear();
    }

}
