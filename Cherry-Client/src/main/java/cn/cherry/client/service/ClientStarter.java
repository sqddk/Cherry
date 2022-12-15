package cn.cherry.client.service;

import cn.cherry.client.base.Receiver;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.*;

/**
 * cherry定时任务调度引擎的 socket 网络服务客户端启动引导类
 *
 * @since 2022/10/19
 * @author realDragonKing
 */
public class ClientStarter {

    private final static class ClientStarterHolder {
        private final static ClientStarter INSTANCE = new ClientStarter();
    }
    public static ClientStarter getInstance() {
        return ClientStarterHolder.INSTANCE;
    }
    private final Logger logger = LogManager.getLogger("Cherry");
    private Channel channel;

    private ClientStarter() {}

    /**
     * 通过提供的host地址和端口，初始化客户端，尝试连接目标服务端
     *
     * @param channelName 客户端名称，用于消费任务通知
     * @param host host地址
     * @param port port端口
     * @param receiver 响应接收/执行者
     * @return this
     */
    public ClientStarter initial(String channelName, String host, int port, Receiver receiver) {
        CompletableFuture<Boolean> waiter = new CompletableFuture<>();

        new Thread(() -> {
            Bootstrap bootstrap = new Bootstrap();
            EventLoopGroup workerGroup = new NioEventLoopGroup(2);

            try {
                bootstrap.group(workerGroup)
                        .channel(NioSocketChannel.class)
                        .option(ChannelOption.TCP_NODELAY, true)
                        .handler(new ClientInitializer());

                ChannelFuture future = bootstrap.connect(host, port).sync();

                this.channel = future.channel();
                this.logger.info("与远程cherry服务端 " + this.channel.remoteAddress() + " 的连接已经建立！");
                waiter.complete(true);

                this.channel.closeFuture().sync();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                workerGroup.shutdownGracefully();
            }
        }).start();

        try {
            waiter.get(500, TimeUnit.MILLISECONDS);
            return this;
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

}
