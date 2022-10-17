package cn.hdudragonking.cherry.scheduler;

import cn.hdudragonking.cherry.task.Task;

/**
 * ��ʱ�����ߵĽӿ�
 * @author realDragonKing
 */
public interface Scheduler {

    /**
     * �ύһ���µĶ�ʱ����
     *
     * @param task ��ʱ����
     */
    void submit(Task task);

    /**
     * ʱ���ֽ���һ��ת��
     */
    void turn();

}
