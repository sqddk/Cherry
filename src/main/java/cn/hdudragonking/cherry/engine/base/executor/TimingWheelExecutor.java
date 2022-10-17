package cn.hdudragonking.cherry.engine.base.executor;

import cn.hdudragonking.cherry.engine.base.TimingWheel;

import java.util.concurrent.BlockingQueue;

/**
 * 时间轮操作者，（实现了{@link java.lang.Runnable}接口），在独立线程中运行
 * <p>
 * 通过阻塞队列收到来自计时器的通知信息后，会解除阻塞状态，执行{@link TimingWheel#turn()}方法，
 * 推动时间轮转动，把任务分配到干活的线程池里面具体执行
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
