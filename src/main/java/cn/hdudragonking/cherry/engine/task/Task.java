package cn.hdudragonking.cherry.engine.task;

import cn.hdudragonking.cherry.engine.base.TimePoint;

/**
 * �ɱ�ִ�еĶ�ʱ����Ľӿ�
 *
 * @since 2022/10/17
 * @author realDragonKing
 */
public interface Task {

    /**
     * ��ȡִ�е�ʱ���
     *
     * @return ʱ���
     */
    TimePoint getTimePoint();

    /**
     * ִ������
     */
    void execute();

}
