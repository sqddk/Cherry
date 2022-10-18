package cn.hdudragonking.cherry.bootstrap.remote.protocol;

public class CherryProtocolFlag {

    private CherryProtocolFlag() {}

    /**
     * 来自客户端的心跳帧
     */
    public static final int FLAG_PING = 0;

    /**
     * 来自服务端的心跳帧
     */
    public static final int FLAG_PONG = 1;

    /**
     * 提交定时任务的控制帧
     */
    public static final int FLAG_ADD = 2;

    /**
     * 取消定时任务的控制帧
     */
    public static final int FLAG_REMOVE = 3;

    /**
     * 通知执行定时任务的控制帧
     */
    public static final int FLAG_NOTIFY = 4;

    /**
     * 汇报错误的控制帧
     */
    public static final int FLAG_ERROR = 5;

}
