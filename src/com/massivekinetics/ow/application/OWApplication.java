package com.massivekinetics.ow.application;

import android.app.Application;
import com.massivekinetics.ow.data.manager.DataManager;
import com.massivekinetics.ow.location.OWLocationManager;

public class OWApplication extends Application {
    public static OWApplication context;
    private OWLocationManager locationManager;
    private DataManager dataManager;

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


}
