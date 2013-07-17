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
    String gpsParams;
    private String session;

    private LoadingListener<WeatherForecast> listener;
    private Resources resources;

    public GetWeatherTask(ConfigManager configManager, LoadingListener<WeatherForecast> listener) {
        this.listener = listener;
        this.resources = OWApplication.getInstance().getResources();
        gpsParams = configManager.getLocationCoordinates();
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

        String response = getWeatherResponse();

        if(response.equals(NetworkUtils.CODE_403)){
            new GetSessionTask().doTask();
            response = getWeatherResponse();
        }

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

    private String getWeatherResponse(){
        String requestBody = com.massivekinetics.ow.network.SecurityManager.getWeatherRequestEncryptedBody();
        String serverUrl = resources.getString(R.string.ow_url_get_weather);
        return NetworkUtils.doPost(serverUrl, requestBody);
    }

}
