package cn.cherry.server.base.message;

import cn.cherry.core.engine.base.TimeSlot;
import cn.cherry.core.engine.base.TimingWheel;
import cn.cherry.core.infra.message.MessageHandler;
import cn.cherry.core.infra.message.MessageType;
import com.alibaba.fastjson2.JSONObject;
import io.netty.channel.Channel;

import org.apache.logging.log4j.Logger;

public class RemoveHandler extends MessageHandler {

    private final TimingWheel timingWheel;
    private final Logger logger;

    public RemoveHandler(TimingWheel timingWheel, Logger logger) {
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
        long taskId = data.getLongValue("taskId");
        long executeTimeValue = data.getLongValue("executeTime");

        TimingWheel timingWheel = this.timingWheel;
        long distance = timingWheel.calDistance(executeTimeValue);
        TimeSlot slot = timingWheel.getSlot(distance);

        boolean isRemove = slot.removeTask(taskId, distance);

        data.put("flag", MessageType.REMOVE_RESULT);
        data.put("result", isRemove);
        data.remove("taskId");
        data.remove("executeTime");

        if (isRemove) {
            this.logger.info(channel.remoteAddress() + " 任务删除成功！");
        } else {
            this.logger.info(channel.remoteAddress() + " 任务删除失败！");
        }

        channel.writeAndFlush(data.toJSONString() + '\n');
    }

}
