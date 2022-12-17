package cn.cherry.client.base.handler;

import cn.cherry.client.base.RemoveResultReceiver;
import cn.cherry.core.message.MessageHandler;
import com.alibaba.fastjson2.JSONObject;
import io.netty.channel.Channel;

import java.util.Map;

public class RemoveResultHandler extends MessageHandler {

    private final Map<String, Map<Long, RemoveResultReceiver>> REMOVE_RESULT_MAP;

    public RemoveResultHandler(Map<String, Map<Long, RemoveResultReceiver>> REMOVE_RESULT_MAP) {
        this.REMOVE_RESULT_MAP = REMOVE_RESULT_MAP;
    }

    /**
     * 对{@link JSONObject}对象进行解析，然后进行处理
     *
     * @param channel 通信信道
     * @param jsonData    json数据
     */
    @Override
    protected void resolve(Channel channel, JSONObject jsonData) {
        String clientName = jsonData.getString("clientName");
        long publishId = jsonData.getLongValue("publishId");
        boolean isRemove = jsonData.getBooleanValue("isRemove");

        Map<Long, RemoveResultReceiver> receiverMap = this.REMOVE_RESULT_MAP.get(clientName);
        RemoveResultReceiver addResultReceiver = receiverMap.remove(publishId);
        addResultReceiver.doReceive(isRemove);
    }
}
