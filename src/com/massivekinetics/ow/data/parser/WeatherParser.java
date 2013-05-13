package com.massivekinetics.ow.data.parser;

import android.util.Log;
import com.massivekinetics.ow.data.model.WeatherForecast;
import com.massivekinetics.ow.data.model.WeatherModel;
import com.massivekinetics.ow.data.parser.strategies.CurrentWeatherStrategy;
import com.massivekinetics.ow.data.parser.strategies.GeneralWeatherStrategy;
import com.massivekinetics.ow.data.parser.strategies.JsonParserStrategy;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.massivekinetics.ow.data.parser.WeatherConstants.CURRENT_CONDITION;
import static com.massivekinetics.ow.data.parser.WeatherConstants.WEATHER;
import static com.massivekinetics.ow.utils.StringUtils.isNullOrEmpty;

public class WeatherParser {

	private static final String TAG = "WeatherParser";
	private JsonParserStrategy parserStrategy = null;


	public WeatherForecast getWeatherForecast(String json) {
		if (!json.contains("error")){
			try {
                WeatherForecast forecast = new WeatherForecast();
				JSONObject generalData = new JSONObject(json);
				List<WeatherModel> generalForecast = getGeneralForecast(generalData);
				WeatherModel currentWeatherModel = getCurrentWeather(generalData);
				mergeCurrentConditions(generalForecast, currentWeatherModel);
				forecast.setForecastList(generalForecast);
                return forecast;
			} catch (JSONException jse) {
				Log.e(TAG, jse.getMessage());
			}
		}
		return WeatherForecast.NULL;
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
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
		for (int i = 0; i < generalForecastArray.length(); i++) {
			JSONObject weatherForecastObject = generalForecastArray
					.getJSONObject(i);
			WeatherModel weatherModel = getWeatherFromJson(weatherForecastObject);
            weatherModel.setTimeStamp(calendar.getTimeInMillis());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
			generalForecast.add(weatherModel);
		}

		return generalForecast;
	}

	private WeatherModel getCurrentWeather(JSONObject generalData)
			throws JSONException {
		setParserStrategy(new CurrentWeatherStrategy());
		JSONObject currentConditionObject = generalData.getJSONObject(
                CURRENT_CONDITION);//.getJSONObject(0);
		WeatherModel currentWeather = getWeatherFromJson(currentConditionObject);
		return currentWeather;
	}

	private void mergeCurrentConditions(List<WeatherModel> generalForecast,
			WeatherModel currentWeatherModel) {
		if (!generalForecast.isEmpty()
				&& currentWeatherModel != WeatherModel.NULL) {
			WeatherModel forecastModel = generalForecast.get(0);

			if (!isNullOrEmpty(currentWeatherModel.getPrecipitationMM()))
				forecastModel.setPrecipitationMM(currentWeatherModel.getPrecipitationMM());

            if (!isNullOrEmpty(currentWeatherModel.getPrecipitationIN()))
                forecastModel.setPrecipitationIN(currentWeatherModel.getPrecipitationIN());

			if (!isNullOrEmpty(currentWeatherModel.getHumidity()))
				forecastModel.setHumidity(currentWeatherModel.getHumidity());

			if (!isNullOrEmpty(currentWeatherModel.getTempMaxF()))
				forecastModel.setTempMaxF(currentWeatherModel.getTempMaxF());

            if (!isNullOrEmpty(currentWeatherModel.getTempMaxC()))
                forecastModel.setTempMaxC(currentWeatherModel.getTempMaxC());

            if (!isNullOrEmpty(currentWeatherModel.getTempMinF()))
                forecastModel.setTempMinF(currentWeatherModel.getTempMinF());

            if (!isNullOrEmpty(currentWeatherModel.getTempMinC()))
                forecastModel.setTempMinC(currentWeatherModel.getTempMinC());

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
