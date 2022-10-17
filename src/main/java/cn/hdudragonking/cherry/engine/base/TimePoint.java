package cn.hdudragonking.cherry.engine.base;

import cn.hdudragonking.cherry.engine.utils.BaseUtils;
import cn.hdudragonking.cherry.engine.utils.TimeUtils;

/**
 * 时间点
 *
 * @since 2022/10/17
 * @author realDragonKing
 */
public class TimePoint {

    /**
     * 根据字符串时间，创建一个时间点对象，并进行严格校验
     *
     * @param time String格式时间
     * @return 时间点
     */
    public static TimePoint parse(String time) {
        if (time.length() != 12 || (! BaseUtils.checkStringOfNumber(time))) {
            return null;
        }
        int year, month, day, hour, minute;
        year = Integer.parseInt(time.substring(0, 4));
        month = TimeUtils.parseMonth(time.substring(4, 6));
        if (month == -1) {
            return null;
        }
        day = TimeUtils.parseDay(time.substring(6, 8), year, month);
        if (day == -1) {
            return null;
        }
        hour = TimeUtils.parseHour(time.substring(8, 10));
        minute = TimeUtils.parseMinute(time.substring(10, 12));
        if(hour == -1 || minute == -1){
            return null;
        }
        return new TimePoint(year, month, day, hour, minute);
    }

    private TimePoint(int year, int month, int day, int hour, int minute){
        this.timeUnitStorage = new int[]{year, month, day, hour, minute};
    }

    /**
     * 时间信息存储单元
     */
    private final int[] timeUnitStorage;

    /**
     * 获取年份
     *
     * @return 年份
     */
    public final int getYear(){
        return timeUnitStorage[0];
    }

    /**
     * 获取月份
     *
     * @return 月份
     */
    public final int getMonth(){
        return timeUnitStorage[1];
    }

    /**
     * 获取月份内天数
     *
     * @return 月份内天数
     */
    public final int getDay(){
        return timeUnitStorage[2];
    }

    /**
     * 获取小时数
     *
     * @return 获取小时数
     */
    public final int getHour(){
        return timeUnitStorage[3];
    }

    /**
     * 获取分钟数
     *
     * @return 分钟数
     */
    public final int getMinute(){
        return timeUnitStorage[4];
    }

    /**
     * 获取年内天数
     *
     * @return 年内天数
     */
    public final int getDaysOfYear(){
        return TimeUtils.calDays(getYear(), getMonth(), getDay());
    }

    /**
     * 返回字符串型时间数据
     *
     * @return 字符串型时间数据
     */
    @Override
    public final String toString(){
        return String.format("%04d%02d%02d%02d%02d",
                this.timeUnitStorage[0],
                this.timeUnitStorage[1],
                this.timeUnitStorage[2],
                this.timeUnitStorage[3],
                this.timeUnitStorage[4]);
    }

    /**
     * 返回长整型时间数据
     *
     * @return 长整型时间数据
     */
    public final long toLong(){
        return Long.parseLong(this.toString());
    }

}
