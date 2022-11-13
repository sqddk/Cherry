package cn.cherry.server.service;

import cn.cherry.core.CherryLocalStarter;
import cn.cherry.core.ConfigLoader;
import cn.cherry.server.ServerConfigLoader;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
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
    private final Logger logger = LogManager.getLogger("Cherry");

    private CherryServer() {
        this.serverBootstrap = new ServerBootstrap();
        this.bossGroup = new NioEventLoopGroup(1);
        this.workerGroup = new NioEventLoopGroup();
    }

    /**
     * 初始化和启动 Netty socket 服务端
     */
    public void initial() {
        ConfigLoader configLoader = ConfigLoader.getInstance(ServerConfigLoader.class);
        String host = configLoader.getValue("host");
        int port = configLoader.getIntValue("port"),
            interval = configLoader.getIntValue("interval"),
            totalTicks = configLoader.getIntValue("totalTicks"),
            wheelTimeout = configLoader.getIntValue("wheelTimeout");

        new Thread(() -> {
            try {
                CherryLocalStarter.getInstance().initial(interval, totalTicks, wheelTimeout);
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

}
