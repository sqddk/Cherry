package cn.cherry.core;

import cn.cherry.core.engine.base.DefaultTimingWheel;
import cn.cherry.core.engine.base.Task;
import cn.cherry.core.engine.base.TimePoint;
import cn.cherry.core.engine.base.TimingWheel;
import cn.cherry.core.engine.base.executor.ScheduleExecutor;
import cn.cherry.core.engine.base.executor.TimingWheelExecutor;
import cn.cherry.core.engine.utils.BaseUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * cherry定时任务调度引擎的本地启动引导类
 *
 * @since 2022/10/17
 * @author realDragonKing
 */
public class LocalStarter {
    private final static class CherryLocalStarterHolder {
        private final static LocalStarter INSTANCE = new LocalStarter();
    }
    public static LocalStarter getInstance() {
        return CherryLocalStarterHolder.INSTANCE;
    }

    private LocalStarter() {}

    /**
     * 时间轮！
     */
    private TimingWheel timingWheel;

    /**
     * 日志打印类
     */
    private final Logger logger = LogManager.getLogger("Cherry");

    /**
     * 为cherry引擎的启动进行初始化
     *
     * @param interval 刻度间隔时间
     * @param totalTicks 总刻度数
     * @param wheelTimeout 时间轮的自旋锁获取超时时间（超时放弃自旋）
     */
    public void initial(int interval, int totalTicks, int wheelTimeout) {
        this.timingWheel = new DefaultTimingWheel(interval, totalTicks, wheelTimeout);
        BlockingQueue<Integer> blockingQueue = new LinkedBlockingQueue<>(1);
        new Thread(new ScheduleExecutor(interval, blockingQueue)).start();
        new Thread(new TimingWheelExecutor(this.timingWheel, blockingQueue)).start();
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
