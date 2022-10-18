package cn.hdudragonking.cherry.bootstrap.remote;

import cn.hdudragonking.cherry.bootstrap.remote.protocol.CherryProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.logging.Logger;

/**
 * cherry����ͨ�Ų�����ն˴�������
 * �����������綨ʱ�����ύ�ͻ�ִ����ͨ��
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
