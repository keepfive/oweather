package com.massivekinetics.ow.application.manager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.massivekinetics.ow.application.AppLocator;
import com.massivekinetics.ow.application.Configuration;
import com.massivekinetics.ow.ui.activities.BaseActivity;
import com.massivekinetics.ow.application.Application;
import com.massivekinetics.ow.application.IConfiguration;
import com.massivekinetics.ow.application.receivers.NotificationReceiver;

import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: bovy
 * Date: 5/7/13
 * Time: 1:06 AM
 */
public class NotificationService {

    public static void turnOn(){
        toggle(true);
    }
    public  static void turnOff(){
        toggle(false);
    }

    public static void reset(){
        turnOff();
        turnOn();
    }

    public static void toggle(boolean isOn) {
        IConfiguration configuration = Configuration.INSTANCE();
        AlarmManager alarmManager = (AlarmManager) Application.getInstance().getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(NotificationReceiver.ACTION);
        PendingIntent displayIntent = PendingIntent.getBroadcast(BaseActivity.getActiveInstance().getBaseContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (isOn) {
            //---get current date and time---
            Calendar calendar = Calendar.getInstance();
            int hour = configuration.getNotificationHour();
            int minute = configuration.getNotificationMinute();
            if (hour == -1 || minute == -1)
                return;

            //---sets the time for the alarm to trigger---
            calendar.set(Calendar.HOUR_OF_DAY, configuration.getNotificationHour());
            calendar.set(Calendar.MINUTE, configuration.getNotificationMinute());
            calendar.set(Calendar.SECOND, 0);

            //---sets the alarm to trigger--
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, displayIntent);
        } else {
            alarmManager.cancel(displayIntent);
        }
    }
}
