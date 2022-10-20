package cn.hdudragonking.cherry.bootstrap.remote.client.codec;

import cn.hdudragonking.cherry.bootstrap.remote.CherryHealthMonitor;
import cn.hdudragonking.cherry.bootstrap.remote.protocol.CherryProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.nio.charset.Charset;
import java.util.List;

import static cn.hdudragonking.cherry.bootstrap.remote.protocol.CherryProtocolFlag.*;

/**
 * 基于cherry通信协议的客户端解码器
 *
 * @since 2022/10/19
 * @author realDragonKing
 */
public class CherryClientDecoder extends MessageToMessageDecoder<ByteBuf> {

    private final Charset charset;
    private final CherryHealthMonitor monitor;

    public CherryClientDecoder() {
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
                    case FLAG_PONG :
                        monitor.acceptPong(ctx.channel());
                        break;
                    case FLAG_NOTIFY :
                        protocol.setFlag(FLAG_NOTIFY);
                        if (pieces.length == 4) {
                            protocol.setStringTimePoint(pieces[1])
                                    .setMetaData(pieces[2].length() != 0 ? pieces[2] : null)
                                    .setTaskID(pieces[3]);
                            out.add(protocol);
                        } else ctx.fireExceptionCaught(new Throwable("无效协议！"));
                        break;
                    case FLAG_ERROR :
                        if (pieces.length == 2) {
                            protocol.setFlag(FLAG_ERROR)
                                    .setErrorMessage(pieces[1]);
                            out.add(protocol);
                        } else ctx.fireExceptionCaught(new Throwable("无效协议！"));
                        break;
                    case FLAG_RESULT_ADD :
                        if (pieces.length == 4) {
                            protocol.setStringTimePoint(pieces[1])
                                    .setMetaData(pieces[2].length() != 0 ? pieces[2] : null)
                                    .setResult(pieces[3]);
                            out.add(protocol);
                        } else ctx.fireExceptionCaught(new Throwable("无效协议！"));
                        break;
                    case FLAG_RESULT_REMOVE :
                        if (pieces.length == 4) {
                            protocol.setStringTimePoint(pieces[1])
                                    .setTaskID(pieces[2])
                                    .setResult(pieces[3]);
                            out.add(protocol);
                        } else ctx.fireExceptionCaught(new Throwable("无效协议！"));
                        break;
                    default: ctx.fireExceptionCaught(new Throwable("无效协议！"));
                }
            } catch (Exception e) {
                ctx.fireExceptionCaught(new Throwable("无效协议！"));
            }
        }
    }

}
