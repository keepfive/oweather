package com.massivekinetics.ow.application.receivers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import com.massivekinetics.ow.R;
import com.massivekinetics.ow.application.AppLocator;
import com.massivekinetics.ow.application.IDataManager;
import com.massivekinetics.ow.ui.activities.ForecastPageActivity;
import com.massivekinetics.ow.application.Application;
import com.massivekinetics.ow.services.utils.StringUtils;

/**
 * Created with IntelliJ IDEA.
 * User: bovy
 * Date: 5/14/13
 * Time: 1:57 PM
 */
public class NotificationReceiver extends BroadcastReceiver {
    public static final String ACTION = "com.massivekinetics.ow.notification";
    public static final String NOTIFICATION_ID_KEY = "notification_id_key";
    public static final int NOTIFICATION_ID = 0x11110111;

    public void onReceive(Context context, Intent intent) {
        IDataManager dataManager = AppLocator.resolve(IDataManager.class);
        String notificationMessage = dataManager.getNotification();
        if (!StringUtils.isNullOrEmpty(notificationMessage)) {

            NotificationManager manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            manager.cancel(NOTIFICATION_ID);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);

            Intent i = new Intent(ForecastPageActivity.ACTION);
            //i.putExtra(NOTIFICATION_ID_KEY, notificationId);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent detailsIntent = PendingIntent.getActivity(context, 0, i, 0);

            notificationBuilder.setContentIntent(detailsIntent).setSmallIcon(R.drawable.icon_notification).
                    setContentTitle("oW").setContentText(notificationMessage).
                    setWhen(System.currentTimeMillis()).
                    setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)).
                    setVibrate(new long[]{100, 100}).setAutoCancel(true);

            manager.notify(NOTIFICATION_ID, notificationBuilder.getNotification());
        }

    }
}
