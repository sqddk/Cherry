package cn.cherry.client.base.message;

import cn.cherry.core.infra.message.MessageType;
import cn.cherry.core.infra.message.MessageHandler;
import cn.cherry.core.infra.message.MessageTypeTag;
import com.alibaba.fastjson2.JSONObject;

@MessageTypeTag(type = MessageType.ADD_RESULT)
public class AddResultMessageHandler extends MessageHandler {

    /**
     * 对{@link JSONObject}对象进行解析，然后进行处理
     *
     * @param data json数据
     */
    @Override
    protected void resolve(JSONObject data) {

    }
}
