package cn.hdudragonking.cherry.bootstrap.remote.server;

import cn.hdudragonking.cherry.bootstrap.CherryLocalStarter;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

/**
 * cherry��ʱ������������ socket �����������������
 *
 * @since 2022/10/18
 * @author realDragonKing
 */
public class CherryServer {

    private final static class CherrySocketServerHolder {
        private final static CherryServer INSTANCE = new CherryServer();
    }
    public static CherryServer getInstance() {
        return CherrySocketServerHolder.INSTANCE;
    }

    private final ServerBootstrap serverBootstrap;
    private final NioEventLoopGroup bossGroup;
    private final NioEventLoopGroup workerGroup;
    private final ConcurrentHashMap<String, Channel> channelMap;
    private final Logger logger = LogManager.getLogger("Cherry");

    private CherryServer() {
        this.serverBootstrap = new ServerBootstrap();
        this.bossGroup = new NioEventLoopGroup(1);
        this.workerGroup = new NioEventLoopGroup();
        this.channelMap = new ConcurrentHashMap<>();
    }

    /**
     * ͨ���ṩ��host��ַ�Ͷ˿ڣ���ʼ�������� Netty socket �����
     *
     * @param host host��ַ
     * @param port port�˿�
     */
    public void initial(String host, int port) {
        Executors.newSingleThreadExecutor().submit(() -> {
            try {
                CherryLocalStarter.getInstance().initial();
                this.serverBootstrap
                        .channel(NioServerSocketChannel.class)
                        .group(this.bossGroup, this.workerGroup)
                        .childOption(ChannelOption.TCP_NODELAY, true)
                        .childHandler(new ServerInitializer());
                ChannelFuture future = this.serverBootstrap.bind(host, port).sync();
                this.logger.info("������Ѿ��� " + future.channel().remoteAddress() + " �ϳɹ����������ṩ����");
                this.logger.info("�ȴ�cherry�ͻ������ӽ��뱾����ˣ�");
                future.channel().closeFuture().sync();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                this.bossGroup.shutdownGracefully();
                this.workerGroup.shutdownGracefully();
            }
        });
    }

    /**
     * ��ȡ������˶�ͨ�Źܵ��Ĵ洢����
     *
     * @return ͨ�Źܵ��Ĵ洢����
     */
    public ConcurrentHashMap<String, Channel> getChannelMap() {
        return this.channelMap;
    }

}
