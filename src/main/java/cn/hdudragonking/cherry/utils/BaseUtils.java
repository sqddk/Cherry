package cn.hdudragonking.cherry.utils;

import cn.hdudragonking.cherry.base.Node;

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
     * 对两个节点进行互相绑定
     *
     * @param previous 前节点
     * @param next 后节点
     */
    public static void bind(Node previous, Node next){
        previous.setNext(next);
        next.setPrevious(previous);
    }

    /**
     * 对两个节点进行解绑
     *
     * @param previous 前节点
     * @param next 后节点
     */
    public static void unBind(Node previous, Node next){
        previous.setNext(null);
        next.setPrevious(null);
    }

}
