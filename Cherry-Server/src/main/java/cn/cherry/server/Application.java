package cn.cherry.server;

import cn.cherry.server.service.CherryServer;

public class Application {

    public static void main(String[] args) {
        CherryServer.getInstance().initial();
    }

}
