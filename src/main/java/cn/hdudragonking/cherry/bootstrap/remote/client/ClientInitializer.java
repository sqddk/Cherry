package cn.hdudragonking.cherry.bootstrap.remote.client;

import cn.hdudragonking.cherry.bootstrap.remote.client.codec.CherryClientDecoder;
import cn.hdudragonking.cherry.bootstrap.remote.client.codec.CherryClientEncoder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;

/**
 * Netty Client 流水线初始化类
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
        ch.pipeline().addLast(new CherryClientEncoder());
        ch.pipeline().addLast(new CherryClientDecoder());
        ch.pipeline().addLast(new ClientHandler(this.receiver));
    }

}
