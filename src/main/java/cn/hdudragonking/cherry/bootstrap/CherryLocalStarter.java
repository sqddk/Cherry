package cn.hdudragonking.cherry.bootstrap;

import cn.hdudragonking.cherry.bootstrap.remote.CherrySocketServer;
import cn.hdudragonking.cherry.engine.base.DefaultTimingWheel;
import cn.hdudragonking.cherry.engine.base.TimePoint;
import cn.hdudragonking.cherry.engine.base.TimingWheel;
import cn.hdudragonking.cherry.engine.base.executor.ScheduleExecutor;
import cn.hdudragonking.cherry.engine.base.executor.TimingWheelExecutor;
import cn.hdudragonking.cherry.engine.task.Task;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * cherry定时任务调度引擎的本地启动引导类
 * <p>
 * 可以在这里启动本地服务和 socket 网络服务
 * {@link CherrySocketServer}
 *
 * @since 2022/10/17
 * @author realDragonKing
 */
public class CherryLocalStarter {
    private static final class CherryLocalStarterHolder{
        private static final CherryLocalStarter INSTANCE = new CherryLocalStarter();
    }
    public static CherryLocalStarter getInstance() {
        return CherryLocalStarterHolder.INSTANCE;
    }
    private CherryLocalStarter() {}

    private TimingWheel timingWheel;
    private final static int DEFAULT_INTERVAL = 60000;

    /**
     * 为cherry引擎的启动进行初始化
     */
    public void initial() {
        ExecutorService threadPool = Executors.newFixedThreadPool(2);
        this.timingWheel = new DefaultTimingWheel(DEFAULT_INTERVAL);
        BlockingQueue<Integer> blockingQueue = new LinkedBlockingQueue<>(1);
        threadPool.submit(new ScheduleExecutor(DEFAULT_INTERVAL, blockingQueue));
        threadPool.submit(new TimingWheelExecutor(this.timingWheel, blockingQueue));
    }

    /**
     * 通过本地进程内API，向时间轮中提交任务
     *
     * @param task 待执行的任务
     */
    public void submit(Task task) {
        this.timingWheel.submit(task);
    }

    /**
     * 根据任务执行时间点和任务唯一ID，通过本地进程内API，向时间轮中删除一个任务
     *
     * @param timePoint 任务执行时间点
     * @param uniqueID 任务唯一ID
     */
    public void remove(TimePoint timePoint, String uniqueID) {

    }

}