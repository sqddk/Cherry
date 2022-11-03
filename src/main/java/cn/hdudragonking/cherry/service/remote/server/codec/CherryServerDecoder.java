package cn.hdudragonking.cherry.service.remote.server.codec;

import cn.hdudragonking.cherry.service.remote.CherryHealthMonitor;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

import static cn.hdudragonking.cherry.service.remote.CherryProtocolFlag.*;

/**
 * 基于cherry协议的服务端解码器
 *
 * @since 2022/10/18
 * @author realDragonKing
 */
public class CherryServerDecoder extends MessageToMessageDecoder<ByteBuf> {

    private final CherryHealthMonitor monitor;

    public CherryServerDecoder() {
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
        try {
            JSONObject protocol = JSON.parseObject(msg.array());

            switch (protocol.getIntValue("flag")) {

                case FLAG_PING :
                    monitor.acceptPing(protocol.getString("channelName"));
                    break;

                case FLAG_ADD :
                case FLAG_REMOVE :
                    out.add(protocol);
                    break;

                default: ctx.fireExceptionCaught(new Throwable("无效协议！"));

            }
        } catch (Exception e) {
            ctx.fireExceptionCaught(new Throwable("无效协议！"));
        }
    }

}