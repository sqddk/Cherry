package cn.cherry.server.service;

import cn.cherry.core.message.MessageHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.Charset;


/**
 * cherry网络通信层面的服务端处理器，来处理网络定时任务提交和回执提醒通信
 *
 * @author realDragonKing
 */
public class ServerHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private final Logger logger;
    private final Charset charset;

    public ServerHandler() {
        this.logger = LogManager.getLogger("Cherry");
        this.charset = Charset.defaultCharset();
    }

    /**
     * Calls {@link ChannelHandlerContext#fireChannelActive()} to forward
     * to the next {@link ChannelInboundHandler} in the {@link ChannelPipeline}.
     * <p>
     * Subclasses may override this method to change behavior.
     *
     * @param ctx 通信管道
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        this.logger.info("一个cherry客户端已经在 " + ctx.channel().remoteAddress() + " 上成功接入本服务端！通信信道打开！");
    }

    /**
     * Is called for each message of type {@link ByteBuf}.
     *
     * @param ctx the {@link ChannelHandlerContext} which this {@link SimpleChannelInboundHandler}
     *            belongs to
     * @param byteBuf the message to handle
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf byteBuf) {
        MessageHandler.tryResolve(ctx.channel(), byteBuf.toString(this.charset));
    }

    /**
     * Calls {@link ChannelHandlerContext#fireExceptionCaught(Throwable)} to forward
     * to the next {@link ChannelHandler} in the {@link ChannelPipeline}.
     * <p>
     * Subclasses may override this method to change behavior.
     *
     * @param ctx 通信管道
     * @param cause 异常原因
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        this.logger.error(cause.getMessage());
    }

}
