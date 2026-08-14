package config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private static Properties properties;
    private static final String CONFIG_FILE_PATH = "config.properties";

    static {
        properties = new Properties();
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE_PATH)) {
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("config.properties not found at project root", e);
        }
    }

    private static String getOrDefault(String key, String def) {
        String v = properties.getProperty(key);
        return (v == null || v.trim().isEmpty()) ? def : v.trim();
    }

    public static String getUrl() { return getOrDefault("url", "https://javabykiran.in/other/CC"); }
    public static String getAdminUrl() { return getOrDefault("adminUrl", "https://javabykiran.in/other/CC/admin_zE82E2.php"); }
    public static String getAdminUsername() { return getOrDefault("adminUsername", "admin"); }
    public static String getAdminPassword() { return getOrDefault("adminPassword", "pass"); }
    public static int getImplicitWait() { return Integer.parseInt(getOrDefault("implicitWait", "10")); }
    public static int getExplicitWait() { return Integer.parseInt(getOrDefault("explicitWait", "15")); }
}