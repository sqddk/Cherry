package cn.cherry.client.service;

import cn.cherry.client.base.Receiver;
import cn.cherry.core.engine.base.TimePoint;
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

import static cn.cherry.core.infra.message.CommandFlag.*;


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

    private final Bootstrap bootstrap = new Bootstrap();
    private final NioEventLoopGroup workerGroup = new NioEventLoopGroup(2);
    private final Logger logger = LogManager.getLogger("Cherry");
    private Channel channel;
    private String groupName;
    private final AtomicInteger monitor = new AtomicInteger(0);
    private final Map<Integer, CompletableFuture<Integer>> addResultBucket = new ConcurrentHashMap<>();
    private final Map<Integer, CompletableFuture<Boolean>> removeResultBucket = new ConcurrentHashMap<>();

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
        this.groupName = channelName;
        CompletableFuture<Boolean> waiter = new CompletableFuture<>();
        new Thread(() -> {
            try {
                this.bootstrap
                        .channel(NioSocketChannel.class)
                        .group(this.workerGroup)
                        .option(ChannelOption.TCP_NODELAY, true)
                        .handler(new ClientInitializer(receiver));
                ChannelFuture future = this.bootstrap.connect(host, port).sync();
                this.channel = future.channel();
                this.logger.info("与远程cherry服务端 " + this.channel.remoteAddress() + " 的连接已经建立！");
                waiter.complete(true);
                this.channel.closeFuture().sync();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                this.workerGroup.shutdownGracefully();
            }
        }).start();
        try {
            waiter.get(500, TimeUnit.MILLISECONDS);
            return this;
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 向远程cherry服务端提交一个定时任务
     *
     * @param timePoint 时间点
     * @param metaData 元数据
     *
     * @return 任务编号（-1为提交失败）
     */
    public int submit(TimePoint timePoint, JSONObject metaData) {
        if (this.channel == null) {
            this.logger.error("与远程cherry服务端的连接尚未建立！");
            return -1;
        }
        if (!this.channel.isActive()) {
            this.logger.error("与远程cherry服务端 " + this.channel.remoteAddress() + " 的连接不可用！");
            return -1;
        }
        if (metaData == null) {
            this.logger.error("提交的元数据不能为null！");
            return -1;
        }
        final int sendingId = this.monitor.addAndGet(1);
        JSONObject protocol = new JSONObject(Map.of(
                        "groupName", this.groupName,
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
            return future.get(100, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            this.logger.error(e.toString());
            return -1;
        }
    }

    /**
     * 向远程cherry服务端提交一个定时任务（空元数据API）
     *
     * @param timePoint 时间点
     * @return 任务编号（-1为提交失败）
     */
    public int submit(TimePoint timePoint) {
        return this.submit(timePoint, new JSONObject());
    }

    /**
     * 从远程cherry服务端中删除一个定时任务
     *
     * @param timePoint 时间点
     * @param taskID 任务ID
     *
     * @return 任务编号（-1为删除失败）
     */
    public boolean remove(TimePoint timePoint, int taskID) {
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
                        "groupName", this.groupName,
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
            return future.get(100, TimeUnit.MILLISECONDS);
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
        switch (type) {
            case FLAG_RESULT_ADD :
                CompletableFuture<Integer> addFuture = this.addResultBucket.remove(sendingId);
                addFuture.complete(taskID);
                break;
            case FLAG_RESULT_REMOVE :
                CompletableFuture<Boolean> removeFuture = this.removeResultBucket.remove(sendingId);
                removeFuture.complete(result);
                break;
        }
    }

}
