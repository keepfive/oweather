package com.massivekinetics.ow.receivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.massivekinetics.ow.R;
import com.massivekinetics.ow.activities.ForecastPageActivity;
import com.massivekinetics.ow.application.Application;
import com.massivekinetics.ow.utils.StringUtils;

/**
 * Created with IntelliJ IDEA.
 * User: bovy
 * Date: 5/14/13
 * Time: 1:57 PM
 */
public class NotificationReceiver extends BroadcastReceiver {
    public static final String ACTION = "com.massivekinetics.ow.notification";
    public static final String NOTIFICATION_ID_KEY = "notification_id_key";

    public void onReceive(Context context, Intent intent) {

        String notificationMessage = Application.getInstance().getDataManager().getNotification();
        if (!StringUtils.isNullOrEmpty(notificationMessage)) {

            //---get the notification ID for the notification;
            // passed in by the MainActivity---
            int notificationId = intent.getExtras().getInt(NOTIFICATION_ID_KEY);

            //---PendingIntent to launch activity if the user selects
            // the notification---
            Intent i = new Intent(ForecastPageActivity.ACTION);
            i.putExtra(NOTIFICATION_ID_KEY, notificationId);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent detailsIntent = PendingIntent.getActivity(context, 0, i, 0);

            NotificationManager nm = (NotificationManager) Application.getInstance().getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notif = new Notification(R.drawable.icon_notification, "oW", System.currentTimeMillis());

            CharSequence from = "oW";

            notif.setLatestEventInfo(Application.getInstance(), from, notificationMessage, detailsIntent);
            nm.notify(notificationId, notif);
            //---destroy the activity---
        }

    }
}
