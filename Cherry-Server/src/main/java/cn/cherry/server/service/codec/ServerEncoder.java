package cn.cherry.server.service.codec;

import com.alibaba.fastjson2.JSONObject;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.List;

/**
 * 基于cherry通信协议的服务端编码器
 *
 * @since 2022/10/18
 * @author realDragonKing
 */
public class ServerEncoder extends MessageToMessageEncoder<JSONObject> {

    private final Charset charset;

    public ServerEncoder() {
        this.charset = Charset.defaultCharset();
    }

    /**
     * Encode from one message to another. This method will be called for each written message that can be handled
     * by this encoder.
     *
     * @param ctx the {@link ChannelHandlerContext} which this {@link MessageToMessageEncoder} belongs to
     * @param protocol the message to encode to another one
     * @param out the {@link List} into which the encoded msg should be added
     *            needs to do some kind of aggregation
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, JSONObject protocol, List<Object> out) {
        out.add(ByteBufUtil.encodeString(ctx.alloc(), CharBuffer.wrap(protocol.toJSONString() + "\n"), charset));
    }

}