package cn.cherry.core.engine;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static java.util.Calendar.*;
import static java.util.Objects.*;

/**
 * 时间转换器，内部封装了{@link Calendar}，用来把 "yyyyMMddhhmmSSsss" + 时区 格式的时间转换成距1970年格林尼治时间的绝对时间值，或者是反过来<br/>
 * {@link TimeParser}通过synchornized关键字实现了线程安全（在intel-i5的速度下，一次时间转换的开销在50-100微秒间）。
 * 以后可以考虑优化并发模型
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
     * @param time 格式时间
     * @param zone 时区
     * @return 绝对时间值
     */
    public synchronized long time2TimeValue(String time, TimeZone zone) throws IllegalArgumentException {
        requireNonNull(time, "time");
        requireNonNull(zone, "zone");

        char[] chars = time.toCharArray();
        if (chars.length != 17) {
            throw new IllegalArgumentException("格式时间不正确！");
        }

        Calendar calendar = this.calendar;
        calendar.setTimeZone(zone);
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
     * 把距1970年格林尼治时间的绝对时间值转换成 "yyyyMMddhhmmSSsss" + 时区 格式的时间
     *
     * @param timeValue 绝对时间值
     * @param zone 时区
     * @return 格式时间
     */
    public synchronized String timeValue2Time(long timeValue, TimeZone zone) {
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
