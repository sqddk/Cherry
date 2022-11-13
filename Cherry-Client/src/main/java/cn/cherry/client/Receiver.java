package cn.cherry.client;

import cn.cherry.core.engine.base.TimePoint;
import com.alibaba.fastjson2.JSONObject;


/**
 * 响应远程的cherry定时任务调度引擎的任务执行通知，执行具体的任务
 *
 * @since 2022/10/19
 * @author realDragonKing
 */
public interface Receiver {

    /**
     * 接收定时任务执行通知
     *
     * @param timePoint 时间点
     * @param metaData 元数据
     * @param taskID 任务ID
     */
    void receiveNotify(TimePoint timePoint, JSONObject metaData, int taskID);

    /**
     * 接收错误信息
     *
     * @param errorMessage 错误信息
     */
    void receiveError(String errorMessage);

}
