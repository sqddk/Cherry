package cn.hdudragonking.cherry.bootstrap.remote.server;

import cn.hdudragonking.cherry.bootstrap.CherryLocalStarter;
import cn.hdudragonking.cherry.bootstrap.remote.protocol.CherryProtocol;
import cn.hdudragonking.cherry.engine.base.TimePoint;
import cn.hdudragonking.cherry.engine.task.ReminderTask;
import io.netty.channel.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static cn.hdudragonking.cherry.bootstrap.remote.protocol.CherryProtocolFlag.*;

/**
 * cherry网络通信层面的服务端处理器，
 * 用来处理网络定时任务提交和回执提醒通信
 *
 * @since 2022/10/18
 * @author realDragonKing
 */
public class ServerHandler extends SimpleChannelInboundHandler<CherryProtocol> {

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
     * @param protocol the message to handle
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CherryProtocol protocol) {
        TimePoint timePoint = TimePoint.parse(protocol.getStringTimePoint());
        if (timePoint == null) {
            ctx.fireExceptionCaught(new Throwable("时间信息格式错误！"));
            return;
        }
        switch (protocol.getFlag()) {

            case FLAG_ADD :
                this.logger.info(ctx.channel().localAddress() + " 提交了一个定时任务！");
                int[] result = this.cherryLocalStarter
                        .submit(new ReminderTask(timePoint, protocol.getMetaData(), ctx.channel()));
                protocol.setFlag(FLAG_RESULT_ADD);
                if (result.length == 2) {
                    protocol.setTaskID(String.valueOf(result[1])).setResult("1");
                    this.logger.info(ctx.channel().localAddress() + " 定时任务提交成功！");
                } else {
                    protocol.setResult("0");
                    this.logger.info(ctx.channel().localAddress() + " 定时任务提交失败！");
                }
                ctx.writeAndFlush(protocol);
                break;

            case FLAG_REMOVE :
                this.logger.info(ctx.channel().localAddress() + " 尝试删除一个定时任务！");
                protocol.setFlag(FLAG_RESULT_REMOVE);
                if (this.cherryLocalStarter.remove(timePoint, protocol.getTaskID())) {
                    protocol.setResult("1");
                    this.logger.info(ctx.channel().localAddress() + " 定时任务删除成功！");
                } else {
                    protocol.setResult("0");
                    this.logger.info(ctx.channel().localAddress() + " 定时任务删除失败！");
                }
                ctx.writeAndFlush(protocol);
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
        CherryProtocol protocol = new CherryProtocol()
                .setFlag(FLAG_ERROR)
                .setErrorMessage(cause.getMessage());
        ctx.writeAndFlush(protocol);
    }

}
