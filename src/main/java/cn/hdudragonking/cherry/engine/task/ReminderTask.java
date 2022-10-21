package cn.hdudragonking.cherry.engine.task;

import cn.hdudragonking.cherry.bootstrap.remote.protocol.CherryProtocol;
import cn.hdudragonking.cherry.engine.base.TimePoint;
import io.netty.channel.Channel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static cn.hdudragonking.cherry.bootstrap.remote.protocol.CherryProtocolFlag.*;

/**
 * 执行提醒任务，提醒网络上的定时任务提交者可以执行
 *
 * @since 2022/10/17
 * @author realDragonKing
 */
public class ReminderTask implements Task {

    private final TimePoint timePoint;
    private final Channel channel;
    private final String metaData;
    private int taskID;
    private final Logger logger = LogManager.getLogger("Cherry");

    public ReminderTask (TimePoint timePoint, String metaData, Channel channel) {
        this.timePoint = timePoint;
        this.metaData = metaData;
        this.channel = channel;
        this.taskID = -1;
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
        CherryProtocol protocol = new CherryProtocol();
        protocol.setFlag(FLAG_NOTIFY)
                .setStringTimePoint(this.timePoint.toString())
                .setMetaData(this.metaData)
                .setTaskID(String.valueOf(this.taskID));
        if (this.channel.isActive()) {
            this.channel.writeAndFlush(protocol);
            this.logger.info(this.channel.remoteAddress() + " 的定时任务 " + this.taskID + " 执行成功！");
            return;
        }
        this.logger.info(this.channel.remoteAddress() + " 的定时任务 " + this.taskID + " 执行失败！");
    }
}
