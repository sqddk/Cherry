package cn.hdudragonking.cherry.bootstrap.remote.client;

import cn.hdudragonking.cherry.bootstrap.remote.protocol.CherryProtocol;
import cn.hdudragonking.cherry.engine.base.TimePoint;
import io.netty.channel.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static cn.hdudragonking.cherry.bootstrap.remote.protocol.CherryProtocolFlag.*;

/**
 * cherry����ͨ�Ų���Ŀͻ��˴�����
 * �����������綨ʱ�����ִ��֪ͨ
 *
 * @since 2022/10/19
 * @author realDragonKing
 */
public class ClientHandler extends SimpleChannelInboundHandler<CherryProtocol> {

    private final Receiver receiver;
    private final Logger logger = LogManager.getLogger("Cherry");

    public ClientHandler(Receiver receiver) {
        this.receiver = receiver;
    }

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
        this.logger.info("������ " + ctx.channel().remoteAddress() + " ��ͨ���ŵ��Ѿ��򿪣�");
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
        switch (protocol.getFlag()) {
            case FLAG_NOTIFY :
                TimePoint timePoint = TimePoint.parse(protocol.getStringTimePoint());
                if (timePoint == null) {
                    ctx.fireExceptionCaught(new Throwable("ʱ����Ϣ��ʽ����"));
                    return;
                }
                this.receiver.receiveNotify(timePoint, protocol.getMetaData(), protocol.getTaskID());
                break;
            case FLAG_ERROR :
                this.receiver.receiveError(protocol.getErrorMessage());
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
     * @param cause �쳣��Ϣ
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        this.logger.error(cause.getMessage());
    }

}
