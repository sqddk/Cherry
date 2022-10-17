package cn.hdudragonking.cherry.engine.base;

import cn.hdudragonking.cherry.engine.task.Task;

/**
 * ʱ���ֵĽӿ�
 *
 * @since 2022/10/17
 * @author realDragonKing
 */
public interface TimingWheel {

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
