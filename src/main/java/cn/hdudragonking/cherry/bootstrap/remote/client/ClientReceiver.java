package cn.hdudragonking.cherry.bootstrap.remote.client;

import cn.hdudragonking.cherry.engine.base.TimePoint;

/**
 * ��ӦԶ�̵�cherry��ʱ����������������ִ��֪ͨ��ִ�о��������
 *
 * @since 2022/10/19
 * @author realDragonKing
 */
public interface ClientReceiver {

    /**
     * ���ն�ʱ����ִ��֪ͨ
     *
     * @param timePoint ʱ���
     * @param metaData Ԫ����
     * @param taskID ����ID
     */
    void receiveNotify(TimePoint timePoint, String metaData, String taskID);

    /**
     * ���մ�����Ϣ
     *
     * @param errorMessage ������Ϣ
     */
    void receiveError(String errorMessage);

    /**
     * ���ն�ʱ�����ύ���
     *
     * @param metaData Ԫ����
     * @param taskID ����ID
     * @param result ���
     */
    void receiveAddResult(String metaData, String taskID, boolean result);

    /**
     * ���ն�ʱ����ɾ�����
     *
     * @param taskID ����ID
     * @param result ���
     */
    void receiveRemoveResult(String taskID, boolean result);

}
