package com.massivekinetics.ow.data.manager;

import com.massivekinetics.ow.data.WeatherForecast;
import com.massivekinetics.ow.data.WeatherForecastChangedListener;

/**
 * Created with IntelliJ IDEA.
 * User: bovy
 * Date: 2/3/13
 * Time: 12:36 AM
 * To change this template use File | Settings | File Templates.
 */
public interface DataManager {
    void update();
    WeatherForecast getWeatherForecast();
    void addWeatherForecastChangedListener(WeatherForecastChangedListener listener);
    void removeWeatherForecastChangedListener(WeatherForecastChangedListener listener);
}
