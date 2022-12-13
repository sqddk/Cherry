package cn.cherry.server.base.message.resolver;

import cn.cherry.core.infra.message.Message;
import cn.cherry.core.infra.message.MessageType;
import cn.cherry.core.infra.message.MessageResolver;
import cn.cherry.core.infra.message.MessageTypeTag;
import cn.cherry.server.base.message.AddMessage;
import com.alibaba.fastjson2.JSONObject;


@MessageTypeTag(flag = MessageType.ADD)
public class AddMessageResolver extends MessageResolver {

    /**
     * 对{@link JSONObject}对象进行解析，解析出{@link Message}
     *
     * @param data json数据
     * @return {@link Message}
     */
    @Override
    protected Message resolve(JSONObject data) {
        return new AddMessage();
    }
}
