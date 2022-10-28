package cn.hdudragonking.cherry.bootstrap.remote.server;

import cn.hdudragonking.cherry.bootstrap.CherryLocalStarter;
import cn.hdudragonking.cherry.bootstrap.remote.protocol.CherryProtocol;
import cn.hdudragonking.cherry.engine.base.TimePoint;
import cn.hdudragonking.cherry.engine.task.ReminderTask;
import com.alibaba.fastjson2.JSONObject;
import io.netty.channel.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;

import static cn.hdudragonking.cherry.bootstrap.remote.protocol.CherryProtocolFlag.*;

/**
 * cherry网络通信层面的服务端处理器，
 * 用来处理网络定时任务提交和回执提醒通信
 *
 * @since 2022/10/18
 * @author realDragonKing
 */
public class ServerHandler extends SimpleChannelInboundHandler<CherryProtocol> {

    private final ConcurrentHashMap<String, Channel> channelMap = CherryServer.getInstance().getChannelMap();
    private final Logger logger = LogManager.getLogger("Cherry");
    private final CherryLocalStarter cherryLocalStarter = CherryLocalStarter.getInstance();

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
        this.logger.info("一个cherry客户端已经在 " + ctx.channel().remoteAddress() + " 上成功接入本服务端！通信信道打开！");
    }

    /**
     * Is called for each message of type {@link CherryProtocol}.
     *
     * @param ctx the {@link ChannelHandlerContext} which this {@link SimpleChannelInboundHandler}
     *            belongs to
     * @param requestProtocol the message to handle
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CherryProtocol requestProtocol) {
        TimePoint timePoint = TimePoint.parse(requestProtocol.getStringTimePoint());
        if (timePoint == null) {
            ctx.fireExceptionCaught(new Throwable("时间信息格式错误！"));
            return;
        }
        CherryProtocol responseProtocol;
        String channelName = requestProtocol.getChannelName();
        JSONObject metaData = requestProtocol.getMetaData();
        if (channelName == null) {
            ctx.fireExceptionCaught(new Throwable("未提交客户端名称！"));
            return;
        }
        // TODO 这里以后应当考虑布隆过滤器
        if (this.channelMap.get(channelName) == null) {
            this.channelMap.put(channelName, ctx.channel());
        }

        switch (requestProtocol.getFlag()) {

            case FLAG_ADD :
                this.logger.info(channelName + " 提交了一个定时任务！");
                int[] result = this.cherryLocalStarter
                        .submit(new ReminderTask(
                                channelName,
                                timePoint,
                                metaData.toJSONString()
                        ));
                responseProtocol = new CherryProtocol(FLAG_RESULT_ADD)
                        .setMetaData(metaData);
                if (result.length == 2) {
                    responseProtocol.setTaskID(result[1]).setResult(true);
                    this.logger.info(channelName + " 定时任务提交成功！");
                } else {
                    responseProtocol.setResult(false);
                    this.logger.info(channelName + " 定时任务提交失败！");
                }
                ctx.writeAndFlush(responseProtocol);
                break;

            case FLAG_REMOVE :
                this.logger.info(channelName + " 尝试删除一个定时任务！");
                int taskID = requestProtocol.getTaskID();
                responseProtocol = new CherryProtocol(FLAG_RESULT_REMOVE)
                        .setMetaData(metaData)
                        .setTaskID(taskID);
                if (this.cherryLocalStarter.remove(timePoint, taskID)) {
                    responseProtocol.setResult(true);
                    this.logger.info(channelName + " 定时任务删除成功！");
                } else {
                    responseProtocol.setResult(false);
                    this.logger.info(channelName + " 定时任务删除失败！");
                }
                ctx.writeAndFlush(responseProtocol);
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
     * @param cause 异常原因
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        this.logger.error(cause.getMessage());
        CherryProtocol protocol = new CherryProtocol(FLAG_ERROR)
                .setErrorMessage(cause.getMessage());
        ctx.writeAndFlush(protocol);
    }

}
