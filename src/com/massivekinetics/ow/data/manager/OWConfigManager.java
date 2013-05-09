package com.massivekinetics.ow.data.manager;

import android.content.SharedPreferences;
import com.massivekinetics.ow.R;
import com.massivekinetics.ow.application.OWApplication;

/**
 * Created with IntelliJ IDEA.
 * User: bovy
 * Date: 2/14/13
 * Time: 9:29 AM
 * To change this template use File | Settings | File Templates.
 */
public class OWConfigManager implements ConfigManager {
    SharedPreferences prefs;

    public OWConfigManager(){
        prefs = OWApplication.getInstance().getSharedPreferences(SETTINGS, 0);
    }

    @Override
    public String getStringConfig(String configName) {
        return prefs.getString(configName, null);
    }

    @Override
    public boolean getBooleanConfig(String configName) {
        return prefs.getBoolean(configName, false);
    }

    @Override
    public int getIntConfig(String configName) {
        return prefs.getInt(configName, 0);
    }

    @Override
    public long getLongConfig(String configName) {
        return prefs.getLong(configName, 0);
    }

    @Override
    public void setConfig(String name, String value) {
        prefs.edit().putString(name, value).commit();
    }

    @Override
    public void setConfig(String name, boolean value) {
        prefs.edit().putBoolean(name, value).commit();
    }

    @Override
    public void setConfig(String name, long value) {
        prefs.edit().putLong(name, value).commit();
    }

    public String getActiveSession(){
        return prefs.getString(SESSION, null);
    }

    @Override
    public boolean getAutoDefineLocation() {
        return prefs.getBoolean(AUTO_DEFINE_LOCATION_ENABLED, true);
    }

    public void setActiveSession(String session){
        prefs.edit().putString(SESSION, session).commit();
    }

    @Override
    public void setAutoDefineLocation(boolean isAllowed) {
        prefs.edit().putBoolean(AUTO_DEFINE_LOCATION_ENABLED, isAllowed).commit();
    }

    @Override
    public String getLocation() {
        return prefs.getString(GPS_PARAMS, null);
    }

    @Override
    public void setLocation(String locationString) {
        prefs.edit().putString(GPS_PARAMS, locationString).commit();
    }

    @Override
    public String getNotificationTimeAsString() {
        int notificationHour = prefs.getInt(NOTIFICATION_TIME_HOUR, -1);
        int notificationMinute = prefs.getInt(NOTIFICATION_TIME_MINUTE, -1);
        if(notificationHour == -1 || notificationMinute == -1)
            return OWApplication.getInstance().getString(R.string.select_time);
        else {
            String notificationTime = notificationHour + ":" + ((notificationMinute < 10) ? "0" + notificationMinute : notificationMinute);
            return notificationTime;
        }
    }

    @Override
    public void setNotificationTime(int hour, int minute) {
        synchronized (OWConfigManager.class){
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

}
