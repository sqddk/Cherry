package cn.cherry.server;

import cn.cherry.core.engine.utils.BaseUtils;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

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

    /**
     * 获取当前程序运行路径
     *
     * @return 程序运行路径
     */
    public static String getFilePath() {
        URL url = BaseUtils.class.getProtectionDomain().getCodeSource().getLocation();
        String filePath = null;
        try {
            filePath = URLDecoder.decode(url.getPath(), StandardCharsets.UTF_8);
            if (filePath.endsWith(".jar")) {
                filePath = filePath.substring(0, filePath.lastIndexOf("/") + 1);
            }
            filePath = new File(filePath).getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filePath;
    }

}
