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
 * cherry����ͨ�Ų���ķ���˴�������
 * �����������綨ʱ�����ύ�ͻ�ִ����ͨ��
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
     * @param requestProtocol the message to handle
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CherryProtocol requestProtocol) {
        TimePoint timePoint = TimePoint.parse(requestProtocol.getStringTimePoint());
        if (timePoint == null) {
            ctx.fireExceptionCaught(new Throwable("ʱ����Ϣ��ʽ����"));
            return;
        }
        CherryProtocol responseProtocol;
        String channelName = requestProtocol.getChannelName();
        JSONObject metaData = requestProtocol.getMetaData();
        if (channelName == null) {
            ctx.fireExceptionCaught(new Throwable("δ�ύ�ͻ������ƣ�"));
            return;
        }
        // TODO �����Ժ�Ӧ�����ǲ�¡������
        if (this.channelMap.get(channelName) == null) {
            this.channelMap.put(channelName, ctx.channel());
        }

        switch (requestProtocol.getFlag()) {

            case FLAG_ADD :
                this.logger.info(channelName + " �ύ��һ����ʱ����");
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
                    this.logger.info(channelName + " ��ʱ�����ύ�ɹ���");
                } else {
                    responseProtocol.setResult(false);
                    this.logger.info(channelName + " ��ʱ�����ύʧ�ܣ�");
                }
                ctx.writeAndFlush(responseProtocol);
                break;

            case FLAG_REMOVE :
                this.logger.info(channelName + " ����ɾ��һ����ʱ����");
                int taskID = requestProtocol.getTaskID();
                responseProtocol = new CherryProtocol(FLAG_RESULT_REMOVE)
                        .setMetaData(metaData)
                        .setTaskID(taskID);
                if (this.cherryLocalStarter.remove(timePoint, taskID)) {
                    responseProtocol.setResult(true);
                    this.logger.info(channelName + " ��ʱ����ɾ���ɹ���");
                } else {
                    responseProtocol.setResult(false);
                    this.logger.info(channelName + " ��ʱ����ɾ��ʧ�ܣ�");
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
     * @param ctx ͨ�Źܵ�
     * @param cause �쳣ԭ��
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        this.logger.error(cause.getMessage());
        CherryProtocol protocol = new CherryProtocol(FLAG_ERROR)
                .setErrorMessage(cause.getMessage());
        ctx.writeAndFlush(protocol);
    }

}
