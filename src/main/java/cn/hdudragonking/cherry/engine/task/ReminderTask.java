package cn.hdudragonking.cherry.engine.task;

import cn.hdudragonking.cherry.bootstrap.remote.protocol.CherryProtocol;
import cn.hdudragonking.cherry.engine.base.TimePoint;
import io.netty.channel.Channel;

/**
 * 执行提醒任务，提醒网络上的定时任务提交者可以执行
 *
 * @since 2022/10/17
 * @author realDragonKing
 */
public class ReminderTask implements Task {

    private final TimePoint timePoint;
    private final Channel channel;

    public ReminderTask (TimePoint timePoint, Channel channel) {
        this.timePoint = timePoint;
        this.channel = channel;
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
     * 执行任务
     */
    @Override
    public void execute() {
        CherryProtocol protocol = new CherryProtocol();
    }
}
