package cn.hdudragonking.cherry.bootstrap.remote.server.codec;

import cn.hdudragonking.cherry.bootstrap.remote.CherryHealthMonitor;
import cn.hdudragonking.cherry.bootstrap.remote.protocol.CherryProtocol;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

import static cn.hdudragonking.cherry.bootstrap.remote.protocol.CherryProtocolFlag.*;

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
            CherryProtocol protocol;
            JSONObject json = JSON.parseObject(msg.array());
            String channelName = json.getString("channelName");
            String timePoint = json.getString("timePoint");

            switch (json.getIntValue("flag")) {

                case FLAG_PING :
                    monitor.acceptPing(channelName);
                    break;

                case FLAG_ADD :
                    protocol = new CherryProtocol(FLAG_ADD)
                            .setChannelName(channelName)
                            .setStringTimePoint(timePoint)
                            .setMetaData(json.getJSONObject("metaData"));
                    out.add(protocol);
                    break;

                case FLAG_REMOVE :
                    protocol = new CherryProtocol(FLAG_ADD)
                            .setChannelName(channelName)
                            .setStringTimePoint(timePoint)
                            .setTaskID(json.getIntValue("taskID"));
                    out.add(protocol);
                    break;

                default: ctx.fireExceptionCaught(new Throwable("无效协议！"));

            }
        } catch (Exception e) {
            ctx.fireExceptionCaught(new Throwable("无效协议！"));
        }
    }

}
