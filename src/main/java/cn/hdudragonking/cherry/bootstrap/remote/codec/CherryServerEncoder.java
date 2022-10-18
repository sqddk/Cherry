package cn.hdudragonking.cherry.bootstrap.remote.codec;

import cn.hdudragonking.cherry.bootstrap.remote.protocol.CherryProtocol;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.List;

import static cn.hdudragonking.cherry.bootstrap.remote.protocol.CherryProtocolFlag.*;

/**
 * cherryÐ­ÒéµÄ±àÂëÆ÷
 *
 * @since 2022/10/18
 * @author realDragonKing
 */
public class CherryServerEncoder extends MessageToMessageEncoder<CherryProtocol> {

    private final Charset charset;

    public CherryServerEncoder() {
        this.charset = Charset.defaultCharset();
    }

    /**
     * Encode from one message to another. This method will be called for each written message that can be handled
     * by this encoder.
     *
     * @param ctx the {@link ChannelHandlerContext} which this {@link MessageToMessageEncoder} belongs to
     * @param cherryProtocol the message to encode to another one
     * @param out the {@link List} into which the encoded msg should be added
     *            needs to do some kind of aggregation
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, CherryProtocol cherryProtocol, List<Object> out) {
        String finalMessage;
        switch (cherryProtocol.getFlag()) {
            case FLAG_PONG :
                finalMessage = FLAG_PONG + "|";
                break;
            case FLAG_NOTIFY:
                finalMessage = FLAG_NOTIFY + "|"
                        + cherryProtocol.getTimePointString() + "|"
                        + cherryProtocol.getMetaData() + "|"
                        + cherryProtocol.getUniqueID();
                break;
            case FLAG_ERROR:
                finalMessage = FLAG_ERROR + "|" + FLAG_ERROR;
                break;
            default :
                return;
        }
        out.add(ByteBufUtil.encodeString(ctx.alloc(), CharBuffer.wrap(finalMessage + "\r"), charset));
    }

}
