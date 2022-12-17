package cn.cherry.core.engine;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static io.netty.util.internal.ObjectUtil.checkPositive;
import static java.util.Calendar.*;
import static java.util.Objects.*;

/**
 * 时间转换器，内部封装了{@link Calendar}，用来把 "yyyyMMddhhmmSSsss" + 时区 格式的时间转换成距1970年格林尼治时间的绝对时间值，或者是反过来<br/>
 * {@link TimeParser}是非线程安全的，以后可以考虑优化资源复用模型和增加并发控制（在intel-i5的速度下，一次时间转换的开销在50-100微秒间）。
 *
 * @author realDragonKing
 */
public class TimeParser {

    private final Calendar calendar;

    public TimeParser() {
        this.calendar = new GregorianCalendar();
    }

    /**
     * 把 "yyyyMMddhhmmSSsss" + 时区 格式的时间转换成距1970年格林尼治时间的绝对时间值
     *
     * @param timeZone 时区
     * @param time 格式时间
     * @return 绝对时间值
     */
    public long time2timeValue(TimeZone timeZone, String time) throws IllegalArgumentException {
        requireNonNull(time, "time");
        requireNonNull(timeZone, "timeZone");

        char[] chars = time.toCharArray();
        if (chars.length != 17) {
            throw new IllegalArgumentException("格式时间不正确！");
        }

        Calendar calendar = this.calendar;
        calendar.setTimeZone(timeZone);
        calendar.set(YEAR, this.char2Int(chars, 0, 4));
        calendar.set(MONTH, this.char2Int(chars, 4, 6) - 1);
        calendar.set(DAY_OF_MONTH, this.char2Int(chars, 6, 8));
        calendar.set(HOUR_OF_DAY, this.char2Int(chars, 8, 10));
        calendar.set(MINUTE, this.char2Int(chars, 10, 12));
        calendar.set(SECOND, this.char2Int(chars, 12, 14));
        calendar.set(MILLISECOND, this.char2Int(chars, 14, 17));

        return calendar.getTimeInMillis();
    }

    /**
     * 把 "yyyyMMddhhmmSSsss" + 时区 格式的时间转换成距1970年格林尼治时间的绝对时间值
     *
     * @param timeZone 时区
     * @param year 年份
     * @param month 月份（1月为1）
     * @param dayOfMonth 月内日期
     * @param hourOfDay 小时
     * @param minute 分钟
     * @param second 秒
     * @param millisecond 毫秒
     * @return 绝对时间值
     */
    public long time2timeValue(TimeZone timeZone, int year, int month, int dayOfMonth,
                               int hourOfDay, int minute, int second, int millisecond) {
        requireNonNull(timeZone, "timeZone");

        checkPositive(year, "year");
        checkPositive(month, "month");
        checkPositive(dayOfMonth, "dayOfMonth");
        checkPositive(hourOfDay, "hourOfDay");
        checkPositive(minute, "minute");
        checkPositive(second, "second");
        checkPositive(millisecond, "millisecond");

        Calendar calendar = this.calendar;
        calendar.setTimeZone(timeZone);
        calendar.set(YEAR, year);
        calendar.set(MONTH, month - 1);
        calendar.set(DAY_OF_MONTH, dayOfMonth);
        calendar.set(HOUR_OF_DAY, hourOfDay);
        calendar.set(MINUTE, minute);
        calendar.set(SECOND, second);
        calendar.set(MILLISECOND, millisecond);

        return calendar.getTimeInMillis();
    }

    /**
     * 把距1970年格林尼治时间的绝对时间值转换成 "yyyyMMddhhmmSSsss" + 时区 格式的时间
     *
     * @param timeValue 绝对时间值
     * @param zone 时区
     * @return 格式时间
     */
    public String timeValue2time(long timeValue, TimeZone zone) {
        requireNonNull(zone, "zone");

        Calendar calendar = this.calendar;
        calendar.setTimeZone(zone);
        calendar.setTimeInMillis(timeValue);

        StringBuilder builder = new StringBuilder()
                .append(calendar.get(YEAR))
                .append(calendar.get(MONTH) + 1)
                .append(calendar.get(DAY_OF_MONTH))
                .append(calendar.get(HOUR_OF_DAY))
                .append(calendar.get(MINUTE))
                .append(calendar.get(SECOND))
                .append(calendar.get(MILLISECOND));

        return builder.toString();
    }

    /**
     * 把char数组中的一截转换成int值
     *
     * @param chars char数组
     * @param start 起始位置
     * @param end 结束位置
     * @return int值
     */
    private int char2Int(char[] chars, int start, int end) {
        int res = 0;
        int digital = 1;
        int num;
        for (int i = end - 1; i >= start; i--) {
            num = chars[i] - '0';
            if (num >= 0 & num <= 9) {
                res += num * digital;
                digital *= 10;
            } else
                throw new IllegalArgumentException("非字符数字！");
        }
        return res;
    }

}
