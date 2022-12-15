package cn.cherry.core.infra.message;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import io.netty.buffer.ByteBuf;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 消息解析器的顶层抽象类，能够把{@link ByteBuf}解析和转换为{@link Message}
 *
 * @author realDragonKing
 */
public abstract class MessageResolver {

    private static final Map<Integer, MessageResolver> RESOLVER_MAP = new HashMap<>();

    /**
     * 扫描加载和实例化指定包名下的所有的{@link MessageResolver}实现类
     *
     * @param packageNameList 待扫描的包名列表
     */
    public static void tryLoad(String... packageNameList) {
        for (String packageName : packageNameList) {
            tryLoad0(packageName);
        }
    }

    /**
     * 扫描加载和实例化指定包名下的所有的{@link MessageResolver}实现类
     *
     * @param packageName 待扫描的包名
     */
    private static void tryLoad0(String packageName) {
        String packagePath = Objects.requireNonNull(MessageResolver.class.getResource("/")).getPath()
                + packageName.replace(".", File.separator);
        File pack = new File(packagePath);
        String name;
        if (pack.isDirectory()) {
            for (File file : Objects.requireNonNull(pack.listFiles())) {
                name = file.getName();
                if (name.endsWith(".class")) {
                    try {
                        name = name.substring(0, name.length() - 6);
                        Class<?> clazz = Class.forName(packageName + "." + name);
                        if (MessageResolver.class.isAssignableFrom(clazz)
                                && ! Modifier.isAbstract(clazz.getModifiers())) {
                            Constructor<?> constructor = clazz.getConstructor();
                            constructor.setAccessible(true);
                            MessageResolver resolver = (MessageResolver) constructor.newInstance();
                            resolver.load();
                        }
                    } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
                             InstantiationException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        } else {
            throw new RuntimeException("扫描消息解析器包出错，请检查文件结构！");
        }
    }

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
     * 用于装载消息解析器{@link MessageResolver}的实现子类
     */
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
