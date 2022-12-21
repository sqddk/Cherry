package cn.cherry.client.service;

import cn.cherry.client.base.AddResultReceiver;
import cn.cherry.client.base.RemoveResultReceiver;
import cn.cherry.client.base.handler.AddResultHandler;
import cn.cherry.client.base.handler.RemoveResultHandler;
import cn.cherry.core.message.MessageType;
import com.alibaba.fastjson2.JSONObject;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

import static cn.cherry.core.message.MessageHandler.registerHandler;


/**
 * 与远程cherry定时任务调度服务端进行交互的客户端
 *
 * @author realDragonKing
 */
public class CherryClient {

    private static final Map<String, Map<Long, AddResultReceiver>> ADD_RESULT_MAP;
    private static final Map<String, Map<Long, RemoveResultReceiver>> REMOVE_RESULT_MAP;

    private final Logger logger;
    private final AtomicLong publishMonitor;
    private final String clientName;
    private final long timeout;
    private Channel channel;

    static {
        // 初始化客户端全局对象
        ADD_RESULT_MAP = new HashMap<>();
        REMOVE_RESULT_MAP = new HashMap<>();

        // 注册消息处理器
        registerHandler(MessageType.ADD_RESULT, new AddResultHandler(ADD_RESULT_MAP));
        registerHandler(MessageType.REMOVE_RESULT, new RemoveResultHandler(REMOVE_RESULT_MAP));
    }

    public CherryClient(String clientName, long timeout) {
        CherryClient.ADD_RESULT_MAP.put(clientName, new ConcurrentHashMap<>());
        CherryClient.REMOVE_RESULT_MAP.put(clientName, new ConcurrentHashMap<>());

        this.logger = LogManager.getLogger("Cherry");
        this.publishMonitor = new AtomicLong(1);

        this.clientName = clientName;
        this.timeout = timeout;
    }

    /**
     * 通过提供的host地址和端口，初始化客户端，尝试连接目标服务端
     *
     * @param host host地址
     * @param port port端口
     */
    public void connect(String host, int port) {
        CompletableFuture<Channel> waiter = new CompletableFuture<>();

        Thread thread = new Thread(() -> connect0(host, port, waiter));
        thread.start();

        try {
            this.channel = waiter.get(this.timeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            this.logger.error("连接目标服务端因超时失败！");
        }
    }

    /**
     * 通过提供的host地址和端口，初始化客户端，尝试连接目标服务端
     *
     * @param host host地址
     * @param port port端口
     * @param waiter 超时等候器
     */
    private void connect0(String host, int port, CompletableFuture<Channel> waiter) {
        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup workerGroup = new NioEventLoopGroup(2);

        try {
            bootstrap.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ClientInitializer());

            ChannelFuture future = bootstrap.connect(host, port).sync();

            Channel channel = future.channel();
            waiter.complete(channel);
            this.logger.info("与远程cherry服务端 " + channel.remoteAddress() + " 的连接已经建立！");

            channel.closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    /**
     * 发送待响应信息（临时api，等待更好的实现）
     *
     * @param jsonData 待发送数据
     */
    public void addTask(JSONObject jsonData, AddResultReceiver addResultReceiver) {
        long publishId = this.processMessage(jsonData);
        Map<Long, AddResultReceiver> receiverMap = CherryClient.ADD_RESULT_MAP.get(clientName);
        receiverMap.put(publishId, addResultReceiver);

        String msg = jsonData.toJSONString() + "\r\n";
        this.checkChannel().writeAndFlush(msg);
    }

    /**
     * 删除一个任务
     *
     * @param jsonData 待发送数据
     */
    public void removeTask(JSONObject jsonData, RemoveResultReceiver removeResultReceiver) {
        long publishId = this.processMessage(jsonData);
        Map<Long, RemoveResultReceiver> receiverMap = CherryClient.REMOVE_RESULT_MAP.get(clientName);
        receiverMap.put(publishId, removeResultReceiver);

        String msg = jsonData.toJSONString() + "\r\n";
        this.checkChannel().writeAndFlush(msg);
    }

    /**
     * 对消息进行加工，注入客户端名称和发布顺序id
     *
     * @param jsonData 待发送数据
     * @return 发布顺序id
     */
    private long processMessage(JSONObject jsonData) {
        String clientName = this.clientName;
        long publishId = this.publishMonitor.getAndIncrement();
        jsonData.put("clientName", clientName);
        jsonData.put("publishId", publishId);
        return publishId;
    }

    /**
     * 对通信信道进行检查
     * @return 通信信道
     */
    private Channel checkChannel() {
        Channel channel = this.channel;
        if (channel == null || !channel.isActive())
            throw new NullPointerException("与服务端的通信信道不可用！");
        else
            return channel;
    }

}
