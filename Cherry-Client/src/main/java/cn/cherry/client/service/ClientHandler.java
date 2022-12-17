package cn.cherry.client.service;

import cn.cherry.core.message.MessageHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.Charset;


/**
 * 处理网络通信
 *
 * @author realDragonKing
 */
public class ClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private final Logger logger;
    private final Charset charset;

    public ClientHandler() {
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
        this.logger.info("与服务端 " + ctx.channel().remoteAddress() + " 的通信信道已经打开！");
    }

    /**
     * Is called for each message of type {@link String}.
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
     * @param cause 异常信息
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        this.logger.error(cause.getMessage());
    }

}
