package cn.hdudragonking.cherry.bootstrap.remote.protocol;

public class CherryProtocolFlag {

    private CherryProtocolFlag() {}

    /**
     * ���Կͻ��˵�����
     */
    public static final int FLAG_PING = 0;

    /**
     * ���Է���˵�����
     */
    public static final int FLAG_PONG = 1;

    /**
     * �ύ��ʱ����
     */
    public static final int FLAG_ADD = 2;

    /**
     * ȡ����ʱ����
     */
    public static final int FLAG_REMOVE = 3;

    /**
     * ִ֪ͨ�ж�ʱ����
     */
    public static final int FLAG_NOTIFY = 4;

    /**
     * �㱨����
     */
    public static final int FLAG_ERROR = 5;

    /**
     * ִ���ύ�����Ľ��
     */
    public static final int FLAG_RESULT_ADD = 9;

    /**
     * ִ��ɾ�������Ľ��
     */
    public static final int FLAG_RESULT_REMOVE = 10;

}
