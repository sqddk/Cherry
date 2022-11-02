package cn.hdudragonking.cherry.service.remote.client;

import cn.hdudragonking.cherry.service.remote.client.codec.CherryClientDecoder;
import cn.hdudragonking.cherry.service.remote.client.codec.CherryClientEncoder;
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

    private final ClientReceiver clientReceiver;

    public ClientInitializer(ClientReceiver clientReceiver) {
        this.clientReceiver = clientReceiver;
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
        ch.pipeline().addLast(new ClientHandler(this.clientReceiver));
    }

}
