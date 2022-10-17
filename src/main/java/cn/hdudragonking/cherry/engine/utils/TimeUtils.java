package cn.hdudragonking.cherry.engine.utils;

import cn.hdudragonking.cherry.engine.base.TimePoint;

import java.util.List;

public class TimeUtils {

    private TimeUtils(){}

    public static final List<Integer> BigMonthList = List.of(1, 3, 5 ,7, 8, 10, 12);

    /**
     * 将月份转化为数字，并检查是否有效（无效则返回-1）
     *
     * @param piece 月份字符串
     * @return 月份数字
     */
    public static int parseMonth(String piece){
        int month = Integer.parseInt(piece);
        return month > 12 ? -1 : month;
    }

    /**
     * 将月份内日期转化为数字，并检查日期是否有效（无效则返回-1）
     *
     * @param piece 日期字符串
     * @param year 年份数字
     * @param month 月份数字
     * @return 月份内日期数字
     */
    public static int parseDay(String piece, int year, int month){
        int day = Integer.parseInt(piece), maxDay;
        if(day == 0) return -1;
        if (month == 2) {
            maxDay = (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0) ? 29 : 28;
        } else if (BigMonthList.contains(month)) {
            maxDay = 31;
        } else {
            maxDay = 30;
        }
        return day > maxDay ? -1 : day ;
    }

    /**
     * 将小时转化为数字，并检查小时数是否有效（无效则返回-1）
     *
     * @param piece 小时数字符串
     * @return 小时数
     */
    public static int parseHour(String piece){
        int hour = Integer.parseInt(piece);
        return hour > 23 ? -1 : hour;
    }

    /**
     * 将分钟数转化为数字，并检查分钟数是否有效（无效则返回-1）
     *
     * @param piece 分钟数字符串
     * @return 分钟数
     */
    public static int parseMinute(String piece){
        int minute = Integer.parseInt(piece);
        return minute > 59 ? -1 : minute;
    }

    /**
     * 计算年内天数
     *
     * @param year 年份
     * @param month 月份
     * @param day 月内天数
     * @return 年内天数
     */
    public static int calDays(int year, int month, int day){
        int februaryDay = (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0) ? 29 : 28,
            sum = day;
        for (int i = 1; i < month; i++) {
            if (i == 2) {
                sum += februaryDay;
                continue;
            }
            sum += BigMonthList.contains(i) ? 31 : 30;
        }
        return sum;
    }

    /**
     * 对两个时间点的差值进行计算
     *
     * @param t1 时间点 1
     * @param t2 时间点 2
     * @return 差值
     */
    public static int calDifference(TimePoint t1, TimePoint t2) {
        return 0;
    }

}
