package cn.hdudragonking.cherry.engine.base.executor;

import cn.hdudragonking.cherry.engine.base.TimingWheel;

import java.util.concurrent.BlockingQueue;

/**
 * ʱ���ֲ����ߣ���ʵ����{@link java.lang.Runnable}�ӿڣ����ڶ����߳�������
 * <p>
 * ͨ�����������յ����Լ�ʱ����֪ͨ��Ϣ�󣬻�������״̬��ִ��{@link TimingWheel#turn()}������
 * �ƶ�ʱ����ת������������䵽�ɻ���̳߳��������ִ��
 *
 * @since 2022/10/17
 * @author realDragonKing
 */
public class TimingWheelExecutor implements Runnable {

    private final BlockingQueue<Integer> messageChannel;
    private final TimingWheel timingWheel;

    public TimingWheelExecutor(TimingWheel timingWheel, BlockingQueue<Integer> messageChannel) {
        this.timingWheel = timingWheel;
        this.messageChannel = messageChannel;
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        for (;;) {
            try {
                this.messageChannel.take();
                this.timingWheel.turn();
            } catch (Exception e) {
                break;
            }
        }
    }

}
