package cn.hdudragonking.cherry.engine.task;

import cn.hdudragonking.cherry.bootstrap.remote.protocol.CherryProtocol;
import cn.hdudragonking.cherry.engine.base.TimePoint;
import io.netty.channel.Channel;

/**
 * ִ�������������������ϵĶ�ʱ�����ύ�߿���ִ��
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
     * ��ȡִ�е�ʱ���
     *
     * @return ʱ���
     */
    @Override
    public TimePoint getTimePoint() {
        return this.timePoint;
    }

    /**
     * ִ������
     */
    @Override
    public void execute() {
        CherryProtocol protocol = new CherryProtocol();
    }
}
