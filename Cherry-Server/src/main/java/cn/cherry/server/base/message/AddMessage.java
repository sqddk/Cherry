package cn.cherry.server.base.message;

import cn.cherry.core.engine.base.TimingWheel;
import cn.cherry.core.engine.base.task.Task;
import cn.cherry.core.infra.message.Message;
import cn.cherry.core.infra.message.MessageType;
import io.netty.channel.Channel;


public class AddMessage implements Message {

    private final Channel channel;
    private final Task task;
    private final TimingWheel timingWheel;

    public AddMessage(Channel channel, Task task, TimingWheel timingWheel) {
        this.channel = channel;
        this.task = task;
        this.timingWheel = timingWheel;
    }

    /**
     * 获取到{@link Message}的类型标志位
     *
     * @return 类型标志位
     */
    @Override
    public int getType() {
        return MessageType.ADD;
    }

    /**
     * 处理{@link Message}消息体的待执行任务
     */
    @Override
    public void handle() {

    }

}
