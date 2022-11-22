package cn.cherry.core;

public class ProtocolFlag {

    private ProtocolFlag() {}

    /**
     * 来自客户端的心跳
     */
    public static final int FLAG_PING = 1;

    /**
     * 来自服务端的心跳
     */
    public static final int FLAG_PONG = 2;

    /**
     * 提交定时任务
     */
    public static final int FLAG_ADD = 3;

    /**
     * 取消定时任务
     */
    public static final int FLAG_REMOVE = 4;

    /**
     * 通知执行定时任务
     */
    public static final int FLAG_NOTIFY = 5;

    /**
     * 汇报错误
     */
    public static final int FLAG_ERROR = 6;

    /**
     * 执行提交操作的结果
     */
    public static final int FLAG_RESULT_ADD = 7;

    /**
     * 执行删除操作的结果
     */
    public static final int FLAG_RESULT_REMOVE = 8;

    /**
     * 进行通信信道注册
     */
    public static final int FLAG_REGISTER = 9;

}
