package cn.cherry.server;

import cn.cherry.server.service.ServerStarter;

public class Application {

    public static void main(String[] args) {
        ServerStarter.getInstance().initial();
    }

}
