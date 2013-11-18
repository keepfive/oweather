package com.massivekinetics.ow.application;

import android.content.SharedPreferences;
import android.os.Build;
import com.massivekinetics.ow.R;

/**
 * Created with IntelliJ IDEA.
 * User: bovy
 * Date: 2/14/13
 * Time: 9:29 AM
 * To change this template use File | Settings | File Templates.
 */
public class Configuration implements IConfiguration {
    private SharedPreferences prefs;
    private boolean isTablet;

    public Configuration() {
        Application app = Application.getInstance();
        prefs = app.getSharedPreferences(SETTINGS, 0);
        isTablet = app.getResources().getBoolean(R.bool.isTablet);
    }

    public String getActiveSession() {
        return prefs.getString(SESSION, null);
    }

    @Override
    public boolean getAutoDefineLocation() {
        return prefs.getBoolean(AUTO_DEFINE_LOCATION_ENABLED, true);
    }

    public void setActiveSession(String session) {
        prefs.edit().putString(SESSION, session).commit();
    }

    @Override
    public void setAutoDefineLocation(boolean isAllowed) {
        prefs.edit().putBoolean(AUTO_DEFINE_LOCATION_ENABLED, isAllowed).commit();
    }

    @Override
    public String getLocationName() {
        return prefs.getString(CITY_NAME, null);
    }

    @Override
    public void setLocationName(String locationName) {
        prefs.edit().putString(CITY_NAME, locationName).commit();
    }

    @Override
    public String getLocationCountry() {
        return prefs.getString(COUNTRY_NAME, null);
    }

    @Override
    public void setLocationCountry(String locationCountry) {
        prefs.edit().putString(COUNTRY_NAME, locationCountry).commit();
    }

    @Override
    public String getLocationCoordinates() {
        return prefs.getString(GPS_PARAMS, null);
    }

    @Override
    public void setLocationCoordinates(String locationCoordinates) {
        prefs.edit().putString(GPS_PARAMS, locationCoordinates).commit();
    }

    @Override
    public void setLocationLastUpdated(long time) {
        prefs.edit().putLong(GPS_LAST_UPDATED, time).commit();
    }

    @Override
    public long getLocationLastUpdated() {
        return prefs.getLong(GPS_LAST_UPDATED, 0);
    }

    @Override
    public String getNotificationTimeAsString() {
        int notificationHour = prefs.getInt(NOTIFICATION_TIME_HOUR, -1);
        int notificationMinute = prefs.getInt(NOTIFICATION_TIME_MINUTE, -1);
        if (notificationHour == -1 || notificationMinute == -1)
            return Application.getInstance().getString(R.string.select_time);
        else {
            String notificationTime = notificationHour + ":" + ((notificationMinute < 10) ? "0" + notificationMinute : notificationMinute);
            return notificationTime;
        }
    }

    @Override
    public void setNotificationTime(int hour, int minute) {
        synchronized (Configuration.class) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt(NOTIFICATION_TIME_HOUR, hour);
            editor.putInt(NOTIFICATION_TIME_MINUTE, minute);
            editor.commit();
        }
    }

    @Override
    public void setNotificationEnabled(boolean isEnabled) {
        prefs.edit().putBoolean(NOTIFICATION_ENABLED, isEnabled).commit();
    }

    @Override
    public boolean isNotificationEnabled() {
        return prefs.getBoolean(NOTIFICATION_ENABLED, false);
    }

    @Override
    public boolean isTemperatureFahrengeitMode() {
        return prefs.getBoolean(TEMPERATURE_MODE_FAHRENHEIT, false);
    }

    @Override
    public void setTemperatureFahrengeitMode(boolean isFahrengeit) {
        prefs.edit().putBoolean(TEMPERATURE_MODE_FAHRENHEIT, isFahrengeit).commit();
    }

    @Override
    public void setNotificationHour(int hour) {
        prefs.edit().putInt(NOTIFICATION_TIME_HOUR, hour).commit();
    }

    @Override
    public int getNotificationHour() {
        return prefs.getInt(NOTIFICATION_TIME_HOUR, -1);
    }

    @Override
    public void setNotificationMinute(int minute) {
        prefs.edit().putInt(NOTIFICATION_TIME_MINUTE, minute).commit();
    }

    @Override
    public int getNotificationMinute() {
        return prefs.getInt(NOTIFICATION_TIME_MINUTE, -1);
    }

    @Override
    public void setWidgetBackground(int widgetId, int color) {
        prefs.edit().putInt(WIDGET_BACKGROUND + widgetId, color).commit();
    }

    @Override
    public int getWidgetBackground(int widgetId) {
        int color = prefs.getInt(WIDGET_BACKGROUND + widgetId, -1);
        if (color == -1)
            return Application.getInstance().getResources().getColor(R.color.widget_bg_black);
        else
            return color;
    }

    @Override
    public boolean isTablet() {
        return isTablet;
    }


    public String getDeviceId() {

			/*
			 * Taken from http://www.pocketmagic.net/?p=1662 Pseudo-Unique ID, that works on all Android devices Some
			 * devices don't have a phone (eg. Tablets) or for some reason you don't want to include the
			 * READ_PHONE_STATE permission. You can still read details like ROM Version, Manufacturer name, CPU type,
			 * and other hardware details, that will be well suited if you want to use the ID for a serial key check, or
			 * other general purposes. The ID computed in this way won't be unique: it is possible to find two devices
			 * with the same ID (based on the same hardware and rom image) but the chances in real world applications
			 * are negligible. For this purpose Build class is used:
			 */

        String pseudoDeviceIMEI = "35" + // we make this look like a valid IMEI
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +
                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +
                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +
                Build.USER.length() % 10; // 13 digits

        return pseudoDeviceIMEI;

    }


}
