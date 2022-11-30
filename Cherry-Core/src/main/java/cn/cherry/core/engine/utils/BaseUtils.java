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
     * 创建一个具体执行任务的线程池
     * <p>
     * 这里拒绝策略应当被重写成，可以通知调用方任务无法被执行
     *
     * @param corePoolSize 核心线程数
     * @param maximumPoolSize 最大线程数
     * @param queueSize 任务缓冲队列长度
     * @return 线程池
     */
    public static ExecutorService createWorkerThreadPool(int corePoolSize, int maximumPoolSize, int queueSize) {
        return new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                2L,
                TimeUnit.MINUTES,
                new LinkedBlockingQueue<>(queueSize),
                (r, executor) -> {});
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