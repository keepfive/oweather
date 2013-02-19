package com.massivekinetics.ow.data.manager;

import android.content.SharedPreferences;
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
        prefs = OWApplication.context.getSharedPreferences(SETTINGS, 0);
    }

    @Override
    public String getStringConfig(String configName) {
        return prefs.getString(configName, "");
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
    public void setConfig(String name, String value) {
        prefs.edit().putString(name, value).commit();
    }

    @Override
    public void setConfig(String name, boolean value) {
        prefs.edit().putBoolean(name, value).commit();
    }

    @Override
    public void setConfig(String name, int value) {
        prefs.edit().putInt(name, value).commit();
    }


}
