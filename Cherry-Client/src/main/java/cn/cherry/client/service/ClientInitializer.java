package cn.cherry.client.service;

import cn.cherry.client.base.Receiver;
import cn.cherry.client.service.codec.ClientDecoder;
import cn.cherry.client.service.codec.ClientEncoder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;

/**
 * Netty Client 流水线初始化器
 *
 * @since 2022/10/19
 * @author realDragonKing
 */
public class ClientInitializer extends ChannelInitializer<SocketChannel> {

    private final Receiver receiver;

    public ClientInitializer(Receiver receiver) {
        this.receiver = receiver;
    }

    /**
     * This method will be called once the {@link Channel} was registered. After the method returns this instance
     * will be removed from the {@link ChannelPipeline} of the {@link Channel}.
     *
     * @param ch the {@link Channel} which was registered.
     */
    @Override
    protected void initChannel(SocketChannel ch) {
        ch.pipeline().addLast(new LineBasedFrameDecoder(4096));
        ch.pipeline().addLast(new ClientEncoder());
        ch.pipeline().addLast(new ClientDecoder());
        ch.pipeline().addLast(new ClientHandler(this.receiver));
    }

}
