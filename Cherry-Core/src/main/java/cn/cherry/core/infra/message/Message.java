package cn.cherry.core.infra.message;

/**
 * 消息{@link Message}作为cherry的客户端/服务端、服务端/服务端间的通信数据载体，是一项必不可少的基础设施，客户端和服务端都能有自己的实现
 *
 * @author realDragonKing
 */
public interface Message {

    /**
     * 获取到{@link Message}的类型标志位
     *
     * @return 类型标志位
     */
    int getFlag();

}
