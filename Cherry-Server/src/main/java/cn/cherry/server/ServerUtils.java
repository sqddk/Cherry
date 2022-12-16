package cn.cherry.server;

public class ServerUtils {

    private ServerUtils() {}

    /**
     * 打印出cherry的logo！
     */
    public static String createLogo() {
        StringBuilder logo = new StringBuilder()
                .append('\n')
                .append("   _____   _                                  \n")
                .append("  / ____| | |                                 \n")
                .append(" | |      | |__     ___   _ __   _ __   _   _ \n")
                .append(" | |      | '_ \\   / _ \\ | '__| | '__| | | | |\n")
                .append(" | |____  | | | | |  __/ | |    | |    | |_| |\n")
                .append("  \\_____| |_| |_|  \\___| |_|    |_|     \\__, |\n")
                .append("                                         __/ |\n")
                .append("                                        |___/ \n");
        return logo.toString();
    }

}
