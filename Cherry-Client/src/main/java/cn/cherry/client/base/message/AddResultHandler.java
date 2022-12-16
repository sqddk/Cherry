package cn.cherry.client.base.message;

import cn.cherry.core.infra.message.MessageType;
import cn.cherry.core.infra.message.MessageHandler;
import cn.cherry.core.infra.message.HandlerTag;
import com.alibaba.fastjson2.JSONObject;
import io.netty.channel.Channel;

@HandlerTag(type = MessageType.ADD_RESULT)
public class AddResultHandler extends MessageHandler {

    /**
     * 对{@link JSONObject}对象进行解析，然后进行处理
     *
     * @param channel 通信信道
     * @param data    json数据
     */
    @Override
    protected void resolve(Channel channel, JSONObject data) {

    }
}
