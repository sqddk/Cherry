package cn.cherry.core.message;

/**
 * 通信消息的类型标志，静态字段集中地
 *
 * @author realDragonKing
 */
public class MessageType {

    private MessageType() {}

    /**
     * 来自客户端的心跳
     */
    public static final int PING = 1;

    /**
     * 来自服务端的心跳
     */
    public static final int PONG = 2;

    /**
     * 提交定时任务
     */
    public static final int ADD = 3;

    /**
     * 取消定时任务
     */
    public static final int REMOVE = 4;

    /**
     * 通知执行定时任务
     */
    public static final int NOTIFY = 5;

    /**
     * 汇报错误
     */
    public static final int ERROR = 6;

    /**
     * 执行提交操作的结果
     */
    public static final int ADD_RESULT = 7;

    /**
     * 执行删除操作的结果
     */
    public static final int REMOVE_RESULT = 8;

    /**
     * 进行通信信道注册
     */
    public static final int REGISTER = 9;

}
