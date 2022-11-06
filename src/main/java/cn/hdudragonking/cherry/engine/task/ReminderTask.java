package cn.hdudragonking.cherry.engine.task;

import cn.hdudragonking.cherry.service.remote.server.CherryServer;
import cn.hdudragonking.cherry.engine.base.TimePoint;
import com.alibaba.fastjson2.JSONObject;
import io.netty.channel.Channel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

import static cn.hdudragonking.cherry.service.remote.CherryProtocolFlag.*;

/**
 * 执行提醒任务，提醒网络上的定时任务提交者可以执行
 *
 * @since 2022/10/17
 * @author realDragonKing
 */
public class ReminderTask implements Task {

    private final TimePoint timePoint;
    private final String channelName;
    private final String metaData;
    private int taskID;
    private final Logger logger = LogManager.getLogger("Cherry");

    public ReminderTask (String channelName, TimePoint timePoint, String metaData) {
        this.channelName = channelName;
        this.timePoint = timePoint;
        this.metaData = metaData;
    }

    /**
     * 获取执行的时间点
     *
     * @return 时间点
     */
    @Override
    public TimePoint getTimePoint() {
        return this.timePoint;
    }

    /**
     * 设置任务的ID
     *
     * @param id 任务ID
     */
    @Override
    public void setTaskID(int id) {
        this.taskID = id;
    }

    /**
     * 获取任务的ID
     *
     * @return 任务ID
     */
    @Override
    public int getTaskID() {
        return this.taskID;
    }

    /**
     * 执行任务
     */
    @Override
    public void execute() {
        JSONObject protocol = new JSONObject(Map.of(
                "flag", FLAG_NOTIFY,
                "metaData", metaData,
                "taskId", taskID,
                "timePoint", timePoint.toString()
        ));
        Map<String, Channel> channelBucket = CherryServer.getInstance().getChannelMap();
        Channel channel = channelBucket.get(channelName);
        if (channel != null && channel.isActive()) {
            channel.writeAndFlush(protocol);
            this.logger.info(this.channelName + " 的定时任务 " + this.taskID + " 执行通知成功！");
            return;
        }
        this.logger.info(this.channelName + " 的定时任务 " + this.taskID + " 执行通知失败！");
    }
}
