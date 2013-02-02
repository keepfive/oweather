package com.massivekinetics.ow.data.manager;

import com.massivekinetics.ow.data.WeatherForecast;
import com.massivekinetics.ow.data.WeatherForecastChangedListener;

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
    private WeatherForecast mWeatherForecast;
    private List<WeatherForecastChangedListener> listeners = new ArrayList<WeatherForecastChangedListener>();

    public WeatherDataManager(WeatherForecast weatherForecast) {
        mWeatherForecast = weatherForecast;
    }

    @Override
    public void update() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public WeatherForecast getWeatherForecast() {
        return mWeatherForecast;
    }

    @Override
    public void addWeatherForecastChangedListener(WeatherForecastChangedListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeWeatherForecastChangedListener(WeatherForecastChangedListener listener) {
        listeners.remove(listener);
    }
}
