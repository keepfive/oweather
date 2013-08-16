package com.massivekinetics.ow.widgets;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import static com.massivekinetics.ow.widgets.oWeatherProvider4x2.updateLocation;
import static com.massivekinetics.ow.widgets.oWeatherProvider4x2.updateTime;
import static com.massivekinetics.ow.widgets.oWeatherProvider4x2.updateWeather;

/**
 * Created with IntelliJ IDEA.
 * User: bovy
 * Date: 8/7/13
 * Time: 11:28 AM
 * To change this template use File | Settings | File Templates.
 */
public class ClockUpdateService extends Service {
    public static final String ACTION_UPDATE = "com.mk.clock.action.UPDATE";
    public static final String ACTION_WEATHER_UPDATED = "com.mk.clock.action.UPDATED_WEATHER";
    public static final String ACTION_LOCATION_UPDATED = "com.mk.clock.action.UPDATED_LOCATION";

    private final static IntentFilter intentFilter;

    static {
        intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_TIME_TICK);
        intentFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        intentFilter.addAction(Intent.ACTION_TIME_CHANGED);
        intentFilter.addAction(ACTION_UPDATE);
    }


    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * BroadcastReceiver receiving the updates.
     */
    private final BroadcastReceiver clockChangedReceiver = new
            BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    updateTime(context);
                }
            };

    public void onCreate() {
        super.onCreate();
        registerReceiver(clockChangedReceiver, intentFilter);
    }

    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(clockChangedReceiver);
    }

    public void onStart(Intent intent, int startId) {
        if (intent != null && intent.getAction() != null) {
            if (intent.getAction().equals(ACTION_UPDATE)) {
                updateTime(this);
                updateLocation(this);
                updateWeather(this);
            } else if (intent.getAction().equals(ACTION_LOCATION_UPDATED)) {
                updateLocation(this);
            } else if (intent.getAction().equals(ACTION_WEATHER_UPDATED)) {
                updateWeather(this);
            }
        }
    }
}
