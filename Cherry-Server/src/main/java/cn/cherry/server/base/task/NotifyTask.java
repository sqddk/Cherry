package cn.cherry.server.base.task;

import cn.cherry.core.engine.base.task.Task;
import cn.cherry.core.message.MessageType;
import com.alibaba.fastjson2.JSONObject;
import io.netty.channel.Channel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class NotifyTask implements Task {

    private final Channel channel;
    private final JSONObject config;
    private final Logger logger = LogManager.getLogger("Cherry");

    public NotifyTask(Channel channel, JSONObject config) {
        this.channel = channel;
        this.config = config;
    }

    /**
     * @return 任务的配置信息
     */
    @Override
    public Map<String, Object> getTaskConfig() {
        return this.config;
    }

    /**
     * 执行任务
     */
    @Override
    public void run() {
        JSONObject data = this.config;
        Channel channel = this.channel;

        data.put("flag", MessageType.NOTIFY);

        if (channel.isActive())
            channel.writeAndFlush(data.toJSONString() + '\n');
        else
            this.logger.error(channel.remoteAddress() + " 通信信道无效！无法执行回调通知！");
    }

}
