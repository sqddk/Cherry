package cn.hdudragonking.cherry.bootstrap.remote.protocol;


/**
 * 实现了cherry通信协议的消息体
 *
 * @since 2022/10/18
 * @author realDragonKing
 */
public class CherryProtocol {

    /**
     * 数据包的类型
     */
    private Integer flag;

    /**
     * 字符串格式的时间点
     */
    private String timePointString;

    /**
     * 元数据
     */
    private String metaData;

    /**
     * 任务ID
     */
    private String taskID;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 结果信息
     */
    private String resultMessage;

    public CherryProtocol() {}

    /**
     * 获取数据包的类型
     *
     * @return 数据包的类型
     */
    public Integer getFlag() {
        return this.flag;
    }

    /**
     * 设置数据包的类型
     *
     * @param flag 类型
     * @return this
     */
    public CherryProtocol setFlag(Integer flag) {
        this.flag = flag;
        return this;
    }

    /**
     * 获取字符串格式的时间点
     *
     * @return 字符串格式的时间点
     */
    public String getStringTimePoint() {
        return this.timePointString;
    }

    /**
     * 设置字符串格式的时间点
     *
     * @param timePointString 字符串格式的时间点
     * @return this
     */
    public CherryProtocol setStringTimePoint(String timePointString) {
        this.timePointString = timePointString;
        return this;
    }

    /**
     * 获取元数据
     *
     * @return 元数据
     */
    public String getMetaData() {
        return this.metaData;
    }

    /**
     * 设置元数据
     *
     * @param metaData 元数据
     * @return this
     */
    public CherryProtocol setMetaData(String metaData) {
        this.metaData = metaData;
        return this;
    }

    /**
     * 获取任务ID
     *
     * @return 任务ID
     */
    public String getTaskID() {
        return this.taskID;
    }

    /**
     * 设置任务ID
     *
     * @param taskID 任务ID
     * @return this
     */
    public CherryProtocol setTaskID(String taskID) {
        this.taskID = taskID;
        return this;
    }

    /**
     * 获取错误信息
     *
     * @return 错误信息
     */
    public String getErrorMessage() {
        return this.errorMessage;
    }

    /**
     * 设置错误信息
     *
     * @param errorMessage 错误信息
     * @return this
     */
    public CherryProtocol setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

    /**
     * 获取操作结果信息
     *
     * @return 操作结果信息
     */
    public String getResult() {
        return this.resultMessage;
    }

    /**
     * 设置操作结果信息
     *
     * @param result 操作结果
     * @return this
     */
    public CherryProtocol setResult(String result) {
        this.resultMessage = result;
        return this;
    }

}
