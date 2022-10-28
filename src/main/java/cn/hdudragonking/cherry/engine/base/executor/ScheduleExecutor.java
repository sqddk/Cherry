package cn.hdudragonking.cherry.engine.base.executor;

import cn.hdudragonking.cherry.engine.base.TimingWheel;

import java.util.concurrent.BlockingQueue;

/**
 * 计时器（实现了{@link java.lang.Runnable}接口），在独立线程中运行
 * <p>
 * 计时器会通过自旋的方式实现 休眠-执行 的轮回（休眠时间由传入的参数决定，单位为秒）
 * <p>
 * 每次执行都会通过阻塞队列传入元素的方式，通知运行在另外一个线程上的时间轮操作者
 * {@link TimingWheelExecutor}执行{@link TimingWheel#turn()}方法
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
