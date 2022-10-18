package cn.hdudragonking.cherry.engine.utils;

import cn.hdudragonking.cherry.engine.base.TimePoint;
import java.text.SimpleDateFormat;
import java.util.List;

public class TimeUtils {

    private TimeUtils(){}

    public static final SimpleDateFormat DateFormat = new SimpleDateFormat("yyyyMMddHHmm");
    public static final List<Integer> BigMonthList = List.of(1, 3, 5 ,7, 8, 10, 12);

    /**
     * ���·�ת��Ϊ���֣�������Ƿ���Ч����Ч�򷵻�-1��
     *
     * @param piece �·��ַ���
     * @return �·�����
     */
    public static int parseMonth(String piece){
        int month = Integer.parseInt(piece);
        return month > 12 ? -1 : month;
    }

    /**
     * ���·�������ת��Ϊ���֣�����������Ƿ���Ч����Ч�򷵻�-1��
     *
     * @param piece �����ַ���
     * @param year �������
     * @param month �·�����
     * @return �·�����������
     */
    public static int parseDay(String piece, int year, int month){
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
     * ��Сʱת��Ϊ���֣������Сʱ���Ƿ���Ч����Ч�򷵻�-1��
     *
     * @param piece Сʱ���ַ���
     * @return Сʱ��
     */
    public static int parseHour(String piece){
        int hour = Integer.parseInt(piece);
        return hour > 23 ? -1 : hour;
    }

    /**
     * ��������ת��Ϊ���֣������������Ƿ���Ч����Ч�򷵻�-1��
     *
     * @param piece �������ַ���
     * @return ������
     */
    public static int parseMinute(String piece){
        int minute = Integer.parseInt(piece);
        return minute > 59 ? -1 : minute;
    }

    /**
     * ��������{@link TimePoint}ʱ��㣬�������ǵ�ms��ֵ���Ҹ���ʱ��̶ȣ�ת��Ϊ��Ҫ�Ĳ�ֵ
     * <p>
     * ��ֵ����˳��Ϊ{@link TimePoint}P2 - {@link TimePoint}P1
     *
     * @return ��Ӧ��ʽ��ʱ���ֵ
     */
    public static int calDifference(TimePoint p1, TimePoint p2, int ticks) {
        return (int) ((p2.getTimeValue() - p1.getTimeValue()) / ticks);
    }

}
