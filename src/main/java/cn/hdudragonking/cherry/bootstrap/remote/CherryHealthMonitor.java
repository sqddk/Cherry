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

    public void acceptPing(Channel channel) {

    }

}
