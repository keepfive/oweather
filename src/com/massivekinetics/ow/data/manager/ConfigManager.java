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
    public static final String GPS_PARAMS = "gps_params";
    public static final String GPS_LAST_UPDATED = "gps_last_updated";
    public static final String SESSION = "session";

    String getStringConfig(String configName);

    boolean getBooleanConfig(String configName);

    int getIntConfig(String configName);

    long getLongConfig(String configName);


    void setConfig(String name, String value);

    void setConfig(String name, boolean value);

    void setConfig(String name, long value);

    String getActiveSession();
    boolean getAutoDefineLocation();

    void setActiveSession(String session);
    void setAutoDefineLocation(boolean isAllowed);

    String getLocation();
    void setLocation(String locationString);

    String getNotificationTimeAsString();
    void setNotificationTime(int hour, int minute);
    void setNotificationEnabled(boolean isEnabled);
    boolean isNotificationEnabled();

}
