package cn.hdudragonking.cherry.bootstrap.remote.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * cherry��ʱ������������ socket �������ͻ�������������
 *
 * @since 2022/10/19
 * @author realDragonKing
 */
public class SocketClient {

    private final static class SocketClientHolder {
        private final static SocketClient INSTANCE = new SocketClient();
    }
    public static SocketClient getInstance() {
        return SocketClientHolder.INSTANCE;
    }

    private final Bootstrap bootstrap;
    private final NioEventLoopGroup workerGroup;
    private final Logger logger = LogManager.getLogger("Cherry");

    private SocketClient() {
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
        try {
            this.bootstrap
                    .channel(NioSocketChannel.class)
                    .group(this.workerGroup)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ClientInitializer(receiver));
            ChannelFuture future = this.bootstrap.connect(host, port).sync();
            this.logger.info("��cherry����� " + future.channel().remoteAddress() + " �������Ѿ�������");
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.workerGroup.shutdownGracefully();
        }
    }

}
