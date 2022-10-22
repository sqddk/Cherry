package cn.hdudragonking.cherry.bootstrap.remote.protocol;

public class CherryProtocolFlag {

    private CherryProtocolFlag() {}

    /**
     * ���Կͻ��˵�����
     */
    public static final int FLAG_PING = 1;

    /**
     * ���Է���˵�����
     */
    public static final int FLAG_PONG = 2;

    /**
     * �ύ��ʱ����
     */
    public static final int FLAG_ADD = 3;

    /**
     * ȡ����ʱ����
     */
    public static final int FLAG_REMOVE = 4;

    /**
     * ִ֪ͨ�ж�ʱ����
     */
    public static final int FLAG_NOTIFY = 5;

    /**
     * �㱨����
     */
    public static final int FLAG_ERROR = 6;

    /**
     * ִ���ύ�����Ľ��
     */
    public static final int FLAG_RESULT_ADD = 7;

    /**
     * ִ��ɾ�������Ľ��
     */
    public static final int FLAG_RESULT_REMOVE = 8;

    /**
     * ����ͨ���ŵ�ע��
     */
    public static final int FLAG_REGISTER = 9;

}
