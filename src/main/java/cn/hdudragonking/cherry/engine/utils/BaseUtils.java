package cn.hdudragonking.cherry.engine.utils;

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

}
