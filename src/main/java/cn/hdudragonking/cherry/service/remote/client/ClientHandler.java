package cn.hdudragonking.cherry.service.remote.client;

import cn.hdudragonking.cherry.engine.base.TimePoint;
import com.alibaba.fastjson2.JSONObject;
import io.netty.channel.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static cn.hdudragonking.cherry.service.remote.CherryProtocolFlag.*;

/**
 * cherry网络通信层面的客户端处理器
 * 用来处理网络定时任务的执行通知
 *
 * @since 2022/10/19
 * @author realDragonKing
 */
public class ClientHandler extends SimpleChannelInboundHandler<JSONObject> {

    private final ClientReceiver clientReceiver;
    private final CherryClient cherryClient;
    private final Logger logger = LogManager.getLogger("Cherry");

    public ClientHandler(ClientReceiver clientReceiver) {
        this.clientReceiver = clientReceiver;
        this.cherryClient = CherryClient.getInstance();
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
     * @param protocol the message to handle
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, JSONObject protocol) {
        switch (protocol.getIntValue("flag")) {
            case FLAG_NOTIFY :
                TimePoint timePoint = TimePoint.parse(protocol.getString("timePoint"));
                if (timePoint == null) {
                    ctx.fireExceptionCaught(new Throwable("时间信息格式错误！"));
                    return;
                }
                this.clientReceiver.receiveNotify(
                        timePoint,
                        protocol.getJSONObject("metaData"),
                        protocol.getIntValue("taskId"));
                break;
            case FLAG_ERROR :
                this.clientReceiver.receiveError(protocol.getString("errorMessage"));
                break;
            case FLAG_RESULT_ADD :
                this.cherryClient.receiveInvokeResult(
                        FLAG_RESULT_ADD,
                        protocol.getIntValue("sendingId"),
                        protocol.getIntValue("taskId"),
                        null);
                break;
            case FLAG_RESULT_REMOVE :
                this.cherryClient.receiveInvokeResult(
                        FLAG_RESULT_REMOVE,
                        protocol.getIntValue("sendingId"),
                        null,
                        protocol.getBooleanValue("result"));
                break;
        }
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