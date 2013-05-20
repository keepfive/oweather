package com.massivekinetics.ow.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.RemoteViews;
import com.massivekinetics.ow.R;
import com.massivekinetics.ow.application.OWApplication;
import com.massivekinetics.ow.data.model.WeatherModel;
import com.massivekinetics.ow.states.WeatherState;
import com.massivekinetics.ow.utils.BitmapUtils;
import com.massivekinetics.ow.utils.ResourcesCodeUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class oWeatherProvider4x1 extends AppWidgetProvider {

	public static final String ACTION = "android.appwidget.action.APPWIDGET_UPDATE";


	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
        SimpleDateFormat dataFormatter = new SimpleDateFormat("dd.MM.yyyy");
		// Get all ids
		ComponentName thisWidget = new ComponentName(context, oWeatherProvider4x1.class);
		int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
		for (int widgetId : allWidgetIds) {

            WeatherModel current = OWApplication.getInstance().getDataManager().getCurrentWeather();

            String cityStr = OWApplication.getInstance().getConfigManager().getLocationName();
            String todayDate = "Today / " + dataFormatter.format(new Date());
            String weatherDescStr = "";
            String currentTemp = "";
            String nightTempStr = "";
            int backgroundColor = R.color.ow_sunny;
            int weatherImage = R.drawable.weather_state_1;

            if(current != WeatherModel.NULL){
                cityStr = (cityStr == null) ? "Unknown" : cityStr;
                weatherDescStr = current.getState().getValue();
                currentTemp = current.getMaxTemperature();
                nightTempStr = current.getMinTemperature();
                WeatherState weatherState = current.getState();
                backgroundColor = ResourcesCodeUtils.getWeatherBackgroundColor(weatherState);
                weatherImage = ResourcesCodeUtils.getWeatherImageResource(weatherState);
            }

			RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
					R.layout.widget_img_4x1);

            Bitmap city = BitmapUtils.getStringAsBitmap(cityStr, 40, 4, 35, 11);
            Bitmap date =  BitmapUtils.getStringAsBitmap(todayDate, 20, 5, 28, -1);
            Bitmap weatherDesc = BitmapUtils.getStringAsBitmap(weatherDescStr, 28, 10, 32, -1);
            Bitmap night = BitmapUtils.getStringAsBitmapWithSize("Night", 24, 0, 23, -1, 40, 20);

            int xOffset = nightTempStr.length() > 1 ? 5 : 10;
            Bitmap nightTemp = BitmapUtils.getStringAsBitmapWithSize(nightTempStr, 27, xOffset, 29, -1, 40, 20);

            xOffset = currentTemp.length() > 1 ? 7 : 60;
            xOffset = currentTemp.length() > 2 ? -27 : xOffset;
            int textSize = currentTemp.length() > 2 ? 102 : 110;
            Bitmap temp = BitmapUtils.getStringAsBitmap2(currentTemp, 102, xOffset, 88);

            remoteViews.setImageViewBitmap(R.id.ivCity, city);
            remoteViews.setImageViewBitmap(R.id.ivDate, date);
            remoteViews.setImageViewBitmap(R.id.ivWeatherDesc, weatherDesc);
            remoteViews.setImageViewBitmap(R.id.ivTemp, temp);
            remoteViews.setImageViewBitmap(R.id.ivDaytime, night);
            remoteViews.setImageViewBitmap(R.id.ivNightTemp, nightTemp);
            remoteViews.setImageViewResource(R.id.ivWeather, weatherImage);
            remoteViews.setImageViewResource(R.id.background, backgroundColor);
            //remoteViews.setTextViewText(R.id.tvCity, cities[ran]);

            Intent intent = new Intent(context, oWeatherProvider4x1.class);

			intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
			intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

			PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			remoteViews.setOnClickPendingIntent(R.id.main, pendingIntent);
			appWidgetManager.updateAppWidget(widgetId, remoteViews);
		}
	}
}
