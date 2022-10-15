package cn.hdudragonking.cherry.utils;

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

}
