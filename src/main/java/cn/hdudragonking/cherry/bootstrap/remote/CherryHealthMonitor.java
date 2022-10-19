package cn.hdudragonking.cherry.bootstrap.remote;

import io.netty.channel.Channel;

/**
 * cherry网络通信层面的连接心跳机制监视者，负责响应ping/pong
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
     * 接收ping心跳帧，服务端检测客户端是否断连
     *
     * @param channel 与客户端的通信管道
     */
    public void acceptPing(Channel channel) {

    }

    /**
     * 接收pong心跳帧，客户端检测服务端是否断连
     *
     * @param channel 与服务端的通信管道
     */
    public void acceptPong(Channel channel) {

    }

}
