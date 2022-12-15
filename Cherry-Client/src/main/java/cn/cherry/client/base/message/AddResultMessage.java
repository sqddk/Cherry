package cn.cherry.client.base.message;

import cn.cherry.core.infra.message.Message;
import cn.cherry.core.infra.message.MessageType;

public class AddResultMessage implements Message {

    public AddResultMessage() {

    }

    /**
     * 获取到{@link Message}的类型标志位
     *
     * @return 类型标志位
     */
    @Override
    public int getType() {
        return MessageType.ADD_RESULT;
    }

    /**
     * 处理{@link Message}消息体的待执行任务
     */
    @Override
    public void handle() {

    }

}
