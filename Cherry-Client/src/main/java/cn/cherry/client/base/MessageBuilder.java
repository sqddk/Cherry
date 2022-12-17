package cn.cherry.client.base;

import cn.cherry.core.engine.TimeParser;
import com.alibaba.fastjson2.JSONObject;

import java.util.TimeZone;

/**
 * 为用户提供，提供丰富的配置项，方便快捷地构建出一个与服务端进行通信的消息体
 *
 * @author realDragonKing
 */
public class MessageBuilder {

    private final JSONObject jsonData;
    private final TimeParser parser;

    public MessageBuilder(int messageType) {
        JSONObject jsonData = new JSONObject();
        jsonData.put("type", messageType);
        this.jsonData = jsonData;
        this.parser = new TimeParser();
    }

    /**
     * 设置回调通知任务的执行时间点
     *
     * @param timeValue 绝对时间值
     * @return MessageBuilder
     */
    public MessageBuilder setTimeValue(long timeValue) {
        JSONObject jsonData = this.jsonData;
        jsonData.put("executeTime", timeValue);
        return this;
    }

    /**
     * 设置回调通知任务的执行时间点
     *
     * @param timeZone 时区
     * @param time 格式时间
     * @return MessageBuilder
     */
    public MessageBuilder setTimeValue(TimeZone timeZone, String time) {
        TimeParser parser = this.parser;
        JSONObject jsonData = this.jsonData;
        long timeValue = parser.time2timeValue(timeZone, time);
        jsonData.put("executeTime", timeValue);
        return this;
    }

    /**
     * 设置回调通知任务的执行时间点
     *
     * @param timeZone 时区
     * @param year 年份
     * @param month 月份（1月为1）
     * @param dayOfMonth 月内日期
     * @param hourOfDay 小时
     * @param minute 分钟
     * @param second 秒
     * @param millisecond 毫秒
     * @return MessageBuilder
     */
    public MessageBuilder setTimeValue(TimeZone timeZone, int year, int month, int dayOfMonth,
                                       int hourOfDay, int minute, int second, int millisecond) {
        TimeParser parser = this.parser;
        JSONObject jsonData = this.jsonData;
        long timeValue = parser.time2timeValue(timeZone, year, month, dayOfMonth, hourOfDay, minute, second, millisecond);
        jsonData.put("executeTime", timeValue);
        return this;
    }

    /**
     * @return 消息体
     */
    public JSONObject create() {
        return this.jsonData;
    }

    /**
     * 设置回调通知任务可以一起返回的元数据项
     *
     * @param key 键
     * @param value 值
     * @return MessageBuilder
     */
    public MessageBuilder setMetaData(String key, Object value) {
        JSONObject jsonData = this.jsonData;
        JSONObject metaData = jsonData.getJSONObject("metaData");
        if (metaData == null) {
            metaData = new JSONObject();
            jsonData.put("metaData", metaData);
        }
        metaData.put(key, value);
        return this;
    }

}
