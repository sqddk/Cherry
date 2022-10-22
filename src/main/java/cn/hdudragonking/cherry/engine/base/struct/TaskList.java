package cn.hdudragonking.cherry.engine.base.struct;

import cn.hdudragonking.cherry.engine.task.Task;

/**
 * ר��װ���������
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
     * ��ĩ�˲���һ���µĽڵ�
     *
     * @param task �ڵ�ֵ
     */
    @Override
    public void add(Task task) {
        task.setTaskID(registeredTaskSize);
        registeredTaskSize++;
        super.add(task);
    }

    /**
     * ��������ID�����Ҳ�ɾ����Ӧ������ɾ��
     *
     * @param taskID ����ID�������Խ�������ת����
     * @return �Ƿ�ɹ�ɾ��
     */
    public boolean removeTask(int taskID) {
        try {
            int maxPosition = this.size() - 1;
            if (maxPosition == -1) {
                return false;
            }
            this.resetHead();
            while (this.getPosition() < maxPosition) {
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
