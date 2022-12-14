package cn.cherry.client.service;

import cn.cherry.client.base.Receiver;
import com.alibaba.fastjson2.JSONObject;
import io.netty.channel.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static cn.cherry.core.infra.message.MessageType.*;

/**
 * cherry网络通信层面的客户端处理器
 * 用来处理网络定时任务的执行通知
 *
 * @since 2022/10/19
 * @author realDragonKing
 */
public class ClientHandler extends SimpleChannelInboundHandler<JSONObject> {

    private final Receiver receiver;
    private final ClientStarter clientStarter;
    private final Logger logger = LogManager.getLogger("Cherry");

    public ClientHandler(Receiver receiver) {
        this.receiver = receiver;
        this.clientStarter = ClientStarter.getInstance();
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
            case NOTIFY:
                TimePoint timePoint = TimePoint.parse(protocol.getString("timePoint"));
                if (timePoint == null) {
                    ctx.fireExceptionCaught(new Throwable("时间信息格式错误！"));
                    return;
                }
                this.receiver.receiveNotify(
                        timePoint,
                        protocol.getJSONObject("metaData"),
                        protocol.getIntValue("taskId"));
                break;
            case ERROR:
                this.receiver.receiveError(protocol.getString("errorMessage"));
                break;
            case ADD_RESULT:
                this.clientStarter.receiveInvokeResult(
                        ADD_RESULT,
                        protocol.getIntValue("sendingId"),
                        protocol.getIntValue("taskId"),
                        null);
                break;
            case REMOVE_RESULT:
                this.clientStarter.receiveInvokeResult(
                        REMOVE_RESULT,
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
