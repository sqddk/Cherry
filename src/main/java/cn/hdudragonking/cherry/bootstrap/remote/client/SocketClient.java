package cn.hdudragonking.cherry.bootstrap.remote.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * cherry定时任务调度引擎的 socket 网络服务客户端启动引导类
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
     * 通过提供的host地址和端口，初始化客户端，尝试连接目标服务端
     *
     * @param host host地址
     * @param port port端口
     * @param receiver 响应接收/执行者
     */
    public void initial(String host, int port, Receiver receiver) {
        try {
            this.bootstrap
                    .channel(NioSocketChannel.class)
                    .group(this.workerGroup)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ClientInitializer(receiver));
            ChannelFuture future = this.bootstrap.connect(host, port).sync();
            this.logger.info("与cherry服务端 " + future.channel().remoteAddress() + " 的连接已经建立！");
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.workerGroup.shutdownGracefully();
        }
    }

}
