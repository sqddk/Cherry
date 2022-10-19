package cn.hdudragonking.cherry.bootstrap.remote.server;

import cn.hdudragonking.cherry.bootstrap.remote.server.codec.CherryServerDecoder;
import cn.hdudragonking.cherry.bootstrap.remote.server.codec.CherryServerEncoder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;

/**
 * Netty Server 流水线初始化类
 *
 * @since 2022/10/19
 * @author realDragonKing
 */
public class ServerInitializer extends ChannelInitializer<SocketChannel> {

    /**
     * This method will be called once the {@link Channel} was registered. After the method returns this instance
     * will be removed from the {@link ChannelPipeline} of the {@link Channel}.
     *
     * @param ch the {@link Channel} which was registered.
     */
    @Override
    protected void initChannel(SocketChannel ch) {
        ch.pipeline().addLast(new LineBasedFrameDecoder(4096));
        ch.pipeline().addLast(new CherryServerEncoder());
        ch.pipeline().addLast(new CherryServerDecoder());
        ch.pipeline().addLast(new ServerHandler());
    }
}
