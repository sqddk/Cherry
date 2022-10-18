package cn.hdudragonking.cherry.engine.task;

import cn.hdudragonking.cherry.engine.base.TimePoint;

/**
 * ִ�������������������ϵĶ�ʱ�����ύ�߿���ִ��
 *
 * @since 2022/10/17
 * @author realDragonKing
 */
public class ReminderTask implements Task {

    private final TimePoint timePoint;

    public ReminderTask (TimePoint timePoint) {
        this.timePoint = timePoint;
    }

    /**
     * ��ȡִ�е�ʱ���
     *
     * @return ʱ���
     */
    @Override
    public TimePoint getTimePoint() {
        return this.timePoint;
    }

    /**
     * ִ������
     */
    @Override
    public void execute() {

    }
}
