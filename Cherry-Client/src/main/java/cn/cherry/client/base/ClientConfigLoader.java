package cn.cherry.client.base;

import cn.cherry.core.infra.ConfigLoader;


/**
 * 配置加载器的客户端实现
 *
 * @since 2022/11/13
 * @author realDragonKing
 */
public class ClientConfigLoader extends ConfigLoader {

    private ClientConfigLoader() {

    }

    /**
     * 加载配置，对于加载失败的情况将抛出{@link RuntimeException}
     */
    @Override
    public void loadConfig() {

    }

    /**
     * 获取int类型配置值
     *
     * @param key 键
     * @return 值
     */
    @Override
    public int getIntValue(String key) {
        return 0;
    }

    /**
     * 获取String类型配置值
     *
     * @param key 键
     * @return 值
     */
    @Override
    public String getValue(String key) {
        return null;
    }
}
