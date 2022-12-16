package cn.cherry.client.service;

import cn.cherry.core.infra.message.MessageHandler;
import com.alibaba.fastjson2.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.Charset;


/**
 * cherry网络通信层面的客户端处理器
 * 用来处理网络定时任务的执行通知
 *
 * @since 2022/10/19
 * @author realDragonKing
 */
public class ClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private final Charset charset;
    private final Logger logger = LogManager.getLogger("Cherry");

    public ClientHandler() {
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
     * Is called for each message of type {@link JSONObject}.
     *
     * @param ctx the {@link ChannelHandlerContext} which this {@link SimpleChannelInboundHandler}
     *            belongs to
     * @param message the message to handle
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf byteBuf) {
        MessageHandler.tryResolve(ctx.channel(), byteBuf, this.charset);
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
