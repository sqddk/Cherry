package cn.hdudragonking.cherry.bootstrap.remote;

import cn.hdudragonking.cherry.bootstrap.remote.codec.CherryServerDecoder;
import cn.hdudragonking.cherry.bootstrap.remote.codec.CherryServerEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;

public class CherrySocketInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) {
        ch.pipeline().addLast(new LineBasedFrameDecoder(4096));
        ch.pipeline().addLast(new CherryServerEncoder());
        ch.pipeline().addLast(new CherryServerDecoder());
        ch.pipeline().addLast(new CherryServerHandler());
    }

}
