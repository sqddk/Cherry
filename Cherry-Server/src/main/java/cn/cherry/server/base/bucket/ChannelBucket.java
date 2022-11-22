package cn.cherry.server.base.bucket;

import io.netty.channel.Channel;


/**
 * 通信信道容器，采用服务组的方式进行管理，一个服务组名称对应一组通信信道。
 * <p>
 * 提供了{@link #getRandomChannel(String groupName)} => 获取到服务组内随机一个通信信道，
 * 以及{@link #getAllChannel(String groupName)} => 获取到服务组内的所有通信信道
 *
 * @since 2022/11/13
 * @author realDragonKing
 */
public interface ChannelBucket {

    ChannelBucket INSTANCE = new DefaultChannelBucket();

    /**
     * 根据服务组名称获取到其中一个通信信道
     *
     * @param groupName 服务组名称
     * @return 随机一个通信信道
     */
    Channel getRandomChannel(String groupName);

    /**
     * 根据服务组名称获取到所有的通信信道
     *
     * @param groupName 服务组名称
     * @return 所有的通信信道
     */
    Channel[] getAllChannel(String groupName);

    /**
     * 根据服务组名称，往对应服务组添加一个通信信道
     *
     * @param groupName 服务组名称
     * @param channel 通信信道
     */
    void addChannel(String groupName, Channel channel);

}
