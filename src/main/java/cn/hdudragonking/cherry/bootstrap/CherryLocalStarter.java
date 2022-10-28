package cn.hdudragonking.cherry.bootstrap;

import cn.hdudragonking.cherry.bootstrap.remote.server.CherryServer;
import cn.hdudragonking.cherry.engine.base.DefaultTimingWheel;
import cn.hdudragonking.cherry.engine.base.TimePoint;
import cn.hdudragonking.cherry.engine.base.TimingWheel;
import cn.hdudragonking.cherry.engine.base.executor.ScheduleExecutor;
import cn.hdudragonking.cherry.engine.base.executor.TimingWheelExecutor;
import cn.hdudragonking.cherry.engine.task.Task;
import cn.hdudragonking.cherry.engine.utils.BaseUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * cherry定时任务调度引擎的本地启动引导类，可以在这里启动本地服务。
 * <p>
 * socket 网络服务 {@link CherryServer}
 * 的启动本质上也是启动本地服务，并提供网络通信能力
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

    /**
     * 时间轮！
     */
    private TimingWheel timingWheel;

    /**
     * 默认的每个刻度之间的时间间隔
     */
    private final static int DEFAULT_INTERVAL = 60000;

    /**
     * 日志打印类
     */
    private final Logger logger = LogManager.getLogger("Cherry");

    /**
     * 为cherry引擎的启动进行初始化，使用默认的时间间隔
     */
    public void initial() {
        this.initial(DEFAULT_INTERVAL);
    }

    /**
     * 为cherry引擎的启动进行初始化
     *
     * @param interval 时间间隔
     */
    public void initial(int interval) {
        ExecutorService threadPool = Executors.newFixedThreadPool(2);
        this.timingWheel = new DefaultTimingWheel(interval);
        BlockingQueue<Integer> blockingQueue = new LinkedBlockingQueue<>(1);
        threadPool.submit(new ScheduleExecutor(DEFAULT_INTERVAL, blockingQueue));
        threadPool.submit(new TimingWheelExecutor(this.timingWheel, blockingQueue));
        BaseUtils.printLogo();
        this.logger.info("定时任务调度引擎已经在本地成功启动并可提供服务！");
    }

    /**
     * 通过本地进程内API，向时间轮中提交任务
     *
     * @param task 待执行的任务
     * @return 是否成功提交 | 任务ID
     */
    public int[] submit(Task task) {
        return this.timingWheel.submit(task);
    }

    /**
     * 根据任务执行时间点和任务ID，通过本地进程内API，向时间轮中删除一个任务
     *
     * @param timePoint 任务执行时间点
     * @param taskID 任务ID
     * @return 是否成功删除
     */
    public boolean remove(TimePoint timePoint, int taskID) {
        return this.timingWheel.remove(timePoint, taskID);
    }

}
