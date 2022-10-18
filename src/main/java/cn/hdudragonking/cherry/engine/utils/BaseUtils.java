package cn.hdudragonking.cherry.engine.utils;

import java.util.concurrent.*;

public final class BaseUtils {

    private BaseUtils(){}

    /**
     * ����ַ����Ƿ�ȫΪ����
     *
     * @param s �������ַ���
     * @return �Ƿ�ȫΪ����
     */
    public static boolean checkStringOfNumber(String s){
        for(char c : s.toCharArray()){
            if(c < 48 || c > 57) return false;
        }
        return true;
    }

    /**
     * ����һ������ִ��������̳߳�
     * <p>
     * ����ܾ�����Ӧ������д�ɣ�����֪ͨ���÷������޷���ִ��
     *
     * @param corePoolSize �����߳���
     * @param maximumPoolSize ����߳���
     * @param queueSize ���񻺳���г���
     * @return �̳߳�
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

}
