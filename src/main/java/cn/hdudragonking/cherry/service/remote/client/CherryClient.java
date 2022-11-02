package cn.hdudragonking.cherry.service.remote.client;

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

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static cn.hdudragonking.cherry.service.remote.CherryProtocolFlag.*;


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
    private final NioEventLoopGroup workerGroup;
    private final Logger logger = LogManager.getLogger("Cherry");
    private Channel channel;
    private String channelName;
    private final AtomicInteger monitor;
    private final Map<Integer, CompletableFuture<Integer>> addResultBucket;
    private final Map<Integer, CompletableFuture<Boolean>> removeResultBucket;

    private CherryClient() {
        this.bootstrap = new Bootstrap();
        this.workerGroup = new NioEventLoopGroup(2);
        this.monitor = new AtomicInteger(0);
        this.addResultBucket = new ConcurrentHashMap<>();
        this.removeResultBucket = new ConcurrentHashMap<>();
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
     * @param timeout 超时时间
     *
     * @return 任务编号（-1为提交失败）
     */
    public int submit(TimePoint timePoint, JSONObject metaData, int timeout) {
        if (this.channel == null) {
            this.logger.error("与远程cherry服务端的连接尚未建立！");
            return -1;
        }
        if (!this.channel.isActive()) {
            this.logger.error("与远程cherry服务端 " + this.channel.remoteAddress() + " 的连接不可用！");
            return -1;
        }
        final int sendingId = this.monitor.addAndGet(1);
        JSONObject protocol = new JSONObject(Map.of(
                        "channelName", this.channelName,
                        "flag", FLAG_ADD,
                        "metaData", metaData,
                        "timePoint", timePoint.toString(),
                        "sendingId", sendingId
                )
        );
        this.channel.writeAndFlush(protocol);
        this.logger.info("已经成功向远程cherry服务端 " + this.channel.remoteAddress() + " 提交了一个定时任务！正在等待操作结果！");
        final CompletableFuture<Integer> future = new CompletableFuture<>();
        this.addResultBucket.put(sendingId, future);
        try {
            return future.get(timeout, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            this.logger.error(e.getMessage());
            return -1;
        }
    }

    /**
     * 从远程cherry服务端中删除一个定时任务
     *
     * @param timePoint 时间点
     * @param taskID 任务ID
     * @param timeout 超时时间
     *
     * @return 任务编号（-1为删除失败）
     */
    public boolean remove(TimePoint timePoint, int taskID, int timeout) {
        if (this.channel == null) {
            this.logger.error("与远程cherry服务端的连接尚未建立！");
            return false;
        }
        if (!this.channel.isActive()) {
            this.logger.error("与远程cherry服务端 " + this.channel.remoteAddress() + " 的连接不可用！");
            return false;
        }
        final int sendingId = this.monitor.addAndGet(1);
        JSONObject protocol = new JSONObject(Map.of(
                        "channelName", this.channelName,
                        "flag", FLAG_REMOVE,
                        "taskId", taskID,
                        "timePoint", timePoint.toString(),
                        "sendingId", sendingId
                )
        );
        this.channel.writeAndFlush(protocol);
        this.logger.info("已经成功向远程cherry服务端 " + this.channel.remoteAddress() + " 发送了一个定时任务删除请求！正在等待操作结果！");
        final CompletableFuture<Boolean> future = new CompletableFuture<>();
        this.removeResultBucket.put(sendingId, future);
        try {
            return future.get(timeout, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            this.logger.error(e.getMessage());
            return false;
        }
    }

    /**
     * 接受远程cherry服务端的定时任务执行结果
     *
     * @param type 执行操作的类型
     * @param sendingId 发送序号
     * @param taskID 任务编号
     * @param result 执行结果
     */
    public void receiveInvokeResult(int type, int sendingId, Integer taskID, Boolean result) {
        
    }

}
