package cn.cherry.core.infra.message;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * 消息处理器的顶层抽象类，能够把{@link ByteBuf}解析和转换为{@link JSONObject}后进行处理
 *
 * @author realDragonKing
 */
public abstract class MessageHandler {

    private static final Logger logger = LogManager.getLogManager().getLogger("Cherry");
    private static final Map<Integer, MessageHandler> HANDLER_MAP = new HashMap<>();

    /**
     * 扫描加载和实例化指定包名下的所有的{@link MessageHandler}实现类
     *
     * @param packageNameList 待扫描的包名列表
     */
    public static void tryLoad(String... packageNameList) {
        for (String packageName : packageNameList) {
            tryLoad0(packageName);
        }
    }

    /**
     * 扫描加载和实例化指定包名下的所有的{@link MessageHandler}实现类
     *
     * @param packageName 待扫描的包名
     */
    private static void tryLoad0(String packageName) {
        String packagePath = Objects.requireNonNull(MessageHandler.class.getResource("/")).getPath()
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
                        if (MessageHandler.class.isAssignableFrom(clazz)
                                && ! Modifier.isAbstract(clazz.getModifiers())) {
                            Constructor<?> constructor = clazz.getConstructor();
                            constructor.setAccessible(true);
                            MessageHandler resolver = (MessageHandler) constructor.newInstance();
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
     * 尝试对接收到、准备解码的数据传输对象{@link ByteBuf}进行消息处理器{@link MessageHandler}匹配和处理
     *
     * @param channel 通信信道
     * @param byteBuf 数据传输对象
     * @param charset 解码字符集
     */
    public static void tryResolve(Channel channel, ByteBuf byteBuf, Charset charset) {
        try {
            JSONObject data = JSON.parseObject(byteBuf.toString(charset));
            Integer flag = data.getInteger("flag");
            if (flag != null) {
                MessageHandler resolver = HANDLER_MAP.get(flag);
                if (resolver != null)
                    resolver.resolve(channel, data);
            }
        } catch (Exception e) {
            MessageHandler.logger.warning(e.toString());
        }
    }

    /**
     * 用于装载消息处理器{@link MessageHandler}的实现子类
     */
    public final void load() {
        Class<? extends MessageHandler> clazz = this.getClass();
        if (!Modifier.isAbstract(clazz.getModifiers())) {
            HandlerTag tag = clazz.getAnnotation(HandlerTag.class);
            if (tag != null) {
                HANDLER_MAP.put(tag.type(), this);
            }
        }
    }

    /**
     * 对{@link JSONObject}对象进行解析，然后进行处理
     *
     * @param channel 通信信道
     * @param data json数据
     */
    protected abstract void resolve(Channel channel, JSONObject data);

}
