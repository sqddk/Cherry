package cn.cherry.core.engine.task;

import cn.cherry.core.engine.task.spec.SpecNode;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.*;

/**
 * 保存了{@link Task}和相关{@link SpecNode}，{@link SpecNode}可以获取到绑定的{@link TaskKeeper}从而执行全体删除
 *
 * @author realDragonKing
 */
public class TaskKeeper {

    private final Task task;
    private final List<SpecNode<?>> specNodeList;

    public TaskKeeper(Task task) {
        requireNonNull(task, "Task");
        this.task = task;
        this.specNodeList = new ArrayList<>();
    }

    /**
     * @return 持有的任务
     */
    public Task getTask() {
        return this.task;
    }

    /**
     * 添加一个{@link SpecNode}任务特征节点
     *
     * @param specNode 任务特征节点
     */
    public void addSpecNode(SpecNode<?> specNode) {
        requireNonNull(specNode, "SpecNode");
        this.specNodeList.add(specNode);
    }

    /**
     * 移除全体{@link SpecNode}和{@link Task}
     */
    public void remove() {
        for (SpecNode<?> specNode : this.specNodeList) {
            specNode.removeSelf();
        }
    }

}
