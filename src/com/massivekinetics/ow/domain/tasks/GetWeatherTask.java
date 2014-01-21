package com.massivekinetics.ow.domain.tasks;

import android.content.res.Resources;
import android.os.AsyncTask;
import com.massivekinetics.ow.R;
import com.massivekinetics.ow.application.*;
import com.massivekinetics.ow.domain.model.WeatherForecast;
import com.massivekinetics.ow.domain.parser.WeatherParser;
import com.massivekinetics.ow.services.network.NetworkService;
import com.massivekinetics.ow.services.network.RESTService;
import com.massivekinetics.ow.services.utils.StringUtils;
import com.massivekinetics.ow.ui.interfaces.LoadingListener;

public class GetWeatherTask extends AsyncTask<Void, Void, WeatherForecast> {

    WeatherParser parser = new WeatherParser();
    String gpsParams;
    private String session;

    private LoadingListener<WeatherForecast> listener;
    private Resources resources;

    public GetWeatherTask(IConfiguration IConfiguration, LoadingListener<WeatherForecast> listener) {
        this.listener = listener;
        this.resources = Application.getInstance().getResources();
        gpsParams = IConfiguration.getLocationCoordinates();
        session = IConfiguration.getActiveSession();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(listener != null)
            listener.notifyStart();
    }

    @Override
    protected WeatherForecast doInBackground(Void... params) {
        if (!NetworkService.isOnline() || StringUtils.isNullOrEmpty(gpsParams) || StringUtils.isNullOrEmpty(session))
            return WeatherForecast.NULL;

        String response = getWeatherResponse();

        if(response.equals(NetworkService.CODE_403)){
            new GetSessionTask().doTask();
            response = getWeatherResponse();
        }

        WeatherForecast forecast = parser.getWeatherForecast(response);

        if (forecast.isSuccessed()){
            forecast.setLocationString(gpsParams);
            IDataManager dataManager = AppLocator.resolve(IDataManager.class);
            dataManager.updateForecast(forecast);
        }

        return forecast;
    }

    @Override
    protected void onPostExecute(WeatherForecast result) {
        super.onPostExecute(result);
        if(listener != null){
            listener.onLoaded(result);
            listener.notifyStop();
        }
    }

    private String getWeatherResponse(){
        String requestBody = RESTService.getWeatherRequestEncryptedBody();
        String serverUrl = resources.getString(R.string.ow_url_get_weather);
        return NetworkService.doPost(serverUrl, requestBody);
    }

}
