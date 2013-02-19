package com.massivekinetics.ow.data.manager;

/**
 * Created with IntelliJ IDEA.
 * User: bovy
 * Date: 2/14/13
 * Time: 9:29 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ConfigManager {
    public static final String SETTINGS = "settings";
    public static final String CITY_NAME = "city_name";
    public static final String TEMPERATURE_MODE_FAHRENHEIT = "temperature_mode_fahrenheit";
    public static final String AUTO_DEFINE_LOCATION_ENABLED = "auto_define_location_enabled";
    public static final String NOTIFICATION_ENABLED = "notification_enabled";
    public static final String NOTIFICATION_TIME_HOUR = "notification_time_hour";
    public static final String NOTIFICATION_TIME_MINUTE = "notification_time_minute";

    String getStringConfig(String configName);

    boolean getBooleanConfig(String configName);

    int getIntConfig(String configName);

    void setConfig(String name, String value);

    void setConfig(String name, boolean value);

    void setConfig(String name, int value);
}
