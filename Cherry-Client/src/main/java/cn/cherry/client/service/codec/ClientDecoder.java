package cn.cherry.client.service.codec;

import cn.cherry.core.infra.message.Message;
import cn.cherry.core.infra.message.MessageResolver;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.nio.charset.Charset;
import java.util.List;


/**
 * 基于cherry通信协议的客户端解码器
 *
 * @since 2022/10/19
 * @author realDragonKing
 */
public class ClientDecoder extends MessageToMessageDecoder<ByteBuf> {

    private final Charset charset;

    public ClientDecoder() {
        this.charset = Charset.defaultCharset();
    }

    /**
     * Decode from one message to another. This method will be called for each written message that can be handled
     * by this decoder.
     *
     * @param ctx the {@link ChannelHandlerContext} which this {@link MessageToMessageDecoder} belongs to
     * @param msg the message to decode to another one
     * @param out the {@link List} to which decoded messages should be added
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) {
        try {
            Message message = MessageResolver.tryResolve(msg, charset);
            if (message != null) {
                out.add(message);
            } else ctx.fireExceptionCaught(new Throwable("无效协议！"));
        } catch (Exception e) {
            ctx.fireExceptionCaught(e);
        }
    }

}
