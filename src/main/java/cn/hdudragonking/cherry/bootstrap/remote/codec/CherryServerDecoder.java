package cn.hdudragonking.cherry.bootstrap.remote.codec;

import cn.hdudragonking.cherry.bootstrap.remote.CherryHealthMonitor;
import cn.hdudragonking.cherry.bootstrap.remote.protocol.CherryProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.nio.charset.Charset;
import java.util.List;

import static cn.hdudragonking.cherry.bootstrap.remote.protocol.CherryProtocolFlag.*;

/**
 * cherry协议的解码器
 *
 * @since 2022/10/18
 * @author realDragonKing
 */
public class CherryServerDecoder extends MessageToMessageDecoder<ByteBuf> {

    private final Charset charset;
    private final CherryHealthMonitor monitor;

    public CherryServerDecoder() {
        this.charset = Charset.defaultCharset();
        this.monitor = CherryHealthMonitor.getInstance();
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
        CherryProtocol protocol = new CherryProtocol();
        String[] pieces = msg.toString(this.charset).split("\\|");
        if (pieces.length == 0) ctx.fireExceptionCaught(new Throwable("无效协议！"));
        else {
            try {
                switch (Integer.parseInt(pieces[0])) {
                    case FLAG_PING :
                        monitor.acceptPing(ctx.channel());
                        break;
                    case FLAG_ADD :
                        if (pieces.length >= 2) protocol.setStringTimePoint(pieces[1]);
                        if (pieces.length == 3) protocol.setMetaData(pieces[2]);
                        out.add(protocol);
                        break;
                    case FLAG_REMOVE :
                        if (pieces.length == 3) {
                            protocol.setStringTimePoint(pieces[1])
                                    .setUniqueID(pieces[2]);
                            out.add(protocol);
                        } else ctx.fireExceptionCaught(new Throwable("删除操作参数不全！无效删除操作！"));
                        break;
                    default: ctx.fireExceptionCaught(new Throwable("无效协议！"));
                }
            } catch (Exception e) {
                ctx.fireExceptionCaught(new Throwable("无效协议！"));
            }
        }
    }
}
