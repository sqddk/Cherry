package cn.hdudragonking.cherry.bootstrap.remote.client;

import cn.hdudragonking.cherry.engine.base.TimePoint;

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
     * @param uniqueID 任务唯一ID
     */
    void receiveNotify(TimePoint timePoint, String metaData, String uniqueID);

    /**
     * 接收错误信息
     *
     * @param errorMessage 错误信息
     */
    void receiveError(String errorMessage);

}
