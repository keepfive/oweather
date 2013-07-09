package com.massivekinetics.ow.data.manager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.massivekinetics.ow.activities.OWActivity;
import com.massivekinetics.ow.activities.OWNotification;
import com.massivekinetics.ow.application.OWApplication;
import com.massivekinetics.ow.receivers.NotificationReceiver;

import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: bovy
 * Date: 5/7/13
 * Time: 1:06 AM
 */
public class NotificationService {
    public static void turnNotification(boolean isOn) {
        ConfigManager configManager = OWApplication.getInstance().getConfigManager();
        //---use the AlarmManager to trigger an alarm---
        AlarmManager alarmManager = (AlarmManager) OWApplication.getInstance().getSystemService(Context.ALARM_SERVICE);

        //---PendingIntent to launch activity when the alarm triggers---
        Intent intent = new Intent(NotificationReceiver.ACTION);

        //---assign an ID of 1---
        intent.putExtra(OWNotification.NOTIFICATION_ID_KEY, 1);

        PendingIntent displayIntent = PendingIntent.getBroadcast(OWActivity.current.getBaseContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (isOn) {

            //---get current date and time---
            Calendar calendar = Calendar.getInstance();
            int hour = configManager.getNotificationHour();
            int minute = configManager.getNotificationMinute();
            if (hour == -1 || minute == -1)
                return;

            //---sets the time for the alarm to trigger---
            calendar.set(Calendar.HOUR_OF_DAY, configManager.getNotificationHour());
            calendar.set(Calendar.MINUTE, configManager.getNotificationMinute());
            calendar.set(Calendar.SECOND, 0);

            //---sets the alarm to trigger--
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, displayIntent);
        } else {
            alarmManager.cancel(displayIntent);
        }


    }
}
