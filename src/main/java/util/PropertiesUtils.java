package util;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * User: yanfb
 * Date: 13-9-10
 * Time: 上午9:27
 */
public class PropertiesUtils {

    private static final String propertiesFile = "parameter";

    // 默认绑定资源
    private static ResourceBundle resources = null;

    /**
     * 获取指定属性值
     * @param property 属性名
     * @return
     */
    public static String getPropertiesValue(String property) {
        String val = "";
        try {
            if (resources == null) {
                initResources();
            }
            val = resources.getString(property);
        } catch (Exception e) {
            // ignore
        }
        return val;
    }

    /**
     * 根据配置文件获取指定key值
     * @param propertiesFile 属性文件
     * @param property 属性名
     * @return
     */
    public static String getPropertiesValue(String propertiesFile,
                                            String property) {
        String val = "";
        ResourceBundle resources1 = null;
        try {
            resources1 = ResourceBundle.getBundle(propertiesFile);
            val = resources1.getString(property);
        }

        catch (MissingResourceException e) {
            // ignore
        }

        return val;
    }

    /**
     * 初始化配置文件
     */
    private static void initResources() {
        try {
            resources = ResourceBundle.getBundle(propertiesFile);
        } catch (MissingResourceException e) {
            throw new RuntimeException(e);
        }
    }
}
