package cn.cherry.core.engine.task;

import java.util.Map;

/**
 * 可被执行的定时任务的接口
 *
 * @author realDragonKing
 */
public interface Task extends Runnable {

    /**
     * @return 任务的配置信息
     */
    Map<String, Object> getTaskConfig();

    /**
     * 任务的执行内容
     */
    void run();

}
