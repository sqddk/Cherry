package cn.hdudragonking.cherry.bootstrap.remote.protocol;


import com.alibaba.fastjson2.JSONObject;

import java.util.Map;

/**
 * 实现了cherry通信协议的消息体
 *
 * @since 2022/10/18
 * @author realDragonKing
 */
public class CherryProtocol {

    private final JSONObject dataBucket;

    public CherryProtocol(int flag) {
        this.dataBucket = new JSONObject(Map.of("flag", flag, "metaData", new JSONObject()));
    }

    /**
     * 获取数据包的类型
     *
     * @return 数据包的类型
     */
    public Integer getFlag() {
        return this.dataBucket.getInteger("flag");
    }

    /**
     * 获取客户端的名称
     *
     * @return 客户端的名称
     */
    public String getChannelName() {
        return this.dataBucket.getString("channelName");
    }

    /**
     * 设置客户端的名称
     *
     * @param channelName 客户端的名称
     * @return this
     */
    public CherryProtocol setChannelName(String channelName) {
        this.dataBucket.put("channelName", channelName);
        return this;
    }

    /**
     * 获取字符串格式的时间点
     *
     * @return 字符串格式的时间点
     */
    public String getStringTimePoint() {
        return this.dataBucket.getString("timePoint");
    }

    /**
     * 设置字符串格式的时间点
     *
     * @param timePointString 字符串格式的时间点
     * @return this
     */
    public CherryProtocol setStringTimePoint(String timePointString) {
        this.dataBucket.put("timePoint", timePointString);
        return this;
    }

    /**
     * 获取元数据
     *
     * @return 元数据
     */
    public JSONObject getMetaData() {
        return this.dataBucket.getJSONObject("metaData");
    }

    /**
     * 设置元数据
     *
     * @param key 键
     * @param value 值
     * @return this
     */
    public CherryProtocol setMetaData(String key, Object value) {
        this.dataBucket.getJSONObject("metaData").put(key, value);
        return this;
    }

    /**
     * 设置元数据
     *
     * @param metaData 一组元数据
     * @return this
     */
    public CherryProtocol setMetaData(JSONObject metaData) {
        this.dataBucket.put("metaData", metaData);
        return this;
    }

    /**
     * 获取任务ID
     *
     * @return 任务ID
     */
    public int getTaskID() {
        return this.dataBucket.getInteger("taskID");
    }

    /**
     * 设置任务ID
     *
     * @param taskID 任务ID
     * @return this
     */
    public CherryProtocol setTaskID(int taskID) {
        this.dataBucket.put("taskID", taskID);
        return this;
    }

    /**
     * 获取错误信息
     *
     * @return 错误信息
     */
    public String getErrorMessage() {
        return this.dataBucket.getString("errorMessage");
    }

    /**
     * 设置错误信息
     *
     * @param errorMessage 错误信息
     * @return this
     */
    public CherryProtocol setErrorMessage(String errorMessage) {
        this.dataBucket.put("errorMessage", errorMessage);
        return this;
    }

    /**
     * 获取操作结果信息
     *
     * @return 操作结果信息
     */
    public boolean getResult() {
        return this.dataBucket.getBooleanValue("result");
    }

    /**
     * 设置操作结果信息
     *
     * @param result 操作结果
     * @return this
     */
    public CherryProtocol setResult(boolean result) {
        this.dataBucket.put("result", result);
        return this;
    }

    public String toString() {
        return this.dataBucket.toJSONString();
    }

}
