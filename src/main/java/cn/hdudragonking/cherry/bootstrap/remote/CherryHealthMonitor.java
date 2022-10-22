package cn.hdudragonking.cherry.bootstrap.remote;

import io.netty.channel.Channel;

/**
 * cherry����ͨ�Ų���������������Ƽ����ߣ�������Ӧping/pong
 *
 * @since 2022/10/18
 * @author realDragonKing
 */
public class CherryHealthMonitor {

    private final static class CherryHealthMonitorHolder {
        private final static CherryHealthMonitor INSTANCE = new CherryHealthMonitor();
    }
    public static CherryHealthMonitor getInstance() {
        return CherryHealthMonitorHolder.INSTANCE;
    }

    private CherryHealthMonitor() {}

    /**
     * ����ping����֡������˼��ͻ����Ƿ����
     *
     * @param channelName �ͻ��˵�����
     */
    public void acceptPing(String channelName) {

    }

    /**
     * ����pong����֡���ͻ��˼�������Ƿ����
     *
     * @param channel �����˵�ͨ�Źܵ�
     */
    public void acceptPong(Channel channel) {

    }

}
