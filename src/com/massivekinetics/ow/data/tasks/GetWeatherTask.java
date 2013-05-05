package com.massivekinetics.ow.data.tasks;

import android.content.res.Resources;
import android.os.AsyncTask;
import com.massivekinetics.ow.R;
import com.massivekinetics.ow.application.OWApplication;
import com.massivekinetics.ow.data.manager.ConfigManager;
import com.massivekinetics.ow.data.manager.WeatherDataManager;
import com.massivekinetics.ow.data.model.WeatherForecast;
import com.massivekinetics.ow.data.parser.WeatherParser;
import com.massivekinetics.ow.network.NetworkUtils;
import com.massivekinetics.ow.utils.StringUtils;

public class GetWeatherTask extends AsyncTask<Void, Void, WeatherForecast> {

    WeatherParser parser = new WeatherParser();
    String baseUrl;
    String cityName;
    String gpsParams;
    String key;
    private String session;

    private LoadingListener<WeatherForecast> listener;
    private Resources resources;

    public GetWeatherTask(ConfigManager configManager, LoadingListener<WeatherForecast> listener) {
        this.listener = listener;
        this.resources = OWApplication.getInstance().getResources();
        baseUrl = resources.getString(R.string.weather_base_url_gps);
        key = resources.getString(R.string.weather_key);
        cityName = configManager.getStringConfig(ConfigManager.CITY_NAME);
        gpsParams = configManager.getLocation();
        session = configManager.getActiveSession();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.notifyStart();
    }

    @Override
    protected WeatherForecast doInBackground(Void... params) {
        if (!NetworkUtils.isOnline() || StringUtils.isNullOrEmpty(gpsParams) || StringUtils.isNullOrEmpty(session))
            return WeatherForecast.NULL;
        String requestBody = com.massivekinetics.ow.network.SecurityManager.getWeatherRequestEncryptedBody();
        String serverUrl = resources.getString(R.string.test_server_url_get_weather);
        final String response = NetworkUtils.doPost(serverUrl, requestBody);

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
