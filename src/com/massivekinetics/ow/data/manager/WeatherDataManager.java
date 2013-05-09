package com.massivekinetics.ow.data.manager;

import android.util.Log;
import com.massivekinetics.ow.application.OWApplication;
import com.massivekinetics.ow.data.WeatherForecastChangedListener;
import com.massivekinetics.ow.data.model.WeatherForecast;
import com.massivekinetics.ow.data.tasks.GetWeatherTask;
import com.massivekinetics.ow.data.tasks.LoadingListener;
import com.massivekinetics.ow.utils.DateUtils;

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

    private WeatherDataManager() {
    }

    public static WeatherDataManager getInstance() {

        synchronized (WeatherDataManager.class){
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
            if(cache.exists()){
                FileInputStream input = new FileInputStream(cache);
                ObjectInputStream ois = new ObjectInputStream(input);
                WeatherForecast forecast = (WeatherForecast)ois.readObject();
                this.mWeatherForecast = forecast;
            }

        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public boolean hasActualForecast() {
        String gpsLocation = OWApplication.getInstance().getConfigManager().getLocation();
        long today = DateUtils.getCurrentInMillis();
        boolean isActual = !mWeatherForecast.getForecastList().isEmpty()
                && gpsLocation.equals(mWeatherForecast.getLocationString())
                && (today - mWeatherForecast.getTimeStamp()) < 5 * 24 * 60 * 60 * 1000;
        return isActual;
    }
}
