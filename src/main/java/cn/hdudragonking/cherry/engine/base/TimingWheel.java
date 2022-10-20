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

    /**
     * ���Ի�ȡ��ʱ���ֵĲ���������û����������������������һ��ʱ���򷵻�����ȡʧ�ܵ���Ϣ
     *
     * @param stopNeed �Ƿ�ʱֹͣ
     * @return �����Ƿ�ɹ���ȡ
     */
    boolean tryLock(boolean stopNeed);

    /**
     * �ͷ�ʱ���ֵĲ�����
     */
    void unLock();

    /**
     * ����ʱ��������ƣ����㳬ʱʱ�䣬�������ڵ�����ָ�ִ�У����Ұ��ճ�ʱʱ����������round
     *
     * @param wasteTicks ��ʧ�Ŀ̶���
     */
    void recover(int wasteTicks);

}
