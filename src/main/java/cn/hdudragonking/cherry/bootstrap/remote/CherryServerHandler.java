package cn.hdudragonking.cherry.bootstrap.remote;

import cn.hdudragonking.cherry.bootstrap.remote.protocol.CherryProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.logging.Logger;

/**
 * cherry网络通信层面的终端处理器，
 * 用来处理网络定时任务提交和回执提醒通信
 *
 * @since 2022/10/18
 * @author realDragonKing
 */
public class CherryServerHandler extends SimpleChannelInboundHandler<CherryProtocol> {

    private final Logger log = Logger.getLogger("Cherry");

    /**
     * Is called for each message of type {@link CherryProtocol}.
     *
     * @param ctx the {@link ChannelHandlerContext} which this {@link SimpleChannelInboundHandler}
     *            belongs to
     * @param cherryProtocol the message to handle
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CherryProtocol cherryProtocol) {

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        this.log.warning(cause.getMessage());
    }
}
