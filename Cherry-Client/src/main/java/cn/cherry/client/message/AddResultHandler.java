package cn.cherry.client.message;

import cn.cherry.core.message.MessageHandler;
import com.alibaba.fastjson2.JSONObject;
import io.netty.channel.Channel;

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
