package com.massivekinetics.ow.domain.parser.strategies;

import org.json.JSONException;

import org.json.JSONObject;

import com.massivekinetics.ow.domain.model.WeatherModel;
import static com.massivekinetics.ow.domain.parser.WeatherConstants.*;

public class GeneralWeatherStrategy implements JsonParserStrategy {

	@Override
	public WeatherModel parse(JSONObject jsonObject) throws JSONException {
		WeatherModel model = new WeatherModel();
		model.setDateString(jsonObject.getString(DATE));
		model.setTempMaxC(jsonObject.getString(TEMP_MAX_C));
		model.setTempMaxF(jsonObject.getString(TEMP_MAX_F));
		model.setTempMinC(jsonObject.getString(TEMP_MIN_C));
		model.setTempMinF(jsonObject.getString(TEMP_MIN_F));
		model.setPrecipitationMM(jsonObject.getString(PRECIPITATION_MM));
        model.setPrecipitationIN(jsonObject.getString(PRECIPITATION_IN));

        model.setWindSpeedMiles(jsonObject.getString(WIND_SPEED_MILES));
		model.setWindSpeedKmph(jsonObject.getString(WIND_SPEED_KMPH));
		model.setWindDegree(jsonObject.getString(WIND_DIRECTION_DEGREE));
		model.parseAndSetWindDirection(jsonObject.getString(WIND_DIRECTION));
		model.setWeatherCode(jsonObject.getString(WEATHER_CODE));
        model.setLunarState(jsonObject.getInt(LUNAR_STATE));
		//model.setDescription(jsonObject.getJSONArray(WEATHER_DESC).getJSONObject(0).getString(VALUE));
		return model;
	}

}
