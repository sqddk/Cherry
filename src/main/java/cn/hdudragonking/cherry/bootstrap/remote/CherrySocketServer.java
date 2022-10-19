package cn.hdudragonking.cherry.bootstrap.remote;

import cn.hdudragonking.cherry.bootstrap.CherryLocalStarter;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * cherry��ʱ������������ socket �����������������
 *
 * @since 2022/10/18
 * @author realDragonKing
 */
public class CherrySocketServer {
    private final static class CherrySocketServerHolder {
        private final static CherrySocketServer INSTANCE = new CherrySocketServer();
    }
    public static CherrySocketServer getInstance() {
        return CherrySocketServerHolder.INSTANCE;
    }

    private final ServerBootstrap serverBootstrap;
    private final NioEventLoopGroup bossGroup;
    private final NioEventLoopGroup workerGroup;
    private final Logger logger = LogManager.getLogger("Cherry");

    private CherrySocketServer() {
        this.serverBootstrap = new ServerBootstrap();
        this.bossGroup = new NioEventLoopGroup(1);
        this.workerGroup = new NioEventLoopGroup();
    }

    /**
     * ͨ���ṩ��host��ַ�Ͷ˿ڣ���ʼ�������� Netty socket �����
     *
     * @param host host��ַ
     * @param port port�˿�
     */
    public void initial(String host, int port) {
        try {
            CherryLocalStarter.getInstance().initial();
            this.serverBootstrap
                    .channel(NioServerSocketChannel.class)
                    .group(this.bossGroup, this.workerGroup)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childHandler(new CherrySocketInitializer());
            ChannelFuture future = this.serverBootstrap.bind(host, port).sync();
            this.logger.info("������Ѿ��� " + String.format("%s:%s", host, port) + " �ϳɹ����������ṩ����");
            this.logger.info("�ȴ��ͻ������ӽ��뱾����ˣ�");
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.bossGroup.shutdownGracefully();
            this.workerGroup.shutdownGracefully();
        }
    }

}
