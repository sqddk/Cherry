package cn.cherry.core.engine;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * {@link SpinLocker}是一种借助CAS操作类{@link AtomicBoolean}，完成的自旋锁实现
 *
 * @author realDragonKing
 */
public class SpinLocker {

    private final long waitTimeout;
    private final AtomicBoolean monitor;

    public SpinLocker(long waitTimeout) {
        this.waitTimeout = waitTimeout;
        this.monitor = new AtomicBoolean(false);
    }

    /**
     * 尝试获取到操作锁，若没获得则进行自旋，自旋超过时间{@link #waitTimeout}则返回锁获取失败的信息
     *
     * @return 是否获取锁成功
     */
    public boolean lock() {
        long endWaitingTime = System.nanoTime() + this.waitTimeout;
        AtomicBoolean monitor = this.monitor;
        while (!monitor.compareAndSet(false, true)) {
            if (System.nanoTime() > endWaitingTime) {
                return false;
            }
        }
        return true;
    }

    /**
     * 无条件释放锁
     */
    public void unLock() {
        this.monitor.set(false);
    }

}
