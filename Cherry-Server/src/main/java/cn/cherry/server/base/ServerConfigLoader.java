package cn.cherry.server.base;

import cn.cherry.core.infra.ConfigLoader;
import cn.cherry.core.infra.utils.BaseUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 配置加载器的服务端实现
 *
 * @since 2022/11/06
 * @author realDragonKing
 */
public class ServerConfigLoader extends ConfigLoader {

    private static final String CONFIG_PATH = "/Config";
    private final String filePath = BaseUtils.getFilePath();
    private final Map<String, String> configBucket = new HashMap<>();
    private final Logger logger = LogManager.getLogger("Cherry");

    private ServerConfigLoader() {}

    /**
     * 读取已经加载的外部配置文件
     */
    @Override
    public void loadConfig() {
        File configFile = new File(this.filePath + CONFIG_PATH);
        if (!configFile.exists()) {
            this.initialConfig(configFile);
        }
        try (BufferedReader br = new BufferedReader(
                     new InputStreamReader(new FileInputStream(configFile), StandardCharsets.UTF_8))) {
            String line, key, value;
            String[] setting;
            while ((line = br.readLine()) != null) {
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
        try (InputStream inputStream = ServerConfigLoader.class.getResourceAsStream(CONFIG_PATH);
             FileOutputStream fileOutputStream = new FileOutputStream(configFile)) {
            if (inputStream == null) {
                this.logger.error("无法检测到内部备份的初始化配置文件！");
                throw new RuntimeException();
            }
            byte[] b = new byte[1024];
            int length;
            while((length = inputStream.read(b)) > 0){
                fileOutputStream.write(b, 0, length);
            }
        } catch (IOException e) {
            this.logger.error(e.toString());
            throw new RuntimeException();
        }
    }

    /**
     * 获取int类型配置值
     *
     * @param key 键
     * @return 值
     */
    @Override
    public int getIntValue(String key) {
        String value = this.configBucket.get(key);
        if (value == null) {
            throw new RuntimeException();
        }
        return Integer.parseInt(value);
    }

    /**
     * 获取String类型配置值
     *
     * @param key 键
     * @return 值
     */
    @Override
    public String getValue(String key) {
        String value = this.configBucket.get(key);
        if (value == null) {
            throw new RuntimeException();
        }
        return value;
    }

}
