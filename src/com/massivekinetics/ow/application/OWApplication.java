package com.massivekinetics.ow.application;

import android.app.Application;
import android.util.DisplayMetrics;
import com.massivekinetics.ow.data.manager.DataManager;
import com.massivekinetics.ow.location.OWLocationManager;

public class OWApplication extends Application {
    public static OWApplication context;
    private OWLocationManager locationManager;
    private DataManager dataManager;

    private DisplayMetrics displayMetrics;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        locationManager = new OWLocationManager();
    }

    public OWLocationManager getLocationManager() {
        return locationManager;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public void setDataManager(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public DisplayMetrics getDisplayMetrics() {
        return displayMetrics;
    }

    public void setDisplayMetrics(DisplayMetrics displayMetrics) {
        this.displayMetrics = displayMetrics;
    }

}
