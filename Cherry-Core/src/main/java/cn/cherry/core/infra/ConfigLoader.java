package cn.cherry.core.infra;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * 配置加载器的顶层抽象类
 *
 * @since 2022/11/25
 * @author realDragonKing
 */
public abstract class ConfigLoader {

    private static ConfigLoader configLoader;

    /**
     * 获取配置加载器全局唯一实例（如果没有初始化过则进行初始化）。通过这种方式让客户端和服务端有不同的配置加载实现方式
     *
     * @param clazz 配置加载器的子类的class对象
     * @return 配置加载器
     */
    public static ConfigLoader getInstance(Class<? extends ConfigLoader> clazz) {
        if (configLoader == null) {
            synchronized (ConfigLoader.class) {
                try {
                    Constructor<? extends ConfigLoader> constructor = clazz.getDeclaredConstructor();
                    constructor.setAccessible(true);
                    configLoader = constructor.newInstance();
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                         NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return configLoader;
    }

    protected ConfigLoader() {
        this.loadConfig();
    }

    /**
     * 加载配置，对于加载失败的情况将抛出{@link RuntimeException}
     */
    public abstract void loadConfig();

    /**
     * 获取int类型配置值
     *
     * @param key 键
     * @return 值
     */
    public abstract int getIntValue(String key);

    /**
     * 获取String类型配置值
     *
     * @param key 键
     * @return 值
     */
    public abstract String getValue(String key);

}
