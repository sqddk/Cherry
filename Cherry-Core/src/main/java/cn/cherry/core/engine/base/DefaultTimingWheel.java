package cn.cherry.core.engine.base;

import cn.cherry.core.engine.base.executor.ScheduleExecutor;
import cn.cherry.core.engine.base.executor.TimingWheelExecutor;
import cn.cherry.core.engine.base.task.Task;
import org.apache.logging.log4j.LogManager;

import java.util.concurrent.*;

import static cn.cherry.core.engine.utils.BaseUtils.*;
import static java.util.Objects.*;

/**
 * 时间轮{@link TimingWheel}的默认具体实现。
 * {@link DefaultTimingWheel}提供了{@link TimeSlot}槽位获取方法{@link #getSlot(int distance)}和转动方法{@link #turn()}实现
 *
 * @author realDragonKing
 */
public class DefaultTimingWheel extends TimingWheel {

    private int position;
    private final Executor executor;
    private final TimeSlot[] slotMap;

    /**
     * 默认构造函数
     *
     * @param interval 每次转动的间隔，单位为 ms
     * @param totalTicks 一轮转动的次数，也就是{@link TimeSlot}的数量
     * @param waitTimeout {@link TimeSlot}自旋锁竞争的超时时间
     * @param taskListSize 单个转动点可以承载的最大任务数量
     * @param minThreadNumber 任务执行线程池的核心线程数
     * @param maxThreadNumber 任务执行线程池的最大线程数
     */
    public DefaultTimingWheel(long interval, int totalTicks, long waitTimeout, int taskListSize,
                              int minThreadNumber, int maxThreadNumber) {
        super(interval, totalTicks, waitTimeout, taskListSize);
        this.position = 0;
        this.slotMap = new TimeSlot[totalTicks];

        for (int i = 0; i < getTotalTicks(); i++) {
            TimeSlot slot = new TimeSlot(this);
            this.slotMap[i] = slot;
        }

        int coreSize = Runtime.getRuntime().availableProcessors() << 1;
        checkPositive(minThreadNumber, "minThreadNumber");
        checkPositive(maxThreadNumber, "maxThreadNumber");
        this.executor = new ThreadPoolExecutor(
                Math.min(coreSize, minThreadNumber),
                Math.max(coreSize, maxThreadNumber),
                2L,
                TimeUnit.MINUTES,
                new LinkedBlockingQueue<>(taskListSize),
                (r, executor1) -> {});

        BlockingQueue<Integer> blockingQueue = new LinkedBlockingQueue<>(1);
        Thread scheduleThread = new Thread(new ScheduleExecutor(interval, blockingQueue));
        Thread executorThread = new Thread(new TimingWheelExecutor(this, blockingQueue));

        executorThread.start();
        scheduleThread.start();

        final long currentTimeValue = System.currentTimeMillis();
        this.setCurrentTimeValue(currentTimeValue);

        LogManager.getLogger("Cherry").info("定时任务调度引擎已经在本地成功启动并可提供服务！");
    }

    /**
     * 执行一个任务
     *
     * @param task 任务
     */
    @Override
    public void executeTask(Task task) {
        requireNonNull(task, "task");
        this.executor.execute(task::execute);
    }

    /**
     * 根据执行时间点和当前时间点的相对时间距离，取到对应的槽位{@link TimeSlot}
     *
     * @param distance 相对时间距离
     * @return 时间轮槽位
     */
    @Override
    public TimeSlot getSlot(int distance) {
        int rawIndex = distance + this.position;
        int index = rawIndex >= getTotalTicks() ? rawIndex % getTotalTicks() : rawIndex;
        return this.slotMap[index];
    }

    /**
     * 时间轮进行一次转动
     */
    @Override
    public void turn() {
        this.addCurrentTimeValue();
        this.position++;
        if (position == getTotalTicks()) {
            position = 0;
        }
        TimeSlot slot = this.slotMap[position];
        slot.decAndExecute();
    }

}
