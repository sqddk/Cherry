package cn.cherry.server.service;

import cn.cherry.core.LocalStarter;
import cn.cherry.core.engine.base.TimePoint;
import cn.cherry.server.base.bucket.ChannelBucket;
import cn.cherry.server.base.task.NotifyTask;
import com.alibaba.fastjson2.JSONObject;
import io.netty.channel.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

import static cn.cherry.core.ProtocolFlag.*;


/**
 * cherry网络通信层面的服务端处理器，
 * 用来处理网络定时任务提交和回执提醒通信
 *
 * @since 2022/10/18
 * @author realDragonKing
 */
public class ServerHandler extends SimpleChannelInboundHandler<JSONObject> {

    private final ChannelBucket bucket = ChannelBucket.INSTANCE;
    private final Logger logger = LogManager.getLogger("Cherry");
    private final LocalStarter localStarter = LocalStarter.getInstance();

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
        String groupName = reqProtocol.getString("groupName");
        JSONObject metaData = reqProtocol.getJSONObject("metaData");

        resProtocol.put("metaData", metaData);
        resProtocol.put("sendingId", reqProtocol.getIntValue("sendingId"));

        if (groupName == null) {
            ctx.fireExceptionCaught(new Throwable("未提交客户端名称！"));
            return;
        }
        // TODO 这里以后应当考虑布隆过滤器
        this.bucket.addChannel(groupName, ctx.channel());

        switch (reqProtocol.getIntValue("flag")) {

            case FLAG_ADD :
                this.logger.info(groupName + " 提交了一个定时任务！");
                int[] result = this.localStarter
                        .submit(new NotifyTask(
                                groupName,
                                timePoint,
                                metaData == null ? null : metaData.toJSONString()
                        ));
                resProtocol.put("flag", FLAG_RESULT_ADD);
                if (result.length == 2) {
                    resProtocol.put("taskId", result[1]);
                    this.logger.info(groupName + " 定时任务提交成功！");
                } else {
                    this.logger.info(groupName + " 定时任务提交失败！");
                }
                ctx.writeAndFlush(resProtocol);
                break;

            case FLAG_REMOVE :
                this.logger.info(groupName + " 尝试删除一个定时任务！");
                int taskId = reqProtocol.getIntValue("taskId");
                resProtocol.put("flag", FLAG_RESULT_REMOVE);
                if (this.localStarter.remove(timePoint, taskId)) {
                    resProtocol.put("result", true);
                    this.logger.info(groupName + " 定时任务删除成功！");
                } else {
                    resProtocol.put("result", false);
                    this.logger.info(groupName + " 定时任务删除失败！");
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
