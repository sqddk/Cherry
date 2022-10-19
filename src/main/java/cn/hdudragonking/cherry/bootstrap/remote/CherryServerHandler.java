package cn.hdudragonking.cherry.bootstrap.remote;

import cn.hdudragonking.cherry.bootstrap.CherryLocalStarter;
import cn.hdudragonking.cherry.bootstrap.remote.protocol.CherryProtocol;
import cn.hdudragonking.cherry.engine.base.TimePoint;
import cn.hdudragonking.cherry.engine.task.ReminderTask;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import java.util.logging.Logger;

import static cn.hdudragonking.cherry.bootstrap.remote.protocol.CherryProtocolFlag.*;

/**
 * cherry网络通信层面的终端处理器，
 * 用来处理网络定时任务提交和回执提醒通信
 *
 * @since 2022/10/18
 * @author realDragonKing
 */
public class CherryServerHandler extends SimpleChannelInboundHandler<CherryProtocol> {

    private final Logger log = Logger.getLogger("Cherry");
    private final CherryLocalStarter cherryLocalStarter = CherryLocalStarter.getInstance();

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
        if (protocol.getFlag() == FLAG_ADD) {
            this.cherryLocalStarter.submit(new ReminderTask(timePoint, ctx.channel()));
        } else if (protocol.getFlag() == FLAG_REMOVE) {
            this.cherryLocalStarter.remove(timePoint, protocol.getUniqueID());
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
        this.log.warning(cause.getMessage());
        CherryProtocol protocol = new CherryProtocol()
                .setFlag(FLAG_ERROR)
                .setErrorMessage(cause.getMessage());
        ctx.writeAndFlush(protocol);
    }
}
