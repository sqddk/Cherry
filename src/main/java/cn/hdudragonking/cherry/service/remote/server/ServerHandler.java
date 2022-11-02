package cn.hdudragonking.cherry.service.remote.server;

import cn.hdudragonking.cherry.service.CherryLocalStarter;
import cn.hdudragonking.cherry.engine.base.TimePoint;
import cn.hdudragonking.cherry.engine.task.ReminderTask;
import com.alibaba.fastjson2.JSONObject;
import io.netty.channel.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static cn.hdudragonking.cherry.service.remote.CherryProtocolFlag.*;

/**
 * cherry网络通信层面的服务端处理器，
 * 用来处理网络定时任务提交和回执提醒通信
 *
 * @since 2022/10/18
 * @author realDragonKing
 */
public class ServerHandler extends SimpleChannelInboundHandler<JSONObject> {

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
     * Is called for each message of type {@link JSONObject}.
     *
     * @param ctx the {@link ChannelHandlerContext} which this {@link SimpleChannelInboundHandler}
     *            belongs to
     * @param reqProtocol the message to handle
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, JSONObject reqProtocol) {
        TimePoint timePoint = TimePoint.parse(reqProtocol.getString("timePoint"));
        if (timePoint == null) {
            ctx.fireExceptionCaught(new Throwable("时间信息格式错误！"));
            return;
        }
        JSONObject resProtocol = new JSONObject();
        String channelName = reqProtocol.getString("channelName");
        JSONObject metaData = reqProtocol.getJSONObject("metaData");
        if (channelName == null) {
            ctx.fireExceptionCaught(new Throwable("未提交客户端名称！"));
            return;
        }
        // TODO 这里以后应当考虑布隆过滤器
        if (this.channelMap.get(channelName) == null) {
            this.channelMap.put(channelName, ctx.channel());
        }

        switch (reqProtocol.getIntValue("flag")) {

            case FLAG_ADD :
                this.logger.info(channelName + " 提交了一个定时任务！");
                int[] result = this.cherryLocalStarter
                        .submit(new ReminderTask(
                                channelName,
                                timePoint,
                                metaData.toJSONString()
                        ));
                resProtocol.put("flag", FLAG_RESULT_ADD);
                resProtocol.put("metaData", metaData);
                if (result.length == 2) {
                    resProtocol.put("taskId", result[1]);
                    resProtocol.put("result", true);
                    this.logger.info(channelName + " 定时任务提交成功！");
                } else {
                    resProtocol.put("result", false);
                    this.logger.info(channelName + " 定时任务提交失败！");
                }
                ctx.writeAndFlush(resProtocol);
                break;

            case FLAG_REMOVE :
                this.logger.info(channelName + " 尝试删除一个定时任务！");
                int taskId = reqProtocol.getIntValue("taskId");
                resProtocol.put("flag", FLAG_RESULT_REMOVE);
                resProtocol.put("metaData", metaData);
                if (this.cherryLocalStarter.remove(timePoint, taskId)) {
                    resProtocol.put("result", true);
                    this.logger.info(channelName + " 定时任务删除成功！");
                } else {
                    resProtocol.put("result", false);
                    this.logger.info(channelName + " 定时任务删除失败！");
                }
                ctx.writeAndFlush(resProtocol);
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
        JSONObject protocol = new JSONObject(Map.of(
                "flag", FLAG_ERROR,
                "errorMessage", cause.getMessage()
        ));
        ctx.writeAndFlush(protocol);
    }

}
