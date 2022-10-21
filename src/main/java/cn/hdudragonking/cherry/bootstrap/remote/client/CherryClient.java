package cn.hdudragonking.cherry.bootstrap.remote.client;

import cn.hdudragonking.cherry.bootstrap.remote.protocol.CherryProtocol;
import cn.hdudragonking.cherry.engine.base.TimePoint;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Executors;

import static cn.hdudragonking.cherry.bootstrap.remote.protocol.CherryProtocolFlag.*;

/**
 * cherry��ʱ������������ socket �������ͻ�������������
 *
 * @since 2022/10/19
 * @author realDragonKing
 */
public class CherryClient {

    private final static class SocketClientHolder {
        private final static CherryClient INSTANCE = new CherryClient();
    }
    public static CherryClient getInstance() {
        return SocketClientHolder.INSTANCE;
    }

    private final Bootstrap bootstrap;
    private Channel channel;
    private final NioEventLoopGroup workerGroup;
    private final Logger logger = LogManager.getLogger("Cherry");

    private CherryClient() {
        this.bootstrap = new Bootstrap();
        this.workerGroup = new NioEventLoopGroup(2);
    }

    /**
     * ͨ���ṩ��host��ַ�Ͷ˿ڣ���ʼ���ͻ��ˣ���������Ŀ������
     *
     * @param host host��ַ
     * @param port port�˿�
     * @param receiver ��Ӧ����/ִ����
     */
    public void initial(String host, int port, Receiver receiver) {
        Executors.newSingleThreadExecutor().submit(() -> {
            try {
                this.bootstrap
                        .channel(NioSocketChannel.class)
                        .group(this.workerGroup)
                        .option(ChannelOption.TCP_NODELAY, true)
                        .handler(new ClientInitializer(receiver));
                ChannelFuture future = this.bootstrap.connect(host, port).sync();
                this.logger.info("��Զ��cherry����� " + future.channel().remoteAddress() + " �������Ѿ�������");
                this.channel = future.channel();
                future.channel().closeFuture().sync();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                this.workerGroup.shutdownGracefully();
            }
        });
    }

    /**
     * ��Զ��cherry������ύһ����ʱ����
     *
     * @param timePoint ʱ���
     * @param metaData Ԫ����
     */
    public void submit(TimePoint timePoint, String metaData) {
        if (this.channel == null) {
            this.logger.error("��Զ��cherry����˵�������δ������");
            return;
        }
        if (!this.channel.isActive()) {
            this.logger.error("��Զ��cherry����� " + this.channel.remoteAddress() + " �����Ӳ����ã�");
            return;
        }
        CherryProtocol protocol = new CherryProtocol()
                .setFlag(FLAG_ADD)
                .setStringTimePoint(timePoint.toString())
                .setMetaData(metaData);
        this.channel.writeAndFlush(protocol);
        this.logger.info("�Ѿ��ɹ���Զ��cherry����� " + this.channel.remoteAddress() + " �ύ��һ����ʱ�������ڵȴ����������");
    }

    /**
     * ��Զ��cherry�������ɾ��һ����ʱ����
     *
     * @param timePoint ʱ���
     * @param taskID ����ID
     */
    public void remove(TimePoint timePoint, String taskID) {
        if (this.channel == null) {
            this.logger.error("��Զ��cherry����˵�������δ������");
            return;
        }
        if (!this.channel.isActive()) {
            this.logger.error("��Զ��cherry����� " + this.channel.remoteAddress() + " �����Ӳ����ã�");
            return;
        }
        CherryProtocol protocol = new CherryProtocol()
                .setFlag(FLAG_REMOVE)
                .setStringTimePoint(timePoint.toString())
                .setTaskID(taskID);
        this.channel.writeAndFlush(protocol);
        this.logger.info("�Ѿ��ɹ���Զ��cherry����� " + this.channel.remoteAddress() + " ������һ����ʱ����ɾ���������ڵȴ����������");
    }

}
