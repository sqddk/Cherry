package cn.hdudragonking.cherry.engine.base;

import cn.hdudragonking.cherry.engine.utils.BaseUtils;
import cn.hdudragonking.cherry.engine.utils.TimeUtils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
        if (time == null || time.length() != 12 || (! BaseUtils.checkStringOfNumber(time))) {
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

    /**
     * 根据{@link Calendar}接口的实现类生成时间点
     *
     * @return 当前时间点
     */
    public static TimePoint getCurrentTimePoint() {
        Calendar calendar = new GregorianCalendar();
        return new TimePoint(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE));
    }

    private TimePoint(int year, int month, int day, int hour, int minute){
        this.timeUnitStorage = new int[]{year, month, day, hour, minute};
        try {
            Date time = TimeUtils.DateFormat.parse(this.toString());
            this.timeValue = time.getTime();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 时间信息存储单元
     */
    private final int[] timeUnitStorage;
    private final long timeValue;

    /**
     * 返回当前时间点的绝对时间
     *
     * @return 绝对时间值
     */
    public final long getTimeValue() {
        return this.timeValue;
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

}
