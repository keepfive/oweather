package com.massivekinetics.ow.data.tasks;

import android.os.AsyncTask;
import com.massivekinetics.ow.R;
import com.massivekinetics.ow.application.OWApplication;
import com.massivekinetics.ow.data.manager.ConfigManager;
import com.massivekinetics.ow.data.manager.WeatherDataManager;
import com.massivekinetics.ow.data.model.WeatherForecast;
import com.massivekinetics.ow.data.parser.WeatherParser;
import com.massivekinetics.ow.network.Downloader;
import com.massivekinetics.ow.utils.StringUtils;

public class GetWeatherTask extends AsyncTask<Void, Void, WeatherForecast> {

    WeatherParser parser = new WeatherParser();
    String baseUrl;
    String cityName;
    String gpsParams;
    String key;
    private LoadingListener<WeatherForecast> listener;

    public GetWeatherTask(ConfigManager configManager, LoadingListener<WeatherForecast> listener) {
        this.listener = listener;
        baseUrl = OWApplication.context.getResources().getString(R.string.weather_base_url_gps);
        key = OWApplication.context.getResources().getString(R.string.weather_key);
        cityName = configManager.getStringConfig(ConfigManager.CITY_NAME);
        gpsParams = configManager.getStringConfig(ConfigManager.GPS_PARAMS);

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.notifyStart();
    }

    @Override
    protected WeatherForecast doInBackground(Void... params) {
        if (StringUtils.isNullOrEmpty(gpsParams))
            return WeatherForecast.NULL;

        String query = baseUrl.replace("%GPS", gpsParams).replace("%KEY", key);
        String response = Downloader.doGet(query);
        WeatherForecast forecast = parser.getWeatherForecast(response);

        if (forecast.isSuccessed()){
            forecast.setLocationString(gpsParams);
            WeatherDataManager.getInstance().updateForecast(forecast);
        }
        return forecast;
    }

    @Override
    protected void onPostExecute(WeatherForecast result) {
        super.onPostExecute(result);
        listener.callback(result);
        listener.notifyStop();
    }

}
