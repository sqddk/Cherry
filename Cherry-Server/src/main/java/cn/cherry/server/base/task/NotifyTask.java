package cn.cherry.server.base.task;

import cn.cherry.core.engine.base.task.Task;
import com.alibaba.fastjson2.JSONObject;

public class NotifyTask implements Task {

    /**
     * @return 任务的配置信息
     */
    @Override
    public JSONObject getTaskConfig() {
        return null;
    }

    /**
     * @return 任务的元数据（由业务方来操作和保存临时数据）
     */
    @Override
    public JSONObject getMetaData() {
        return null;
    }

    /**
     * 执行任务
     */
    @Override
    public void execute() {

    }

}
