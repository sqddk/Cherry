package cn.hdudragonking.cherry.service.remote.server;

import cn.hdudragonking.cherry.service.CherryLocalStarter;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

/**
 * cherry定时任务调度引擎的 socket 网络服务启动引导类
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
     * 通过提供的host地址和端口，初始化和启动 Netty socket 服务端
     *
     * @param host host地址
     * @param port port端口
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
                SocketAddress localAddress = new InetSocketAddress(host, port);
                ChannelFuture future = this.serverBootstrap.bind(localAddress).sync();
                this.logger.info("服务端已经在 " + localAddress + " 上成功启动并可提供服务！");
                this.logger.info("等待cherry客户端连接接入本服务端！");
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
     * 获取到服务端对通信管道的存储容器
     *
     * @return 通信管道的存储容器
     */
    public ConcurrentHashMap<String, Channel> getChannelMap() {
        return this.channelMap;
    }

}
