package cn.cherry.core.message;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;


/**
 * 消息处理器的顶层抽象类，能够把{@link ByteBuf}解析和转换为{@link JSONObject}后进行处理
 *
 * @author realDragonKing
 */
public abstract class MessageHandler {

    private static final Map<Integer, MessageHandler> HANDLER_MAP = new HashMap<>();

    /**
     * 注册一个消息处理器
     *
     * @param messageType 消息类型标志
     * @param handler 消息处理器
     */
    public static void registerHandler(int messageType, MessageHandler handler) {
        requireNonNull(handler, "MessageHandler");
        HANDLER_MAP.put(messageType, handler);
    }

    /**
     * 尝试对接收到、准备解码的数据传输对象{@link ByteBuf}进行消息处理器{@link MessageHandler}匹配和处理
     *
     * @param channel 通信信道
     * @param data 数据
     */
    public static void tryResolve(Channel channel, String data) {
        try {
            JSONObject jsonData = JSON.parseObject(data);
            Integer type = jsonData.getInteger("type");
            if (type != null) {
                MessageHandler resolver = HANDLER_MAP.get(type);
                if (resolver != null)
                    resolver.resolve(channel, jsonData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 对{@link JSONObject}对象进行解析，然后进行处理
     *
     * @param channel 通信信道
     * @param data json数据
     */
    protected abstract void resolve(Channel channel, JSONObject data);

}
