package cn.cherry.core.engine.utils;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;

public final class BaseUtils {

    private BaseUtils(){}

    /**
     * 检查字符串是否全为数字
     *
     * @param s 待检查的字符串
     * @return 是否全为数字
     */
    public static boolean checkStringOfNumber(String s){
        for(char c : s.toCharArray()){
            if(c < 48 || c > 57) {
                return false;
            }
        }
        return true;
    }

    /**
     * 打印出cherry的logo！
     */
    public static void printLogo() {
        System.out.println("\n" +
                "   _____   _                                  \n" +
                "  / ____| | |                                 \n" +
                " | |      | |__     ___   _ __   _ __   _   _ \n" +
                " | |      | '_ \\   / _ \\ | '__| | '__| | | | |\n" +
                " | |____  | | | | |  __/ | |    | |    | |_| |\n" +
                "  \\_____| |_| |_|  \\___| |_|    |_|     \\__, |\n" +
                "                                         __/ |\n" +
                "                                        |___/ \n");
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

    /**
     * 对int值进行检查，非正数时抛出异常
     *
     * @param value 待检查的int值
     * @param name 值的名称
     * @return 检查完毕的int值
     */
    public static int checkPositive(int value, String name) {
        if (value <= 0) throw new IllegalArgumentException(name);
        return value;
    }

    /**
     * 对long值进行检查，非正数时抛出异常
     *
     * @param value 待检查的long值
     * @param name 值的名称
     * @return 检查完毕的long值
     */
    public static long checkPositive(long value, String name) {
        if (value <= 0) throw new IllegalArgumentException(name);
        return value;
    }

}