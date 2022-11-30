package cn.cherry.core.infra.message;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Objects;

/**
 * 可以处理消息{@link Message}的对象，解析器{@link MessageResolver}和处理器{@link MessageHandler}都是其子类<br/>
 * 提供了静态方法{@link #tryLoad(String...)}来扫描加载和实例化指定包名下的所有的{@link MessageAccepter}实现类
 *
 * @author realDragonKing
 */
public abstract class MessageAccepter {

    /**
     * 扫描加载和实例化指定包名下的所有的{@link MessageAccepter}实现类
     *
     * @param packageNameList 待扫描的包名列表
     */
    public static void tryLoad(String... packageNameList) {
        for (String packageName : packageNameList) {
            tryLoad0(packageName);
        }
    }

    /**
     * 扫描加载和实例化指定包名下的所有的{@link MessageAccepter}实现类
     *
     * @param packageName 待扫描的包名
     */
    private static void tryLoad0(String packageName) {
        String packagePath = Objects.requireNonNull(MessageAccepter.class.getResource("/")).getPath()
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
                        if (MessageAccepter.class.isAssignableFrom(clazz)
                                && ! Modifier.isAbstract(clazz.getModifiers())) {
                            Constructor<?> constructor = clazz.getConstructor();
                            constructor.setAccessible(true);
                            MessageAccepter accepter = (MessageAccepter) constructor.newInstance();
                            accepter.load();
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
     * 用于分门别类地装载消息接收者{@link MessageAccepter}的实现子类
     * （有{@link MessageResolver}和{@link MessageHandler}两个类别）
     */
    public abstract void load();

}
