package cn.cherry.client.service;

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

import static io.netty.util.internal.ObjectUtil.checkPositive;

/**
 * 与远程cherry定时任务调度服务端进行交互的客户端
 *
 * @author realDragonKing
 */
public class CherryClient {

    private final Logger logger;
    private Channel channel;

    public CherryClient(String host, int port, long timeout) {
        this.logger = LogManager.getLogger("Cherry");
        this.connect(host, port, timeout);
    }

    /**
     * 通过提供的host地址和端口，初始化客户端，尝试连接目标服务端
     *
     * @param host host地址
     * @param port port端口
     * @param timeout 连接超时时间
     */
    private void connect(String host, int port, long timeout) {
        checkPositive(timeout, "timeout");

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
            waiter.get(timeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            this.logger.error("连接目标服务端超时失败！");
        }
    }

}
