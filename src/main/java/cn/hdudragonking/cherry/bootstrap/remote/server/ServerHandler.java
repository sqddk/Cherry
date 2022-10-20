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
 * cherry����ͨ�Ų���ķ���˴�������
 * �����������綨ʱ�����ύ�ͻ�ִ����ͨ��
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
     * @param ctx ͨ�Źܵ�
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        this.logger.info("һ��cherry�ͻ����Ѿ��� " + ctx.channel().remoteAddress() + " �ϳɹ����뱾����ˣ�ͨ���ŵ��򿪣�");
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
        switch (protocol.getFlag()) {

            case FLAG_ADD :
                this.logger.info(ctx.channel().localAddress() + " �ύ��һ����ʱ����");
                int[] result = this.cherryLocalStarter
                        .submit(new ReminderTask(timePoint, protocol.getMetaData(), ctx.channel()));
                protocol.setFlag(FLAG_RESULT_ADD);
                if (result.length == 2) {
                    protocol.setTaskID(String.valueOf(result[1])).setResult("1");
                    this.logger.info(ctx.channel().localAddress() + " ��ʱ�����ύ�ɹ���");
                } else {
                    protocol.setResult("0");
                    this.logger.info(ctx.channel().localAddress() + " ��ʱ�����ύʧ�ܣ�");
                }
                ctx.writeAndFlush(protocol);
                break;

            case FLAG_REMOVE :
                this.logger.info(ctx.channel().localAddress() + " ����ɾ��һ����ʱ����");
                protocol.setFlag(FLAG_RESULT_REMOVE);
                if (this.cherryLocalStarter.remove(timePoint, protocol.getTaskID())) {
                    protocol.setResult("1");
                    this.logger.info(ctx.channel().localAddress() + " ��ʱ����ɾ���ɹ���");
                } else {
                    protocol.setResult("0");
                    this.logger.info(ctx.channel().localAddress() + " ��ʱ����ɾ��ʧ�ܣ�");
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
