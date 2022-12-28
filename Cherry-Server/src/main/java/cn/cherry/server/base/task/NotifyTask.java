package cn.cherry.server.base.task;

import cn.cherry.core.engine.task.Task;
import cn.cherry.core.message.MessageType;
import com.alibaba.fastjson2.JSONObject;
import io.netty.channel.Channel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class NotifyTask implements Task {

    private final Channel channel;
    private final JSONObject taskData;
    private static final Logger logger = LogManager.getLogger("Cherry");

    public NotifyTask(Channel channel, JSONObject taskData) {
        this.channel = channel;
        this.taskData = taskData;
    }

    /**
     * @return 任务的配置信息
     */
    @Override
    public Map<String, Object> getTaskData() {
        return this.taskData;
    }

    /**
     * 执行任务
     */
    @Override
    public void run() {
        Channel channel = this.channel;
        if (channel.isActive()) {
            JSONObject data = new JSONObject();
            data.put("type", MessageType.NOTIFY);
            data.put("metaData", this.taskData);

            channel.writeAndFlush(data.toJSONString() + "\r\n");
        }
        else
            logger.error(channel.remoteAddress() + " 通信信道无效！无法执行回调通知！");
    }

}
