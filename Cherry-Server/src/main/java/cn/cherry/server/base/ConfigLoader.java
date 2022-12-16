package cn.cherry.server.base;

import cn.cherry.core.engine.utils.BaseUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 服务端的配置加载器
 *
 * @author realDragonKing
 */
public class ConfigLoader {

    private static final String CONFIG_PATH = "/Config";
    private final Map<String, String> configBucket;
    private final String filePath;
    private final Logger logger;

    private ConfigLoader() {
        this.configBucket = new HashMap<>();
        this.filePath = BaseUtils.getFilePath();
        this.logger = LogManager.getLogger("Cherry");
    }

    private static ConfigLoader configLoader;

    /**
     * 获取配置加载器全局唯一实例（如果没有初始化过则进行初始化）。通过这种方式让客户端和服务端有不同的配置加载实现方式
     *
     * @return 配置加载器
     */
    public static ConfigLoader getInstance() {
        if (configLoader == null) {
            synchronized (ConfigLoader.class) {
                configLoader = new ConfigLoader();
                configLoader.loadConfig();
            }
        }
        return configLoader;
    }

    /**
     * 读取已经加载的外部配置文件
     */
    private void loadConfig() {
        File configFile = new File(this.filePath + CONFIG_PATH);
        if (!configFile.exists()) {
            this.initialConfig(configFile);
        }

        try (InputStreamReader iReader = new InputStreamReader(new FileInputStream(configFile), StandardCharsets.UTF_8);
             BufferedReader bReader = new BufferedReader(iReader)) {

            String line, key, value;
            String[] setting;

            while ((line = bReader.readLine()) != null) {
                if (line.startsWith("#")) {
                    continue;
                }

                setting = line.split("=");
                if (setting.length != 2 || setting[0].length() == 0 || setting[1].length() == 0) {
                    continue;
                }

                key = setting[0].trim();
                value = setting[1].trim();
                this.configBucket.put(key, value);
            }
        } catch (IOException e) {
            this.logger.error(e.toString());
            throw new RuntimeException(e);
        }
    }

    /**
     * 尝试初始化配置
     * @param configFile 外部配置文件
     */
    private void initialConfig(File configFile) {
        try (InputStream inputStream = ConfigLoader.class.getResourceAsStream(CONFIG_PATH);
             FileOutputStream fileOutputStream = new FileOutputStream(configFile)) {
            if (inputStream == null) {
                throw new RuntimeException("无法检测到内部备份的初始化配置文件！");
            }

            byte[] b = new byte[1024];
            int length;
            while((length = inputStream.read(b)) > 0){
                fileOutputStream.write(b, 0, length);
            }
        } catch (Exception e) {
            this.logger.error(e.toString());
            System.exit(0);
        }
    }

    /**
     * 获取int类型配置值
     *
     * @param key 键
     * @return 值
     */
    public int getIntValue(String key) {
        String value = this.configBucket.get(key);
        if (value == null) {
            throw new NullPointerException("这个值不存在！");
        }
        return Integer.parseInt(value);
    }

    /**
     * 获取String类型配置值
     *
     * @param key 键
     * @return 值
     */
    public String getValue(String key) {
        String value = this.configBucket.get(key);
        if (value == null) {
            throw new NullPointerException("这个值不存在！");
        }
        return value;
    }

}
