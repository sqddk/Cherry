package cn.hdudragonking.cherry.bootstrap.remote.client.codec;

import cn.hdudragonking.cherry.bootstrap.remote.protocol.CherryProtocol;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.List;

import static cn.hdudragonking.cherry.bootstrap.remote.protocol.CherryProtocolFlag.*;

/**
 * ����cherryͨ��Э��Ŀͻ��˱�����
 *
 * @since 2022/10/19
 * @author realDragonKing
 */
public class CherryClientEncoder extends MessageToMessageEncoder<CherryProtocol> {

    private final Charset charset;

    public CherryClientEncoder() {
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
    protected void encode(ChannelHandlerContext ctx, CherryProtocol protocol, List<Object> out) {
        String finalMessage;
        switch (protocol.getFlag()) {
            case FLAG_PING :
                finalMessage = FLAG_PING + "|";
                break;
            case FLAG_ADD :
                finalMessage = FLAG_ADD + "|" +
                        protocol.getStringTimePoint() + "|" +
                        protocol.getMetaData();
                break;
            case FLAG_REMOVE:
                finalMessage = FLAG_REMOVE + "|" +
                        protocol.getStringTimePoint() + "|" +
                        protocol.getTaskID();
                break;
            default :
                return;
        }
        out.add(ByteBufUtil.encodeString(ctx.alloc(), CharBuffer.wrap(finalMessage + "\r"), charset));
    }
}