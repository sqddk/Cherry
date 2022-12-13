package cn.cherry.core.infra.message;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import io.netty.buffer.ByteBuf;

import java.lang.reflect.Modifier;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * 消息解析器的顶层抽象类，能够把{@link ByteBuf}解析和转换为{@link Message}
 *
 * @author realDragonKing
 */
public abstract class MessageResolver extends MessageAccepter {

    private static final Map<Integer, MessageResolver> RESOLVER_MAP = new HashMap<>();

    /**
     * 尝试对接收到、准备解码的数据传输对象{@link ByteBuf}进行消息解析器{@link MessageResolver}匹配，解析出{@link Message}
     *
     * @param buf 数据传输对象
     * @return {@link Message}（如果解析失败则返回null）
     */
    public static Message tryResolve(ByteBuf buf, Charset charset) {
        try {
            JSONObject data = JSON.parseObject(buf.toString(charset));
            Integer flag = data.getInteger("flag");
            if (flag != null) {
                MessageResolver resolver = RESOLVER_MAP.get(flag);
                if (resolver != null) {
                    return resolver.resolve(data);
                }
            }
        } catch (Exception ignored) {}
        return null;
    }

    /**
     * 用于分门别类地装载消息接收者{@link MessageAccepter}的实现子类
     * （有{@link MessageResolver}和{@link MessageHandler}两个类别）
     */
    @Override
    public final void load() {
        Class<? extends MessageResolver> clazz = this.getClass();
        if (!Modifier.isAbstract(clazz.getModifiers())) {
            MessageTypeTag tag = clazz.getAnnotation(MessageTypeTag.class);
            if (tag != null) {
                RESOLVER_MAP.put(tag.type(), this);
            }
        }
    }

    /**
     * 对{@link JSONObject}对象进行解析，解析出{@link Message}
     *
     * @param data json数据
     * @return {@link Message}（如果解析失败则返回null）
     */
    protected abstract Message resolve(JSONObject data);

}
