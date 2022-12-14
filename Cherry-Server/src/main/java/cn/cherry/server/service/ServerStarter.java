package cn.cherry.server.service;

import cn.cherry.core.engine.base.DefaultTimingWheel;
import cn.cherry.core.engine.base.TimingWheel;
import cn.cherry.core.infra.ConfigLoader;
import cn.cherry.core.infra.message.MessageAccepter;
import cn.cherry.server.base.ServerConfigLoader;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * cherry定时任务调度引擎的 socket 网络服务启动引导类
 *
 * @author realDragonKing
 */
public class ServerStarter {

    private final static class ServerStarterHolder {
        private final static ServerStarter INSTANCE = new ServerStarter();
    }
    public static ServerStarter getInstance() {
        return ServerStarterHolder.INSTANCE;
    }

    private static final String resolverPackageName = "cn.cherry.server.base.message.resolver";
    private static final String handlerPackageName = "cn.cherry.server.base.message.handler";
    private final Logger logger = LogManager.getLogger("Cherry");
    private TimingWheel timingWheel;

    private ServerStarter() {}

    /**
     * 初始化和启动 Netty socket 服务端
     */
    public void initial() {
        ConfigLoader configLoader = ConfigLoader.getInstance(ServerConfigLoader.class);
        this.logger.info("配置文件初始化完成！");

        String host = configLoader.getValue("host");
        int port = configLoader.getIntValue("port");
        int interval = configLoader.getIntValue("interval");
        int totalTicks = configLoader.getIntValue("totalTicks");
        int waitTimeout = configLoader.getIntValue("waitTimeout");
        int taskSize = configLoader.getIntValue("taskSize");
        int minThreadNumber = configLoader.getIntValue("minThreadNumber");
        int maxThreadNumber = configLoader.getIntValue("maxThreadNumber");

        this.timingWheel = new DefaultTimingWheel(interval, totalTicks, waitTimeout, taskSize, minThreadNumber, maxThreadNumber);
        this.logger.info("Cherry-Core调度引擎已经在本地成功启动并可提供服务！");

        MessageAccepter.tryLoad(resolverPackageName, handlerPackageName);
        this.logger.info("消息解析器和消息执行器已经被全部加载！");

        ServerBootstrap bootstrap = new ServerBootstrap();
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childHandler(new ServerInitializer());

            SocketAddress localAddress = new InetSocketAddress(host, port);
            ChannelFuture future = bootstrap.bind(localAddress).sync();

            this.logger.info("服务端已经在 " + localAddress + " 上成功启动并可提供服务！");
            this.logger.info("等待cherry客户端连接接入本服务端！");

            future.channel().closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    /**
     * @return 服务端启动的时间轮
     */
    public TimingWheel getTimingWheel() {
        TimingWheel timingWheel = this.timingWheel;
        if (timingWheel == null)
            throw new NullPointerException("TimingWheel尚未被初始化！请等待服务端完成初始化！");
        return timingWheel;
    }

}
