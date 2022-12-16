package cn.cherry.core.engine.task;

import cn.cherry.core.engine.task.spec.SpecNode;

import java.util.LinkedList;
import java.util.List;

import static java.util.Objects.*;

/**
 * 保存了{@link Task}和相关{@link SpecNode}，{@link SpecNode}可以获取到绑定的{@link TaskKeeper}从而执行全体删除
 *
 * @author realDragonKing
 */
public class TaskKeeper {

    private Task task;
    private final List<SpecNode<?>> specNodeList;

    public TaskKeeper() {
        this.specNodeList = new LinkedList<>();
    }

    /**
     * @return 获取保存的任务
     */
    public Task getTask() {
        return task;
    }

    /**
     * 设置保存的{@link Task}任务
     *
     * @param task 任务
     */
    public void setTask(Task task) {
        this.task = task;
    }

    /**
     * 添加一个{@link SpecNode}任务特征节点
     *
     * @param specNode 任务特征节点
     */
    public void addSpecNode(SpecNode<?> specNode) {
        requireNonNull(specNode, "specNode");
        this.specNodeList.add(specNode);
    }

    /**
     * 移除全体{@link SpecNode}和{@link Task}
     */
    public void clear() {
        for (SpecNode<?> specNode : this.specNodeList) {
            specNode.removeSelf();
        }
        this.specNodeList.clear();
        this.task = null;
    }

}
