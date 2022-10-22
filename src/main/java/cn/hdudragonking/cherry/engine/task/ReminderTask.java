package cn.hdudragonking.cherry.engine.task;

import cn.hdudragonking.cherry.bootstrap.remote.protocol.CherryProtocol;
import cn.hdudragonking.cherry.bootstrap.remote.server.CherryServer;
import cn.hdudragonking.cherry.engine.base.TimePoint;
import com.alibaba.fastjson2.JSON;
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
    private final String channelName;
    private final String metaData;
    private int taskID;
    private final Logger logger = LogManager.getLogger("Cherry");

    public ReminderTask (String channelName, TimePoint timePoint, String metaData) {
        this.channelName = channelName;
        this.timePoint = timePoint;
        this.metaData = metaData;
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
        CherryProtocol protocol = new CherryProtocol(FLAG_NOTIFY);
        protocol.setStringTimePoint(this.timePoint.toString())
                .setMetaData(JSON.parseObject(this.metaData))
                .setTaskID(this.taskID);
        Channel channel = CherryServer.getInstance().getChannelMap().get(channelName);
        if (channel != null && channel.isActive()) {
            channel.writeAndFlush(protocol);
            this.logger.info(this.channelName + " �Ķ�ʱ���� " + this.taskID + " ִ��֪ͨ�ɹ���");
            return;
        }
        this.logger.info(this.channelName + " �Ķ�ʱ���� " + this.taskID + " ִ��֪ͨʧ�ܣ�");
    }
}
