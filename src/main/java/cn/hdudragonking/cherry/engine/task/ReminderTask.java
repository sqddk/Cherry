package cn.hdudragonking.cherry.engine.task;

import cn.hdudragonking.cherry.bootstrap.remote.protocol.CherryProtocol;
import cn.hdudragonking.cherry.engine.base.TimePoint;
import io.netty.channel.Channel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static cn.hdudragonking.cherry.bootstrap.remote.protocol.CherryProtocolFlag.*;

/**
 * ִ�������������������ϵĶ�ʱ�����ύ�߿���ִ��
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
     * ��ȡִ�е�ʱ���
     *
     * @return ʱ���
     */
    @Override
    public TimePoint getTimePoint() {
        return this.timePoint;
    }

    /**
     * ���������ID
     *
     * @param id ����ID
     */
    @Override
    public void setTaskID(int id) {
        this.taskID = id;
    }

    /**
     * ��ȡ�����ID
     *
     * @return ����ID
     */
    @Override
    public int getTaskID() {
        return this.taskID;
    }

    /**
     * ִ������
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
            this.logger.info(this.channel.remoteAddress() + " �Ķ�ʱ���� " + this.taskID + " ִ�гɹ���");
            return;
        }
        this.logger.info(this.channel.remoteAddress() + " �Ķ�ʱ���� " + this.taskID + " ִ��ʧ�ܣ�");
    }
}
