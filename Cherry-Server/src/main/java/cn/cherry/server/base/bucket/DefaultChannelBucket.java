package cn.cherry.server.base.bucket;

import io.netty.channel.Channel;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 通信信道容器的默认实现类，使用{@link ConcurrentHashMap}和{@link PointerLinkedList}存储通信信道，使用轮询访问机制获取随机一个通信信道
 *
 * @since 2022/11/13
 * @author realDragonKing
 */
public class DefaultChannelBucket implements ChannelBucket {

    public DefaultChannelBucket() {}

    /**
     * 根据服务组名称获取到其中一个通信信道
     *
     * @param groupName 服务组名称
     * @return 通信信道
     */
    @Override
    public Channel getRandomChannel(String groupName) {
        return null;
    }

    /**
     * 根据服务组名称获取到所有的通信信道
     *
     * @param groupName 服务组名称
     * @return 所有的通信信道
     */
    @Override
    public Channel[] getAllChannel(String groupName) {
        return new Channel[0];
    }

    /**
     * 根据服务组名称，往对应服务组添加一个通信信道
     *
     * @param groupName 服务组名称
     * @param channel   通信信道
     */
    @Override
    public void addChannel(String groupName, Channel channel) {

    }


}
