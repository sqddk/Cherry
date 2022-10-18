package cn.hdudragonking.cherry.bootstrap.remote.protocol;

public class CherryProtocolFlag {

    private CherryProtocolFlag() {}

    /**
     * ���Կͻ��˵�����֡
     */
    public static final int FLAG_PING = 0;

    /**
     * ���Է���˵�����֡
     */
    public static final int FLAG_PONG = 1;

    /**
     * �ύ��ʱ����Ŀ���֡
     */
    public static final int FLAG_ADD = 2;

    /**
     * ȡ����ʱ����Ŀ���֡
     */
    public static final int FLAG_REMOVE = 3;

    /**
     * ִ֪ͨ�ж�ʱ����Ŀ���֡
     */
    public static final int FLAG_NOTIFY = 4;

    /**
     * �㱨����Ŀ���֡
     */
    public static final int FLAG_ERROR = 5;

}
