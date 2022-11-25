package cn.cherry.core.infra.command;

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
 * 消息解析器的顶层抽象类，提供静态方法{@link #load(String)}来扫描和装载有{@link MessageResolverTag}注解的
 * {@link MessageResolver}实现类，实现高度解耦
 *
 * @author realDragonKing
 */
public abstract class MessageResolver {

    private static final Map<Integer, MessageResolver> RESOLVER_MAP = new HashMap<>();

    /**
     * 扫描加载和实例化指定包名下的所有拥有{@link MessageResolverTag}注解的{@link MessageResolver}实现类
     *
     * @param packageName 待扫描的包名
     */
    public static void load(String packageName) {
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
                            MessageResolverTag tag = clazz.getAnnotation(MessageResolverTag.class);
                            if (tag != null) {
                                Constructor<?> constructor = clazz.getDeclaredConstructor();
                                constructor.setAccessible(true);
                                RESOLVER_MAP.put(tag.flag(), (MessageResolver) constructor.newInstance());
                            }
                        }
                    } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
                             InstantiationException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        } else {
            throw new RuntimeException("扫描命令解析器包出错，请检查文件结构！");
        }
    }

    /**
     * 尝试对接收到、准备解码的数据传输对象{@link ByteBuf}进行消息解析器{@link MessageResolver}匹配，解析出{@link Message}
     *
     * @param buf 数据传输对象
     * @return {@link Message}
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
     * 对{@link JSONObject}对象进行解析，解析出{@link Message}
     *
     * @param data json数据
     * @return {@link Message}
     */
    protected abstract Message resolve(JSONObject data);

}
