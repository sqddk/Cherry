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
     * @return �ύ�Ƿ�ɹ� | ����ID
     */
    int[] submit(Task task);

    /**
     * ��������ID���Ƴ�һ����ʱ����
     *
     * @param timePoint ����ʱ���
     * @param id ����ID
     * @return �����Ƿ�ɾ���ɹ�
     */
    boolean remove(TimePoint timePoint, String id);

    /**
     * ʱ���ֽ���һ��ת��
     */
    void turn();

}
