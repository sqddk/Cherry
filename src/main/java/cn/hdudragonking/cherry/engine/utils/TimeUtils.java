package cn.hdudragonking.cherry.engine.utils;

import cn.hdudragonking.cherry.engine.base.TimePoint;

import java.util.List;

public class TimeUtils {

    private TimeUtils(){}

    /**
     * 有31天的月份列表
     */
    public static final List<Integer> BigMonthList = List.of(1, 3, 5 ,7, 8, 10, 12);

    /**
     * 将月份转化为数字，并检查是否有效（无效则返回-1）
     *
     * @param piece 月份字符串
     * @return 月份数字
     */
    public static int parseAndCheckMonth(String piece){
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
    public static int parseAndCheckDay(String piece, int year, int month){
        int day = Integer.parseInt(piece);
        int maxDay;
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
    public static int parseAndCheckHour(String piece){
        int hour = Integer.parseInt(piece);
        return hour > 23 ? -1 : hour;
    }

    /**
     * 将分钟数转化为数字，并检查分钟数是否有效（无效则返回-1）
     *
     * @param piece 分钟数字符串
     * @return 分钟数
     */
    public static int parseAndCheckMinute(String piece){
        int minute = Integer.parseInt(piece);
        return minute > 59 ? -1 : minute;
    }

    /**
     * 根据两个{@link TimePoint}时间点，计算他们的ms差值并且根据时间刻度，转换为需要的差值
     * <p>
     * 差值计算顺序为{@link TimePoint}P2 - {@link TimePoint}P1
     *
     * @return 对应格式的时间差值
     */
    public static int calDifference(TimePoint p1, TimePoint p2, int ticks) {
        return (int) ((p2.getTimeValue() - p1.getTimeValue()) / ticks);
    }

}
