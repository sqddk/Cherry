package cn.cherry.server.base.message;

import cn.cherry.core.engine.base.TimingWheel;
import cn.cherry.core.infra.message.MessageType;
import cn.cherry.core.infra.message.MessageHandler;
import cn.cherry.core.infra.message.MessageTypeTag;
import cn.cherry.server.service.ServerStarter;
import com.alibaba.fastjson2.JSONObject;


@MessageTypeTag(type = MessageType.ADD)
public class AddMessageHandler extends MessageHandler {

    private final TimingWheel timingWheel;

    public AddMessageHandler() {
         this.timingWheel = ServerStarter.getInstance().getTimingWheel();
    }

    /**
     * 对{@link JSONObject}对象进行解析，然后进行处理
     *
     * @param data json数据
     */
    @Override
    protected void resolve(JSONObject data) {

    }
}
