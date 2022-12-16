package cn.cherry.server.base.message;

import cn.cherry.core.engine.base.TimeSlot;
import cn.cherry.core.engine.base.TimingWheel;
import cn.cherry.core.infra.message.MessageType;
import cn.cherry.core.infra.message.MessageHandler;
import cn.cherry.core.infra.message.HandlerTag;
import cn.cherry.server.base.task.NotifyTask;
import cn.cherry.server.service.ServerStarter;
import com.alibaba.fastjson2.JSONObject;
import io.netty.channel.Channel;


@HandlerTag(type = MessageType.ADD)
public class AddHandler extends MessageHandler {

    private final TimingWheel timingWheel;

    public AddHandler() {
         this.timingWheel = ServerStarter.getInstance().getTimingWheel();
    }

    /**
     * 对{@link JSONObject}对象进行解析，然后进行处理
     *
     * @param channel 通信信道
     * @param data    json数据
     */
    @Override
    protected void resolve(Channel channel, JSONObject data) {
        long executeTimeValue = data.getLongValue("executeTime");
        long publishId = data.getLongValue("publishId");

        TimingWheel timingWheel = this.timingWheel;
        long distance = timingWheel.calDistance(executeTimeValue);
        TimeSlot slot = timingWheel.getSlot(distance);

        long taskId = slot.submitTask(new NotifyTask(channel, data), distance);

        JSONObject result = new JSONObject(4);
        result.put("flag", MessageType.ADD_RESULT);
        result.put("publishId", publishId);
        if (taskId == -1) {
            result.put("result", false);
        } else {
            result.put("result", true);
            result.put("taskId", taskId);
        }
        channel.writeAndFlush(result.toJSONString() + '\n');
    }

}
