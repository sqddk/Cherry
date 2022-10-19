package cn.hdudragonking.cherry;

import cn.hdudragonking.cherry.bootstrap.remote.CherrySocketServer;


public class Main {

    public static void main(String[] args) {
        System.out.println("\n" +
                "   _____   _                                  \n" +
                "  / ____| | |                                 \n" +
                " | |      | |__     ___   _ __   _ __   _   _ \n" +
                " | |      | '_ \\   / _ \\ | '__| | '__| | | | |\n" +
                " | |____  | | | | |  __/ | |    | |    | |_| |\n" +
                "  \\_____| |_| |_|  \\___| |_|    |_|     \\__, |\n" +
                "                                         __/ |\n" +
                "                                        |___/ \n");
        CherrySocketServer.getInstance().initial("127.0.0.1", 4000);
    }

}
