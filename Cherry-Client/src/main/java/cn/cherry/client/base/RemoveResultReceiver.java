package cn.cherry.client.base;

/**
 * 异步地接收消息回调，作出处理
 *
 * @author realDragonKing
 */
public interface RemoveResultReceiver {

    void doReceive(boolean isRemove);

}
