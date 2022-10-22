package cn.hdudragonking.cherry.bootstrap.remote.client.codec;

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
 * 基于cherry通信协议的客户端解码器
 *
 * @since 2022/10/19
 * @author realDragonKing
 */
public class CherryClientDecoder extends MessageToMessageDecoder<ByteBuf> {

    private final CherryHealthMonitor monitor;

    public CherryClientDecoder() {
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
            JSONObject metaData = json.getJSONObject("metaData");
            int taskID = json.getIntValue("taskID");
            String timePoint = json.getString("timePoint");

            switch (json.getIntValue("flag")) {

                case FLAG_PONG :
                    monitor.acceptPong(ctx.channel());
                    break;

                case FLAG_NOTIFY :
                    protocol = new CherryProtocol(FLAG_NOTIFY)
                            .setMetaData(metaData)
                            .setStringTimePoint(timePoint)
                            .setTaskID(taskID);
                    out.add(protocol);
                    break;

                case FLAG_ERROR :
                    protocol = new CherryProtocol(FLAG_ERROR)
                            .setErrorMessage(json.getString("errorMessage"));
                    out.add(protocol);
                    break;

                case FLAG_RESULT_ADD :
                    protocol = new CherryProtocol(FLAG_RESULT_ADD)
                            .setStringTimePoint(timePoint)
                            .setTaskID(taskID)
                            .setMetaData(metaData)
                            .setResult(json.getBooleanValue("result"));
                    out.add(protocol);
                    break;

                case FLAG_RESULT_REMOVE :
                    protocol = new CherryProtocol(FLAG_RESULT_REMOVE)
                            .setStringTimePoint(timePoint)
                            .setTaskID(taskID)
                            .setMetaData(metaData)
                            .setResult(json.getBooleanValue("result"));
                    out.add(protocol);
                    break;

                default: ctx.fireExceptionCaught(new Throwable("无效协议！"));
            }
        } catch (Exception e) {
            ctx.fireExceptionCaught(new Throwable("无效协议！"));
        }
    }

}
