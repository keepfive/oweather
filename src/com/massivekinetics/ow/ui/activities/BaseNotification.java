package com.massivekinetics.ow.ui.activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import com.massivekinetics.ow.R;
import com.massivekinetics.ow.application.AppLocator;
import com.massivekinetics.ow.application.IDataManager;
import com.massivekinetics.ow.services.utils.StringUtils;

/**
 * Created with IntelliJ IDEA.
 * User: bovy
 * Date: 5/10/13
 * Time: 11:30 PM
 */
public class BaseNotification extends BaseActivity {
    public static final String ACTION = "com.massivekinetics.ow.notification";
    public static final String NOTIFICATION_ID_KEY = "notification_id_key";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IDataManager dataManager = AppLocator.resolve(IDataManager.class);
        String notificationMessage = dataManager.getNotification();
        if (!StringUtils.isNullOrEmpty(notificationMessage)) {

            //---get the notification ID for the notification;
            // passed in by the MainActivity---
            int notificationId = getIntent().getExtras().getInt(NOTIFICATION_ID_KEY);

            //---PendingIntent to launch activity if the user selects
            // the notification---
            Intent i = new Intent(ForecastPageActivity.ACTION);
            i.putExtra(NOTIFICATION_ID_KEY, notificationId);

            PendingIntent detailsIntent = PendingIntent.getActivity(this, 0, i, 0);

            NotificationManager nm = (NotificationManager)
                    getSystemService(NOTIFICATION_SERVICE);
            Notification notif = new Notification(R.drawable.icon_notification, "oW", System.currentTimeMillis());

            CharSequence from = "oW";

            notif.setLatestEventInfo(this, from, notificationMessage, detailsIntent);
            nm.notify(notificationId, notif);
            //---destroy the activity---
        }
        finish();
    }

    @Override
    protected void initViews() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void initListeners() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}