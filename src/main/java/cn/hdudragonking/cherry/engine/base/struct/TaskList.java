package cn.hdudragonking.cherry.engine.base.struct;

import cn.hdudragonking.cherry.engine.task.Task;


/**
 * 专门装任务的链表
 *
 * @since 2022/10/19
 * @author realDragonKing
 */
public class TaskList extends DefaultPointerLinkedList<Task> {

    private int registeredTaskSize;

    public TaskList() {
        this.registeredTaskSize = 0;
    }

    /**
     * 在末端插入一个新的节点
     *
     * @param task 节点值
     */
    @Override
    public void add(Task task) {
        task.setTaskID(registeredTaskSize);
        registeredTaskSize++;
        super.add(task);
    }

    /**
     * 根据任务ID，查找并删除相应的任务删除
     *
     * @param taskID 任务ID（将尝试进行数字转换）
     * @return 是否成功删除
     */
    public boolean removeTask(int taskID) {
        try {
            int maxPosition = this.size() - 1;
            if (maxPosition == -1) {
                return false;
            }
            this.resetHead();
            while (this.position() < maxPosition) {
                if (this.getPointer().getTaskID() == taskID) {
                    this.remove();
                    return true;
                }
                this.moveNext();
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

}
