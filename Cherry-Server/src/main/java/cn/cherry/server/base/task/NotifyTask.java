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
     * 执行任务
     */
    @Override
    public void run() {

    }

}
