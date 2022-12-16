package cn.cherry.server.service;

import cn.cherry.core.engine.base.DefaultTimingWheel;
import cn.cherry.core.engine.base.TimingWheel;
import cn.cherry.core.message.MessageType;
import cn.cherry.server.ServerUtils;
import cn.cherry.server.base.ConfigLoader;
import cn.cherry.server.base.message.AddHandler;
import cn.cherry.server.base.message.RemoveHandler;
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

import static cn.cherry.core.message.MessageHandler.registerHandler;

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

    private ServerStarter() {}

    /**
     * 初始化和启动 Netty socket 服务端
     */
    public void initial() {
        Logger logger = LogManager.getLogger("Cherry");
        logger.info(ServerUtils.createLogo());

        // 初始化读取配置
        ConfigLoader configLoader = ConfigLoader.getInstance();
        logger.info("配置文件初始化完成！");

        String host = configLoader.getValue("host");
        int port = configLoader.getIntValue("port");
        int interval = configLoader.getIntValue("interval");
        int totalTicks = configLoader.getIntValue("totalTicks");
        int waitTimeout = configLoader.getIntValue("waitTimeout");
        int taskSize = configLoader.getIntValue("taskSize");
        int minThreadNumber = configLoader.getIntValue("minThreadNumber");
        int maxThreadNumber = configLoader.getIntValue("maxThreadNumber");

        // 启动时间轮
        TimingWheel timingWheel = new DefaultTimingWheel(interval, totalTicks, waitTimeout, taskSize, minThreadNumber, maxThreadNumber);
        logger.info("Cherry-Core调度引擎已经在本地成功启动并可提供服务！");

        // 注册消息处理器
        registerHandler(MessageType.ADD, new AddHandler(timingWheel, logger));
        registerHandler(MessageType.REMOVE, new RemoveHandler(timingWheel, logger));
        logger.info("消息解析器已经被全部加载！");

        // 启动Netty网络通信支持
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

            logger.info("服务端已经在 " + localAddress + " 上成功启动并可提供服务！");
            logger.info("等待Cherry客户端连接接入本服务端！");

            future.channel().closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
