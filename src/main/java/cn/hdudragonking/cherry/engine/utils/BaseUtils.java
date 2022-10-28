package cn.hdudragonking.cherry.engine.utils;

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
            if(c < 48 || c > 57) return false;
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

}