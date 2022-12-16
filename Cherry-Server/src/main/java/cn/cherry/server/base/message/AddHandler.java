package cn.cherry.server.base.message;

import cn.cherry.core.engine.TimeSlot;
import cn.cherry.core.engine.TimingWheel;
import cn.cherry.core.message.MessageType;
import cn.cherry.core.message.MessageHandler;
import cn.cherry.server.base.task.NotifyTask;
import com.alibaba.fastjson2.JSONObject;
import io.netty.channel.Channel;
import org.apache.logging.log4j.Logger;

public class AddHandler extends MessageHandler {

    private final TimingWheel timingWheel;
    private final Logger logger;

    public AddHandler(TimingWheel timingWheel, Logger logger) {
         this.timingWheel = timingWheel;
         this.logger = logger;
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

        JSONObject result = new JSONObject();
        result.put("flag", MessageType.ADD_RESULT);
        result.put("publishId", publishId);

        if (taskId == -1) {
            result.put("result", false);
            this.logger.error(channel.remoteAddress() + " 任务提交失败！");
        } else {
            result.put("result", true);
            result.put("taskId", taskId);
            this.logger.info(channel.remoteAddress() + " 任务提交成功！");
        }

        channel.writeAndFlush(result.toJSONString() + '\n');
    }

}
