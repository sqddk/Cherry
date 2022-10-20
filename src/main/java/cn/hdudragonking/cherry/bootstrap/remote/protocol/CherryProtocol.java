package cn.hdudragonking.cherry.bootstrap.remote.protocol;


/**
 * ʵ����cherryͨ��Э�����Ϣ��
 *
 * @since 2022/10/18
 * @author realDragonKing
 */
public class CherryProtocol {

    /**
     * ���ݰ�������
     */
    private Integer flag;

    /**
     * �ַ�����ʽ��ʱ���
     */
    private String timePointString;

    /**
     * Ԫ����
     */
    private String metaData;

    /**
     * ����ID
     */
    private String taskID;

    /**
     * ������Ϣ
     */
    private String errorMessage;

    /**
     * �����Ϣ
     */
    private String resultMessage;

    public CherryProtocol() {}

    /**
     * ��ȡ���ݰ�������
     *
     * @return ���ݰ�������
     */
    public Integer getFlag() {
        return this.flag;
    }

    /**
     * �������ݰ�������
     *
     * @param flag ����
     * @return this
     */
    public CherryProtocol setFlag(Integer flag) {
        this.flag = flag;
        return this;
    }

    /**
     * ��ȡ�ַ�����ʽ��ʱ���
     *
     * @return �ַ�����ʽ��ʱ���
     */
    public String getStringTimePoint() {
        return this.timePointString;
    }

    /**
     * �����ַ�����ʽ��ʱ���
     *
     * @param timePointString �ַ�����ʽ��ʱ���
     * @return this
     */
    public CherryProtocol setStringTimePoint(String timePointString) {
        this.timePointString = timePointString;
        return this;
    }

    /**
     * ��ȡԪ����
     *
     * @return Ԫ����
     */
    public String getMetaData() {
        return this.metaData;
    }

    /**
     * ����Ԫ����
     *
     * @param metaData Ԫ����
     * @return this
     */
    public CherryProtocol setMetaData(String metaData) {
        this.metaData = metaData;
        return this;
    }

    /**
     * ��ȡ����ID
     *
     * @return ����ID
     */
    public String getTaskID() {
        return this.taskID;
    }

    /**
     * ��������ID
     *
     * @param taskID ����ID
     * @return this
     */
    public CherryProtocol setTaskID(String taskID) {
        this.taskID = taskID;
        return this;
    }

    /**
     * ��ȡ������Ϣ
     *
     * @return ������Ϣ
     */
    public String getErrorMessage() {
        return this.errorMessage;
    }

    /**
     * ���ô�����Ϣ
     *
     * @param errorMessage ������Ϣ
     * @return this
     */
    public CherryProtocol setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

    /**
     * ��ȡ���������Ϣ
     *
     * @return ���������Ϣ
     */
    public String getResult() {
        return this.resultMessage;
    }

    /**
     * ���ò��������Ϣ
     *
     * @param result �������
     * @return this
     */
    public CherryProtocol setResult(String result) {
        this.resultMessage = result;
        return this;
    }

}
