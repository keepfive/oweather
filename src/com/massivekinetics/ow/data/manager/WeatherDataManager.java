package com.massivekinetics.ow.data.manager;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.massivekinetics.ow.R;
import com.massivekinetics.ow.application.OWApplication;
import com.massivekinetics.ow.data.WeatherForecastChangedListener;
import com.massivekinetics.ow.data.model.WeatherForecast;
import com.massivekinetics.ow.data.model.WeatherModel;
import com.massivekinetics.ow.data.tasks.GetWeatherTask;
import com.massivekinetics.ow.data.tasks.LoadingListener;
import com.massivekinetics.ow.network.NetworkUtils;
import com.massivekinetics.ow.utils.DateUtils;
import com.massivekinetics.ow.widgets.ClockUpdateService;
import com.massivekinetics.ow.widgets.oWeatherProvider4x1;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: bovy
 * Date: 2/3/13
 * Time: 12:42 AM
 * To change this template use File | Settings | File Templates.
 */
public class WeatherDataManager implements DataManager {
    private static final String TAG = "WeatherDataManager";
    private static WeatherDataManager instance = null;
    private WeatherForecast mWeatherForecast = WeatherForecast.NULL;
    private List<WeatherForecastChangedListener> listeners = new ArrayList<WeatherForecastChangedListener>();

    private static final long EXPIRATION_ONLINE = 3 * 60 * 60 * 1000;  // 3 hours
    private static final long EXPIRATION_OFFLINE = 5 * (24 - 1) * 60 * 60 * 1000;  // 5 days minus 5 hours --> (24 - 1) just for convenience.


    private WeatherDataManager() {
    }

    public static WeatherDataManager getInstance() {

        synchronized (WeatherDataManager.class) {
            if (instance == null)
                instance = new WeatherDataManager();
        }
        return instance;

    }

    @Override
    public void runUpdate(LoadingListener<WeatherForecast> listener) {
        new GetWeatherTask(new OWConfigManager(), listener).execute();
    }

    @Override
    public void getWeatherForecast(LoadingListener<WeatherForecast> listener) {
        if (hasActualForecast())
            listener.callback(mWeatherForecast);
        else
            runUpdate(listener);
    }

    @Override
    public void addWeatherForecastChangedListener(WeatherForecastChangedListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeWeatherForecastChangedListener(WeatherForecastChangedListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void updateForecast(WeatherForecast forecast) {
        this.mWeatherForecast = forecast;
        saveForecast();
        OWApplication.getInstance().startService(new Intent(ClockUpdateService.ACTION_UPDATE));
    }

    @Override
    public void saveForecast() {
        try {
            File cache = new File(OWApplication.getInstance().getCacheDir(), "oweather.dat");
            cache.createNewFile();
            FileOutputStream fos = new FileOutputStream(cache);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(mWeatherForecast);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void restoreForecast() {
        try {
            File cache = new File(OWApplication.getInstance().getCacheDir(), "oweather.dat");
            if (cache.exists()) {
                FileInputStream input = new FileInputStream(cache);
                ObjectInputStream ois = new ObjectInputStream(input);
                WeatherForecast forecast = (WeatherForecast) ois.readObject();
                this.mWeatherForecast = forecast;
            }

        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public boolean hasActualForecast() {
        if(mWeatherForecast == null || mWeatherForecast == WeatherForecast.NULL)
            restoreForecast();

        String gpsLocation = OWApplication.getInstance().getConfigManager().getLocationCoordinates();
        long weatherLifeInMsec = DateUtils.getCurrentInMillis()- mWeatherForecast.getTimeStamp();
        long expirationLimit = NetworkUtils.isOnline() ? EXPIRATION_ONLINE : EXPIRATION_OFFLINE;

        boolean isActual = !mWeatherForecast.getForecastList().isEmpty() && gpsLocation.equals(mWeatherForecast.getLocationString())
                && weatherLifeInMsec < expirationLimit;

        return isActual;
    }

    @Override
    public WeatherModel getCurrentWeather() {
        if (hasActualForecast()) {
            DateUtils dateUtils = new DateUtils();
            for (WeatherModel model : mWeatherForecast.getForecastList()) {
                if (dateUtils.isToday(model.getDate())) {
                    return model;
                }
            }
        }
        else
            runUpdate(null);
        return WeatherModel.NULL;
    }

    @Override
    public String getNotification() {
        String notification = null;

        if (hasActualForecast()) {
            DateUtils dateUtils = new DateUtils();
            for (WeatherModel model : mWeatherForecast.getForecastList()) {
                if (dateUtils.isToday(model.getDate())) {
                    char degree = '\u00B0';
                    String degreeName = degree + (OWApplication.getInstance().getConfigManager().isTemperatureFahrengeitMode() ? "F" : "C");
                    String notificationPattern = OWApplication.getInstance().getString(R.string.notification_pattern);

                    notification = notificationPattern.replace("[DESC]", model.getState().getValue())
                            .replace("[TEMP_MAX]", model.getMaxTemperature() + degreeName)
                            .replace("[TEMP_MIN]", model.getMinTemperature() + degreeName);
                }
            }
        }

        return notification;

    }

   /* private void updateWidget(Context context){
        AppWidgetManager man = AppWidgetManager.getInstance(context);
        int[] ids = man.getAppWidgetIds(new ComponentName(context,oWeatherProvider4x1.class));
        Intent updateIntent = new Intent();
        updateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        context.sendBroadcast(updateIntent);
    } */
}
