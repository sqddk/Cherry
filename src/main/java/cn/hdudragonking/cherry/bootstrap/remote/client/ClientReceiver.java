package cn.hdudragonking.cherry.bootstrap.remote.client;

import cn.hdudragonking.cherry.engine.base.TimePoint;
import com.alibaba.fastjson2.JSONObject;

/**
 * 响应远程的cherry定时任务调度引擎的任务执行通知，执行具体的任务
 *
 * @since 2022/10/19
 * @author realDragonKing
 */
public interface ClientReceiver {

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

    /**
     * 接收定时任务提交结果
     *
     * @param metaData 元数据
     * @param taskID 任务ID
     * @param result 结果
     */
    void receiveAddResult(JSONObject metaData, int taskID, boolean result);

    /**
     * 接收定时任务删除结果
     *
     * @param metaData 元数据
     * @param taskID 任务ID
     * @param result 结果
     */
    void receiveRemoveResult(JSONObject metaData, int taskID, boolean result);

}
