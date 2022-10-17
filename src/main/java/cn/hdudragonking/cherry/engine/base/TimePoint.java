package cn.hdudragonking.cherry.engine.base;

import cn.hdudragonking.cherry.engine.utils.BaseUtils;
import cn.hdudragonking.cherry.engine.utils.TimeUtils;

/**
 * ʱ���
 *
 * @since 2022/10/17
 * @author realDragonKing
 */
public class TimePoint {

    /**
     * �����ַ���ʱ�䣬����һ��ʱ�����󣬲������ϸ�У��
     *
     * @param time String��ʽʱ��
     * @return ʱ���
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
     * ʱ����Ϣ�洢��Ԫ
     */
    private final int[] timeUnitStorage;

    /**
     * ��ȡ���
     *
     * @return ���
     */
    public final int getYear(){
        return timeUnitStorage[0];
    }

    /**
     * ��ȡ�·�
     *
     * @return �·�
     */
    public final int getMonth(){
        return timeUnitStorage[1];
    }

    /**
     * ��ȡ�·�������
     *
     * @return �·�������
     */
    public final int getDay(){
        return timeUnitStorage[2];
    }

    /**
     * ��ȡСʱ��
     *
     * @return ��ȡСʱ��
     */
    public final int getHour(){
        return timeUnitStorage[3];
    }

    /**
     * ��ȡ������
     *
     * @return ������
     */
    public final int getMinute(){
        return timeUnitStorage[4];
    }

    /**
     * ��ȡ��������
     *
     * @return ��������
     */
    public final int getDaysOfYear(){
        return TimeUtils.calDays(getYear(), getMonth(), getDay());
    }

    /**
     * �����ַ�����ʱ������
     *
     * @return �ַ�����ʱ������
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
     * ���س�����ʱ������
     *
     * @return ������ʱ������
     */
    public final long toLong(){
        return Long.parseLong(this.toString());
    }

}
