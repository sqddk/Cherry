package cn.hdudragonking.cherry.bootstrap.remote.client;

import cn.hdudragonking.cherry.bootstrap.remote.protocol.CherryProtocol;
import cn.hdudragonking.cherry.engine.base.TimePoint;
import com.alibaba.fastjson2.JSONObject;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Executors;

import static cn.hdudragonking.cherry.bootstrap.remote.protocol.CherryProtocolFlag.*;

/**
 * cherry定时任务调度引擎的 socket 网络服务客户端启动引导类
 *
 * @since 2022/10/19
 * @author realDragonKing
 */
public class CherryClient {

    private final static class SocketClientHolder {
        private final static CherryClient INSTANCE = new CherryClient();
    }
    public static CherryClient getInstance() {
        return SocketClientHolder.INSTANCE;
    }

    private final Bootstrap bootstrap;
    private Channel channel;
    private String channelName;
    private final NioEventLoopGroup workerGroup;
    private final Logger logger = LogManager.getLogger("Cherry");

    private CherryClient() {
        this.bootstrap = new Bootstrap();
        this.workerGroup = new NioEventLoopGroup(2);
    }

    /**
     * 通过提供的host地址和端口，初始化客户端，尝试连接目标服务端
     *
     * @param channelName 客户端名称，用于消费任务通知
     * @param host host地址
     * @param port port端口
     * @param clientReceiver 响应接收/执行者
     */
    public void initial(String channelName, String host, int port, ClientReceiver clientReceiver) {
        this.channelName = channelName;
        Executors.newSingleThreadExecutor().submit(() -> {
            try {
                this.bootstrap
                        .channel(NioSocketChannel.class)
                        .group(this.workerGroup)
                        .option(ChannelOption.TCP_NODELAY, true)
                        .handler(new ClientInitializer(clientReceiver));
                ChannelFuture future = this.bootstrap.connect(host, port).sync();
                this.channel = future.channel();
                this.logger.info("与远程cherry服务端 " + this.channel.remoteAddress() + " 的连接已经建立！");
                this.channel.closeFuture().sync();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                this.workerGroup.shutdownGracefully();
            }
        });
    }

    /**
     * 向远程cherry服务端提交一个定时任务
     *
     * @param timePoint 时间点
     * @param metaData 元数据
     */
    public void submit(TimePoint timePoint, JSONObject metaData) {
        if (this.channel == null) {
            this.logger.error("与远程cherry服务端的连接尚未建立！");
            return;
        }
        if (!this.channel.isActive()) {
            this.logger.error("与远程cherry服务端 " + this.channel.remoteAddress() + " 的连接不可用！");
            return;
        }
        CherryProtocol protocol = new CherryProtocol(FLAG_ADD)
                .setChannelName(this.channelName)
                .setStringTimePoint(timePoint.toString())
                .setMetaData(metaData);
        this.channel.writeAndFlush(protocol);
        this.logger.info("已经成功向远程cherry服务端 " + this.channel.remoteAddress() + " 提交了一个定时任务！正在等待操作结果！");
    }

    /**
     * 从远程cherry服务端中删除一个定时任务
     *
     * @param timePoint 时间点
     * @param taskID 任务ID
     */
    public void remove(TimePoint timePoint, int taskID) {
        if (this.channel == null) {
            this.logger.error("与远程cherry服务端的连接尚未建立！");
            return;
        }
        if (!this.channel.isActive()) {
            this.logger.error("与远程cherry服务端 " + this.channel.remoteAddress() + " 的连接不可用！");
            return;
        }
        CherryProtocol protocol = new CherryProtocol(FLAG_REMOVE)
                .setChannelName(this.channelName)
                .setStringTimePoint(timePoint.toString())
                .setTaskID(taskID);
        this.channel.writeAndFlush(protocol);
        this.logger.info("已经成功向远程cherry服务端 " + this.channel.remoteAddress() + " 发送了一个定时任务删除请求！正在等待操作结果！");
    }

}
