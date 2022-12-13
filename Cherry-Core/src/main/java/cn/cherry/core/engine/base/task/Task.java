package cn.cherry.core.engine.base.task;

import com.alibaba.fastjson2.JSONObject;

/**
 * 可被执行的定时任务的接口
 *
 * @author realDragonKing
 */
public interface Task {

    /**
     * @return 任务的配置信息
     */
    JSONObject getTaskConfig();

    /**
     * @return 任务的元数据（由业务方来操作和保存临时数据）
     */
    JSONObject getMetaData();

    /**
     * 任务的执行内容
     */
    void execute();

}
