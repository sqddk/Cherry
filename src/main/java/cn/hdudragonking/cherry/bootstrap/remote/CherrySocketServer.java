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
 * cherry定时任务调度引擎的 socket 网络服务启动引导类
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
     * 通过提供的host地址和端口，初始化和启动 Netty socket 服务端
     *
     * @param host host地址
     * @param port port端口
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
            this.logger.info("服务端已经在 " + String.format("%s:%s", host, port) + " 上成功启动并可提供服务！");
            this.logger.info("等待客户端连接接入本服务端！");
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.bossGroup.shutdownGracefully();
            this.workerGroup.shutdownGracefully();
        }
    }

}
