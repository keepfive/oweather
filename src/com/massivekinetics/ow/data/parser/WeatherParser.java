package com.massivekinetics.ow.data.parser;

import static com.massivekinetics.ow.data.parser.WeatherConstants.CURRENT_CONDITION;
import static com.massivekinetics.ow.data.parser.WeatherConstants.DATA;
import static com.massivekinetics.ow.data.parser.WeatherConstants.WEATHER;
import static com.massivekinetics.ow.utils.StringUtils.isNullOrEmpty;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.massivekinetics.ow.data.model.WeatherForecast;
import com.massivekinetics.ow.data.model.WeatherModel;
import com.massivekinetics.ow.data.parser.strategies.CurrentWeatherStrategy;
import com.massivekinetics.ow.data.parser.strategies.GeneralWeatherStrategy;
import com.massivekinetics.ow.data.parser.strategies.JsonParserStrategy;

public class WeatherParser {

	private static final String TAG = "WeatherParser";
	private JsonParserStrategy parserStrategy = null;


	public WeatherForecast getWeatherForecast(String json) {
		WeatherForecast forecast = new WeatherForecast();
		
		if (json.contains("error"))
			forecast.setStatus(ParserStatus.ERROR);
		else {
			try {
				JSONObject generalData = new JSONObject(json).getJSONObject(DATA);
				List<WeatherModel> generalForecast = getGeneralForecast(generalData);
				WeatherModel currentWeatherModel = getCurrentWeather(generalData);
				mergeCurrentConditions(generalForecast, currentWeatherModel);
				
				forecast.setForecastList(generalForecast);
				forecast.setStatus(ParserStatus.SUCCESS);
			} catch (JSONException jse) {
				forecast.setStatus(ParserStatus.ERROR);
				Log.e(TAG, jse.getMessage());
			}
		}
		return forecast;
	}

	public WeatherModel getWeatherFromJson(JSONObject jsonObject) {
		WeatherModel model = WeatherModel.NULL;
		try {
			model = parserStrategy.parse(jsonObject);
		} catch (NullPointerException npe) {
			Log.e(TAG, npe.getMessage());
		} catch (JSONException jse) {
			Log.e(TAG, jse.getMessage());
		}
		return model;
	}

	private List<WeatherModel> getGeneralForecast(JSONObject generalData)
			throws JSONException {
		setParserStrategy(new GeneralWeatherStrategy());

		List<WeatherModel> generalForecast = new ArrayList<WeatherModel>();

		JSONArray generalForecastArray = generalData.getJSONArray(WEATHER);
		for (int i = 0; i < generalForecastArray.length(); i++) {
			JSONObject weatherForecastObject = generalForecastArray
					.getJSONObject(i);
			WeatherModel weatherModel = getWeatherFromJson(weatherForecastObject);
			generalForecast.add(weatherModel);
		}

		return generalForecast;
	}

	private WeatherModel getCurrentWeather(JSONObject generalData)
			throws JSONException {
		setParserStrategy(new CurrentWeatherStrategy());
		JSONObject currentConditionObject = generalData.getJSONArray(
				CURRENT_CONDITION).getJSONObject(0);
		WeatherModel currentWeather = getWeatherFromJson(currentConditionObject);
		return currentWeather;
	}

	private void mergeCurrentConditions(List<WeatherModel> generalForecast,
			WeatherModel currentWeatherModel) {
		if (!generalForecast.isEmpty()
				&& currentWeatherModel != WeatherModel.NULL) {
			WeatherModel forecastModel = generalForecast.get(0);

			if (!isNullOrEmpty(currentWeatherModel.getPrecipitation()))
				forecastModel.setPrecipitation(currentWeatherModel
						.getPrecipitation());

			if (!isNullOrEmpty(currentWeatherModel.getHumidity()))
				forecastModel.setHumidity(currentWeatherModel.getHumidity());

			if (!isNullOrEmpty(currentWeatherModel.getTempMaxF()))
				forecastModel.setTempMaxF(currentWeatherModel.getTempMaxF());

			if (!isNullOrEmpty(currentWeatherModel.getWeatherCode()))
				forecastModel.setWeatherCode(currentWeatherModel
						.getWeatherCode());

			if (!isNullOrEmpty(currentWeatherModel.getDescription()))
				forecastModel.setDescription(currentWeatherModel
						.getDescription());

			if (!isNullOrEmpty(currentWeatherModel.getWindSpeedMiles()))
				forecastModel.setWindSpeedMiles(currentWeatherModel
						.getWindSpeedMiles());

			if (!isNullOrEmpty(currentWeatherModel.getWindSpeedKmph()))
				forecastModel.setWindSpeedKmph(currentWeatherModel
						.getWindSpeedKmph());

			if (!isNullOrEmpty(currentWeatherModel.getWindDegree()))
				forecastModel
						.setWindDegree(currentWeatherModel.getWindDegree());

			if (!isNullOrEmpty(currentWeatherModel.getWindDirection()))
				forecastModel.setWindDirection(currentWeatherModel
						.getWindDirection());
		}
	}

	private void setParserStrategy(JsonParserStrategy parserStrategy) {
		this.parserStrategy = parserStrategy;
	}

}
