package cn.hdudragonking.cherry.bootstrap.remote.protocol;


/**
 * 实现了cherry通信协议的消息体
 *
 * @since 2022/10/18
 * @author realDragonKing
 */
public class CherryProtocol {

    private Integer flag;
    private String timePointString;
    private String metaData;
    private String taskID;
    private String errorMessage;

    public CherryProtocol() {}

    public Integer getFlag() {
        return this.flag;
    }

    public CherryProtocol setFlag(Integer flag) {
        this.flag = flag;
        return this;
    }

    public String getStringTimePoint() {
        return this.timePointString;
    }

    public CherryProtocol setStringTimePoint(String timePointString) {
        this.timePointString = timePointString;
        return this;
    }

    public String getMetaData() {
        return this.metaData;
    }

    public CherryProtocol setMetaData(String metaData) {
        this.metaData = metaData;
        return this;
    }

    public String getTaskID() {
        return this.taskID;
    }

    public CherryProtocol setTaskID(String taskID) {
        this.taskID = taskID;
        return this;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public CherryProtocol setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

}
