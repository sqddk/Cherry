package cn.hdudragonking.cherry;

import cn.hdudragonking.cherry.service.remote.server.CherryServer;

public class Main {

    public static void main(String[] args) {
        CherryServer.getInstance().initial();
    }

}
