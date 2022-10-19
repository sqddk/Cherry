package cn.hdudragonking.cherry.bootstrap.remote;

import cn.hdudragonking.cherry.bootstrap.CherryLocalStarter;
import cn.hdudragonking.cherry.bootstrap.remote.protocol.CherryProtocol;
import cn.hdudragonking.cherry.engine.base.TimePoint;
import cn.hdudragonking.cherry.engine.task.ReminderTask;
import io.netty.channel.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static cn.hdudragonking.cherry.bootstrap.remote.protocol.CherryProtocolFlag.*;

/**
 * cherry����ͨ�Ų�����ն˴�������
 * �����������綨ʱ�����ύ�ͻ�ִ����ͨ��
 *
 * @since 2022/10/18
 * @author realDragonKing
 */
public class CherryServerHandler extends SimpleChannelInboundHandler<CherryProtocol> {

    private final Logger logger = LogManager.getLogger("Cherry");
    private final CherryLocalStarter cherryLocalStarter = CherryLocalStarter.getInstance();

    /**
     * Calls {@link ChannelHandlerContext#fireChannelActive()} to forward
     * to the next {@link ChannelInboundHandler} in the {@link ChannelPipeline}.
     * <p>
     * Subclasses may override this method to change behavior.
     *
     * @param ctx ͨ�Źܵ�
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        this.logger.info("һ���ͻ����Ѿ��� " + ctx.channel().remoteAddress() + " �ϳɹ����뱾����ˣ�");
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
            ctx.fireExceptionCaught(new Throwable("ʱ����Ϣ��ʽ����"));
            return;
        }
        if (protocol.getFlag() == FLAG_ADD) {
            this.logger.info(ctx.channel().localAddress() + " �ύ��һ����ʱ����");
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
     * @param ctx ͨ�Źܵ�
     * @param cause �쳣ԭ��
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
