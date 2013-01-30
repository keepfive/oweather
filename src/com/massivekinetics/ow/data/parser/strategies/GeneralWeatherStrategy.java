package com.massivekinetics.ow.data.parser.strategies;

import org.json.JSONException;

import org.json.JSONObject;

import com.massivekinetics.ow.data.WeatherModel;
import static com.massivekinetics.ow.data.parser.WeatherConstants.*;

public class GeneralWeatherStrategy implements JsonParserStrategy {

	@Override
	public WeatherModel parse(JSONObject jsonObject) throws JSONException {
		WeatherModel model = new WeatherModel();
		model.setDateString(jsonObject.getString(DATE));
		model.setTempMaxC(jsonObject.getString(TEMP_MAX_C));
		model.setTempMaxF(jsonObject.getString(TEMP_MAX_F));
		model.setTempMinC(jsonObject.getString(TEMP_MIN_C));
		model.setTempMinF(jsonObject.getString(TEMP_MIN_F));
		model.setPrecipitation(jsonObject.getString(PRECIPITATION));
		model.setWindSpeedMiles(jsonObject.getString(WIND_SPEED_MILES));
		model.setWindSpeedKmph(jsonObject.getString(WIND_SPEED_KMPH));
		model.setWindDegree(jsonObject.getString(WIND_DIRECTION_DEGREE));
		model.setWindDirection(jsonObject.getString(WIND_DIRECTION));
		model.setWeatherCode(jsonObject.getString(WEATHER_CODE));
		model.setDescription(jsonObject.getJSONArray(WEATHER_DESC).getJSONObject(0).getString(VALUE));
		return model;
	}

}
