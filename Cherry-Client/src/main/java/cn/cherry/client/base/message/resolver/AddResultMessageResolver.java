package cn.cherry.client.base.message.resolver;

import cn.cherry.client.base.message.AddResultMessage;
import cn.cherry.core.infra.message.Message;
import cn.cherry.core.infra.message.MessageFlag;
import cn.cherry.core.infra.message.MessageResolver;
import cn.cherry.core.infra.message.MessageType;
import com.alibaba.fastjson2.JSONObject;


@MessageType(flag = MessageFlag.ADD_RESULT)
public class AddResultMessageResolver extends MessageResolver {

    /**
     * 对{@link JSONObject}对象进行解析，解析出{@link Message}
     *
     * @param data json数据
     * @return {@link Message}
     */
    @Override
    protected Message resolve(JSONObject data) {
        return new AddResultMessage();
    }

}
