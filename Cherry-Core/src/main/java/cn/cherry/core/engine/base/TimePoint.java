package cn.cherry.core.engine.base;


import cn.cherry.core.engine.utils.BaseUtils;
import cn.cherry.core.engine.utils.TimeUtils;

import java.util.Calendar;

/**
 * 时间点，结合{@link Calendar}完成时间信息的转换
 *
 * @since 2022/10/17
 * @author realDragonKing
 */
public class TimePoint {

    /**
     * 根据字符串时间，创建一个时间点对象，并进行严格校验
     *
     * @param time String格式时间，格式为“yyyyMMddHHmm”
     * @return 时间点
     */
    public static TimePoint parse(String time) {
        if (time == null || time.length() != 12 || (! BaseUtils.checkStringOfNumber(time))) {
            return null;
        }
        int year, month, day, hour, minute;
        year = Integer.parseInt(time.substring(0, 4));
        month = TimeUtils.parseAndCheckMonth(time.substring(4, 6));
        if (month == -1) {
            return null;
        }
        day = TimeUtils.parseAndCheckDay(time.substring(6, 8), year, month);
        hour = TimeUtils.parseAndCheckHour(time.substring(8, 10));
        minute = TimeUtils.parseAndCheckMinute(time.substring(10, 12));
        if(day == -1 || hour == -1 || minute == -1){
            return null;
        }
        // TODO 以后可以搞一个Calender的资源池，在线程安全的前提下保障多calender并行处理时间，提高速度
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day, hour, minute);
        return new TimePoint(calendar);
    }

    /**
     * 根据时间值，创建一个时间点对象
     *
     * @param timeValue 时间值，单位为毫秒（ms）
     * @return 时间点对象
     */
    public static TimePoint parse(long timeValue) {
        // TODO 以后可以搞一个Calender的资源池，在线程安全的前提下保障多calender并行处理时间，提高速度
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeValue);
        return new TimePoint(calendar);
    }

    /**
     * 生成当前的时间点
     *
     * @return 当前时间点
     */
    public static TimePoint getCurrentTimePoint() {
        return TimePoint.parse(System.currentTimeMillis());
    }

    private TimePoint(Calendar calendar){
        this.timeString = String.format("%04d%02d%02d%02d%02d",
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE));
        this.timeValue = calendar.getTimeInMillis();
    }

    /**
     * 当前时间点的绝对时间值，单位为毫秒（ms）
     */
    private final long timeValue;

    /**
     * String格式时间，格式为“yyyyMMddHHmm”
     */
    private final String timeString;

    /**
     * 返回当前时间点的绝对时间值，单位为毫秒（ms）
     *
     * @return 绝对时间值
     */
    public final long getTimeValue() {
        return this.timeValue;
    }

    /**
     * 返回String格式时间，格式为“yyyyMMddHHmm”
     *
     * @return String格式时间
     */
    @Override
    public final String toString(){
        return this.timeString;
    }

}