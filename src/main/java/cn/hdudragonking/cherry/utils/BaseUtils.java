package cn.hdudragonking.cherry.utils;

import cn.hdudragonking.cherry.base.Node;

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
     * �������ڵ���л����
     *
     * @param previous ǰ�ڵ�
     * @param next ��ڵ�
     */
    public static void bind(Node previous, Node next){
        previous.setNext(next);
        next.setPrevious(previous);
    }

    /**
     * �������ڵ���н��
     *
     * @param previous ǰ�ڵ�
     * @param next ��ڵ�
     */
    public static void unBind(Node previous, Node next){
        previous.setNext(null);
        next.setPrevious(null);
    }

}
