package com.massivekinetics.ow.data.tasks;

import android.os.AsyncTask;

import com.massivekinetics.ow.R;
import com.massivekinetics.ow.data.WeatherForecast;
import com.massivekinetics.ow.application.OWApplication;
import com.massivekinetics.ow.data.parser.WeatherParser;
import com.massivekinetics.ow.network.Downloader;

public class GetWeatherTask extends AsyncTask<String, Void, WeatherForecast> {
	
	WeatherParser parser = new WeatherParser();

	private LoadingListener<WeatherForecast> listener;
	String baseUrl;
	String key;

	public GetWeatherTask(LoadingListener<WeatherForecast> listener) {
		this.listener = listener;
		baseUrl = OWApplication.context.getResources().getString(R.string.weather_base_url_city);
		key = OWApplication.context.getResources().getString(R.string.weather_key);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
        listener.notifyStart();
	}

	@Override
	protected WeatherForecast doInBackground(String... params) {
		String cityName = params[0];
		String numOfDays = params[1];
		String query = baseUrl.replace("%CITY", cityName).replace("%DAYS", numOfDays).replace("%KEY", key);
		String response = Downloader.doGet(query);
		WeatherForecast forecast = parser.getWeatherForecast(response);
		return forecast;
	}

	@Override
	protected void onPostExecute(WeatherForecast result) {
		super.onPostExecute(result);
		listener.callback(result);
	}

}
