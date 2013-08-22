package com.massivekinetics.ow.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import com.massivekinetics.ow.R;
import com.massivekinetics.ow.activities.SettingsActivity;
import com.massivekinetics.ow.activities.SplashScreenActivity;
import com.massivekinetics.ow.activities.UpdatePageActivity;
import com.massivekinetics.ow.application.OWApplication;
import com.massivekinetics.ow.data.manager.ConfigManager;
import com.massivekinetics.ow.data.manager.DataManager;
import com.massivekinetics.ow.data.manager.OWConfigManager;
import com.massivekinetics.ow.data.manager.WeatherDataManager;
import com.massivekinetics.ow.data.model.WeatherModel;
import com.massivekinetics.ow.states.WeatherState;
import com.massivekinetics.ow.utils.NavigationService;
import com.massivekinetics.ow.utils.ResourcesCodeUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class oWeatherProvider4x2 extends AppWidgetProvider {

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    public void onDisabled(Context context) {
        super.onDisabled(context);
        context.stopService(new Intent(context, ClockUpdateService.class));
    }

    public void onEnabled(Context context) {
        super.onEnabled(context);
        context.startService(new Intent(ClockUpdateService.ACTION_UPDATE));
    }

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        context.startService(new Intent(ClockUpdateService.ACTION_UPDATE));
    }

    public static void updateWeather(Context context) {
        Log.e("abw", "updateWeather");
        ComponentName clockWidget = new ComponentName(context, oWeatherProvider4x2.class);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        buildUpdate(context, appWidgetManager, clockWidget);
    }

    private static void buildUpdate(Context context, AppWidgetManager appWidgetManager, ComponentName componentName) {
        ConfigManager config = new OWConfigManager();
        DataManager dataManager = WeatherDataManager.getInstance();

        int[] ids = appWidgetManager.getAppWidgetIds(componentName);

        for (int wid : ids) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_4x2);

            //system values
            boolean is24hour = DateFormat.is24HourFormat(context);
            Calendar calendar = Calendar.getInstance();

            //updated values
            int amPmVisibility = (is24hour) ? View.GONE : View.VISIBLE;
            String timeFormat = (is24hour) ? "HH:mm" : "h:mm";
            String dateFormat = "E, dd MMM";
            String amPm = (calendar.get(Calendar.HOUR_OF_DAY) >= 12) ? "PM" : "AM";
            String time = new SimpleDateFormat(timeFormat).format(calendar.getTime());
            String date = new SimpleDateFormat(dateFormat, Locale.ENGLISH).format(calendar.getTime());

            remoteViews.setTextViewText(R.id.time, time);
            remoteViews.setTextViewText(R.id.date, date);

            remoteViews.setViewVisibility(R.id.amPm, amPmVisibility);
            if (!is24hour)
                remoteViews.setTextViewText(R.id.amPm, amPm);


            //---------------------------
            String locality = config.getLocationName();
            String country = config.getLocationCountry();
            //updating views
            if (locality == null)
                locality = context.getString(R.string.no_location);

            remoteViews.setTextViewText(R.id.locality, locality);

            if (country != null)
                remoteViews.setTextViewText(R.id.country, country);
            int countryVisibility = (country == null) ? View.INVISIBLE : View.VISIBLE;
            remoteViews.setViewVisibility(R.id.country, countryVisibility);
            //---------------------------

            WeatherModel currentWeather = dataManager.getCurrentWeather();
            if (currentWeather == WeatherModel.NULL) {
                remoteViews.setInt(R.id.dataAvailable, "setVisibility", View.GONE);
                remoteViews.setInt(R.id.dataNotAvailable, "setVisibility", View.VISIBLE);
            } else {
                remoteViews.setInt(R.id.dataAvailable, "setVisibility", View.VISIBLE);
                remoteViews.setInt(R.id.dataNotAvailable, "setVisibility", View.GONE);

                WeatherState weatherState = currentWeather.getState();
                String state = weatherState.getValue();
                int stateResourceId = ResourcesCodeUtils.getWidgetWeatherImageResource(weatherState);

                remoteViews.setTextViewText(R.id.weatherState, state);
                remoteViews.setImageViewResource(R.id.image, stateResourceId);
                String max = currentWeather.getMaxTemperature();
                String min = currentWeather.getMinTemperature();
                if (!config.isTemperatureFahrengeitMode() && Integer.parseInt(max) > 0)
                    max = "+" + max;

                if (!config.isTemperatureFahrengeitMode() && Integer.parseInt(min) > 0)
                    min = "+" + min;

                remoteViews.setTextViewText(R.id.max, max);
                remoteViews.setTextViewText(R.id.min, min);
            }

            remoteViews.setInt(R.id.widget_content, "setBackgroundColor", config.getWidgetBackground(wid));

            setOnClickListeners(context, remoteViews);

            appWidgetManager.updateAppWidget(wid, remoteViews);
        }
    }

    private static void setOnClickListeners(Context context, RemoteViews remoteViews) {
        Intent runAlarmClock = NavigationService.alarmIntent(context);
        Intent runOweatherIntent = new Intent(context, SplashScreenActivity.class);
        Intent runSettingsIntent = new Intent(context, SettingsActivity.class);

        PendingIntent onClickPending;
        if (runAlarmClock == null)
            runAlarmClock = runOweatherIntent;

        runAlarmClock.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        onClickPending = PendingIntent.getActivity(context, 0, runAlarmClock, 0);
        remoteViews.setOnClickPendingIntent(R.id.timeContainer, onClickPending);
        remoteViews.setOnClickPendingIntent(R.id.time, onClickPending);
        remoteViews.setOnClickPendingIntent(R.id.amPm, onClickPending);


        runOweatherIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        onClickPending = PendingIntent.getActivity(context, 0, runOweatherIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.dataAvailable, onClickPending);
        remoteViews.setOnClickPendingIntent(R.id.dataNotAvailable, onClickPending);
        remoteViews.setOnClickPendingIntent(R.id.image, onClickPending);
        remoteViews.setOnClickPendingIntent(R.id.imageError, onClickPending);
        remoteViews.setOnClickPendingIntent(R.id.weatherState, onClickPending);
        remoteViews.setOnClickPendingIntent(R.id.max, onClickPending);
        remoteViews.setOnClickPendingIntent(R.id.min, onClickPending);

        runSettingsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        onClickPending = PendingIntent.getActivity(context, 0, runSettingsIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.locality, onClickPending);
        remoteViews.setOnClickPendingIntent(R.id.country, onClickPending);

    }
}