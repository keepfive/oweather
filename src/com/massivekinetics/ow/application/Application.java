package com.massivekinetics.ow.application;

import android.util.DisplayMetrics;
import com.massivekinetics.ow.application.location.GeoLocator;
import com.massivekinetics.ow.application.location.IGeoLocator;

public class Application extends android.app.Application {
    private static Application context;

    private DisplayMetrics displayMetrics;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        initCoreDependencies();
    }

    public static Application getInstance(){
        return context;
    }

    private void initCoreDependencies(){
        AppLocator.init();
        AppLocator.register(IConfiguration.class, new Configuration());
        AppLocator.register(IGeoLocator.class, new GeoLocator());
        AppLocator.register(DisplayMetrics.class, getDisplayMetrics());
        AppLocator.register(IDataManager.class, new WeatherDataManager());
    }

    public DisplayMetrics getDisplayMetrics() {
        return displayMetrics;
    }

    public void setDisplayMetrics(DisplayMetrics displayMetrics) {
        this.displayMetrics = displayMetrics;
    }


}
