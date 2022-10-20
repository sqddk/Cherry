package cn.hdudragonking.cherry.engine.base.executor;

import cn.hdudragonking.cherry.engine.base.TimingWheel;

import java.util.concurrent.BlockingQueue;

/**
 * ��ʱ����ʵ����{@link java.lang.Runnable}�ӿڣ����ڶ����߳�������
 * <p>
 * ��ʱ����ͨ�������ķ�ʽʵ�� ����-ִ�� ���ֻأ�����ʱ���ɴ���Ĳ�����������λΪ�룩
 * <p>
 * ÿ��ִ�ж���ͨ���������д���Ԫ�صķ�ʽ��֪ͨ����������һ���߳��ϵ�ʱ���ֲ�����
 * {@link TimingWheelExecutor}ִ��{@link TimingWheel#turn()}����
 *
 * @since 2022/10/17
 * @author realDragonKing
 */
public class ScheduleExecutor implements Runnable {

    private final int interval;
    private final BlockingQueue<Integer> messageChannel;

    public ScheduleExecutor(int interval, BlockingQueue<Integer> messageChannel) {
        this.interval = interval;
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
                Thread.sleep(this.interval);
                this.messageChannel.offer(1);
            } catch (Exception e) {
                break;
            }
        }
    }

}
