package com.massivekinetics.ow.widgets;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.RemoteViews;
import com.massivekinetics.ow.R;
import com.massivekinetics.ow.application.OWApplication;
import com.massivekinetics.ow.data.manager.ConfigManager;
import com.massivekinetics.ow.data.manager.DataManager;
import com.massivekinetics.ow.data.manager.OWConfigManager;
import com.massivekinetics.ow.data.manager.WeatherDataManager;
import com.massivekinetics.ow.data.model.WeatherModel;
import com.massivekinetics.ow.states.WeatherState;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class oWeatherProvider4x2 extends AppWidgetProvider {
    ConfigManager settings;

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

    public static void updateTime(Context context) {
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

            String locality = config.getLocationName();
            String country = config.getLocationCountry();

            WeatherModel currentWeather = dataManager.getCurrentWeather();
            if (currentWeather == WeatherModel.NULL) {

            } else {
                WeatherState weatherState = currentWeather.getState();
                String state = weatherState.getValue();
                remoteViews.setTextViewText(R.id.weatherState, state);
                String max = currentWeather.getMaxTemperature();
                String min = currentWeather.getMinTemperature();
                if (!config.isTemperatureFahrengeitMode() && (!max.contains("-") || !max.trim().equals("0")))
                    max = "+" + max;

                if (!config.isTemperatureFahrengeitMode() && (!min.contains("-") || !min.trim().equals("0")))
                    min = "+" + min;

                remoteViews.setTextViewText(R.id.max, max);
                remoteViews.setTextViewText(R.id.min, min);
            }

            //updating views
            if (locality == null)
                locality = context.getString(R.string.no_location);

            remoteViews.setTextViewText(R.id.locality, locality);

            if (country != null)
                remoteViews.setTextViewText(R.id.country, country);
            int countryVisibility = (country == null) ? View.INVISIBLE : View.VISIBLE;
            remoteViews.setViewVisibility(R.id.country, countryVisibility);

            remoteViews.setTextColor(R.id.time, Color.WHITE);
            remoteViews.setTextViewText(R.id.time, time);
            remoteViews.setTextViewText(R.id.date, date);

            remoteViews.setViewVisibility(R.id.amPm, amPmVisibility);
            if (!is24hour)
                remoteViews.setTextViewText(R.id.amPm, amPm);

            remoteViews.setInt(R.id.widget_content, "setBackgroundColor", config.getWidgetBackground(wid));
            appWidgetManager.updateAppWidget(wid, remoteViews);
        }
    }
}