package cn.cherry.core.infra.message;

import java.lang.reflect.Modifier;
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
     * 尝试对消息{@link Message}进行消息处理器{@link MessageHandler}匹配，进行特定处理
     *
     * @param message 消息{@link Message}
     * @return 响应的消息
     */
    public static Message tryHandle(Message message) {
        int flag = message.getFlag();
        Message callback = null;
        MessageHandler handler = HANDLER_MAP.get(flag);
        if (handler != null) {
            callback = handler.handle(message);
        }
        return callback;
    }

    /**
     * 用于分门别类地装载消息接收者{@link MessageAccepter}的实现子类
     * （有{@link MessageResolver}和{@link MessageHandler}两个类别）
     */
    @Override
    public void load() {
        Class<? extends MessageHandler> clazz = this.getClass();
        if (!Modifier.isAbstract(clazz.getModifiers())) {
            MessageTypeTag tag = clazz.getAnnotation(MessageTypeTag.class);
            if (tag != null) {
                HANDLER_MAP.put(tag.flag(), this);
            }
        }
    }

    /**
     * 对{@link Message}消息进行处理，并且返回响应信息
     *
     * @param message 接受的消息
     * @return {@link Message}一般不会返回null！
     */
    public abstract Message handle(Message message);

}
