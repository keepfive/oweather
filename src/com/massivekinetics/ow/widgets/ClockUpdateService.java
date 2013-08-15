package com.massivekinetics.ow.widgets;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import static com.massivekinetics.ow.widgets.oWeatherProvider4x2.updateTime;

/**
 * Created with IntelliJ IDEA.
 * User: bovy
 * Date: 8/7/13
 * Time: 11:28 AM
 * To change this template use File | Settings | File Templates.
 */
public class ClockUpdateService extends Service {
    public static final String ACTION_UPDATE = "com.mk.clock.action.UPDATE";

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
                /**
                 * {@inheritDoc}
                 */
                public void onReceive(Context context, Intent intent) {
                    updateTime(context);
                }
            };

    /**
     * {@inheritDoc}
     */
    public void onCreate() {
        super.onCreate();

        registerReceiver(clockChangedReceiver, intentFilter);
    }

    /**
     * {@inheritDoc}
     */
    public void onDestroy() {
        super.onDestroy();

        unregisterReceiver(clockChangedReceiver);
    }

    /**
     * {@inheritDoc}
     */
    public void onStart(Intent intent, int startId) {
        if (intent != null && intent.getAction() != null) {
            if (intent.getAction().equals(ACTION_UPDATE)) {
                updateTime(this);
            }
        }
    }
}
