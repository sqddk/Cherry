package cn.cherry.client.base.handler;

import cn.cherry.client.base.AddResultReceiver;
import cn.cherry.core.message.MessageHandler;
import com.alibaba.fastjson2.JSONObject;
import io.netty.channel.Channel;

import java.util.Map;

public class AddResultHandler extends MessageHandler {

    private final Map<String, Map<Long, AddResultReceiver>> ADD_RESULT_MAP;

    public AddResultHandler(Map<String, Map<Long, AddResultReceiver>> ADD_RESULT_MAP) {
        this.ADD_RESULT_MAP = ADD_RESULT_MAP;
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
        long taskId = jsonData.getLongValue("taskId");

        Map<Long, AddResultReceiver> receiverMap = this.ADD_RESULT_MAP.get(clientName);
        AddResultReceiver addResultReceiver = receiverMap.remove(publishId);
        addResultReceiver.doReceive(taskId);
    }

}
