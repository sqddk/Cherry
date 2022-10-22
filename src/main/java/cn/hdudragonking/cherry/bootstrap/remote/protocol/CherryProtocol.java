package cn.hdudragonking.cherry.bootstrap.remote.protocol;


import com.alibaba.fastjson2.JSONObject;

import java.util.Map;

/**
 * ʵ����cherryͨ��Э�����Ϣ��
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
     * ��ȡ���ݰ�������
     *
     * @return ���ݰ�������
     */
    public Integer getFlag() {
        return this.dataBucket.getInteger("flag");
    }

    /**
     * ��ȡ�ͻ��˵�����
     *
     * @return �ͻ��˵�����
     */
    public String getChannelName() {
        return this.dataBucket.getString("channelName");
    }

    /**
     * ���ÿͻ��˵�����
     *
     * @param channelName �ͻ��˵�����
     * @return this
     */
    public CherryProtocol setChannelName(String channelName) {
        this.dataBucket.put("channelName", channelName);
        return this;
    }

    /**
     * ��ȡ�ַ�����ʽ��ʱ���
     *
     * @return �ַ�����ʽ��ʱ���
     */
    public String getStringTimePoint() {
        return this.dataBucket.getString("timePoint");
    }

    /**
     * �����ַ�����ʽ��ʱ���
     *
     * @param timePointString �ַ�����ʽ��ʱ���
     * @return this
     */
    public CherryProtocol setStringTimePoint(String timePointString) {
        this.dataBucket.put("timePoint", timePointString);
        return this;
    }

    /**
     * ��ȡԪ����
     *
     * @return Ԫ����
     */
    public JSONObject getMetaData() {
        return this.dataBucket.getJSONObject("metaData");
    }

    /**
     * ����Ԫ����
     *
     * @param key ��
     * @param value ֵ
     * @return this
     */
    public CherryProtocol setMetaData(String key, Object value) {
        this.dataBucket.getJSONObject("metaData").put(key, value);
        return this;
    }

    /**
     * ����Ԫ����
     *
     * @param metaData һ��Ԫ����
     * @return this
     */
    public CherryProtocol setMetaData(JSONObject metaData) {
        this.dataBucket.put("metaData", metaData);
        return this;
    }

    /**
     * ��ȡ����ID
     *
     * @return ����ID
     */
    public int getTaskID() {
        return this.dataBucket.getInteger("taskID");
    }

    /**
     * ��������ID
     *
     * @param taskID ����ID
     * @return this
     */
    public CherryProtocol setTaskID(int taskID) {
        this.dataBucket.put("taskID", taskID);
        return this;
    }

    /**
     * ��ȡ������Ϣ
     *
     * @return ������Ϣ
     */
    public String getErrorMessage() {
        return this.dataBucket.getString("errorMessage");
    }

    /**
     * ���ô�����Ϣ
     *
     * @param errorMessage ������Ϣ
     * @return this
     */
    public CherryProtocol setErrorMessage(String errorMessage) {
        this.dataBucket.put("errorMessage", errorMessage);
        return this;
    }

    /**
     * ��ȡ���������Ϣ
     *
     * @return ���������Ϣ
     */
    public boolean getResult() {
        return this.dataBucket.getBooleanValue("result");
    }

    /**
     * ���ò��������Ϣ
     *
     * @param result �������
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
