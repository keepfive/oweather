package com.massivekinetics.ow.data.manager;

import com.massivekinetics.ow.data.WeatherForecastChangedListener;
import com.massivekinetics.ow.data.model.WeatherForecast;
import com.massivekinetics.ow.data.tasks.GetWeatherTask;
import com.massivekinetics.ow.data.tasks.LoadingListener;

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
    private WeatherForecast mWeatherForecast = WeatherForecast.NULL;
    private List<WeatherForecastChangedListener> listeners = new ArrayList<WeatherForecastChangedListener>();

    private static WeatherDataManager instance = null;

    private WeatherDataManager() {
    }

    public static WeatherDataManager getInstance(){
        if(instance == null)
            instance = new WeatherDataManager();
        return instance;
    }

    @Override
    public void update() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void getWeatherForecast(LoadingListener<WeatherForecast> listener) {
        if(isForecastUpToDate())
            listener.callback(mWeatherForecast);
        else
            new GetWeatherTask(listener).execute("kiev", "5");
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
    }

    @Override
    public void saveForecast() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void restoreForecast() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    private boolean isForecastUpToDate(){
        return mWeatherForecast!=WeatherForecast.NULL;
    }
}
