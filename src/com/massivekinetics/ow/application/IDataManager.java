package com.massivekinetics.ow.application;

import com.massivekinetics.ow.domain.model.WeatherForecast;
import com.massivekinetics.ow.domain.model.WeatherModel;
import com.massivekinetics.ow.ui.interfaces.LoadingListener;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: bovy
 * Date: 2/3/13
 * Time: 12:36 AM
 * To change this template use File | Settings | File Templates.
 */
public interface IDataManager extends Serializable {

    void runUpdate(LoadingListener<WeatherForecast> listener);

    public void getWeatherForecast(LoadingListener<WeatherForecast> listener);

    void updateForecast(WeatherForecast forecast);
    void saveForecast();
    void restoreForecast();
    boolean hasActualForecast();
    WeatherModel getCurrentWeather();
    String getNotification();
}
