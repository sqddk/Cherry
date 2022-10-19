package cn.hdudragonking.cherry.bootstrap.remote.client;

import cn.hdudragonking.cherry.engine.base.TimePoint;

/**
 * ��ӦԶ�̵�cherry��ʱ����������������ִ��֪ͨ��ִ�о��������
 *
 * @since 2022/10/19
 * @author realDragonKing
 */
public interface Receiver {

    /**
     * ���ն�ʱ����ִ��֪ͨ
     *
     * @param timePoint ʱ���
     * @param metaData Ԫ����
     * @param uniqueID ����ΨһID
     */
    void receiveNotify(TimePoint timePoint, String metaData, String uniqueID);

    /**
     * ���մ�����Ϣ
     *
     * @param errorMessage ������Ϣ
     */
    void receiveError(String errorMessage);

}
