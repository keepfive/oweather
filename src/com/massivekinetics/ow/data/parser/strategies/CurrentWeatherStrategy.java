package com.massivekinetics.ow.data.parser.strategies;

import org.json.JSONException;
import org.json.JSONObject;

import com.massivekinetics.ow.data.model.WeatherModel;

import static com.massivekinetics.ow.data.parser.WeatherConstants.*;

public class CurrentWeatherStrategy implements JsonParserStrategy {

	@Override
	public WeatherModel parse(JSONObject jsonObject) throws JSONException {
		WeatherModel model = new WeatherModel();
		model.setTempMaxC(jsonObject.getString(TEMP_C));
		model.setTempMaxF(jsonObject.getString(TEMP_F));
		model.setWeatherCode(jsonObject.getString(WEATHER_CODE));
		model.setWindSpeedMiles(jsonObject.getString(WIND_SPEED_MILES));
		model.setWindSpeedKmph(jsonObject.getString(WIND_SPEED_KMPH));
		model.setWindDegree(jsonObject.getString(WIND_DIRECTION_DEGREE));
		model.setWindDirection(jsonObject.getString(WIND_DIRECTION));
		model.setPrecipitationMM(jsonObject.getString(PRECIPITATION_MM));
        model.setPrecipitationIN(jsonObject.getString(PRECIPITATION_IN));
		model.setHumidity(jsonObject.getString(HUMIDITY));
		return model;
	}

}
