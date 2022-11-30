package cn.cherry.core.infra.message;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息处理器的顶层抽象类，能够处理特定的{@link Message}
 *
 * @author realDragonKing
 */
public abstract class MessageHandler extends MessageAccepter {

    private static final Map<Integer, MessageHandler> HANDLER_MAP = new HashMap<>();

    /**
     * 用于分门别类地装载消息接收者{@link MessageAccepter}的实现子类
     * （有{@link MessageResolver}和{@link MessageHandler}两个类别）
     */
    @Override
    public void load() {

    }

    /**
     * 尝试对消息{@link Message}进行消息处理器{@link MessageHandler}匹配，进行特定处理
     *
     * @param message 消息{@link Message}
     * @return 响应的消息
     */
    public static Message tryHandle(Message message) {

    }

    public abstract Message handle(Message message);

}
