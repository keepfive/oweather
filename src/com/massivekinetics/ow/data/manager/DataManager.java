package com.massivekinetics.ow.data.manager;

import com.massivekinetics.ow.data.WeatherForecastChangedListener;
import com.massivekinetics.ow.data.model.WeatherForecast;
import com.massivekinetics.ow.data.model.WeatherModel;
import com.massivekinetics.ow.data.tasks.LoadingListener;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: bovy
 * Date: 2/3/13
 * Time: 12:36 AM
 * To change this template use File | Settings | File Templates.
 */
public interface DataManager extends Serializable {

    void runUpdate(LoadingListener<WeatherForecast> listener);

    public void getWeatherForecast(LoadingListener<WeatherForecast> listener);

    void updateForecast(WeatherForecast forecast);
    void saveForecast();
    void restoreForecast();
    boolean hasActualForecast();
    WeatherModel getCurrentWeather();
    String getNotification();
}
