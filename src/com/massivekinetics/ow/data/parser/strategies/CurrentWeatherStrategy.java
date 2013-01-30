package com.massivekinetics.ow.data.parser.strategies;

import static com.massivekinetics.ow.data.parser.WeatherConstants.HUMIDITY;
import static com.massivekinetics.ow.data.parser.WeatherConstants.PRECIPITATION;
import static com.massivekinetics.ow.data.parser.WeatherConstants.TEMP_C;
import static com.massivekinetics.ow.data.parser.WeatherConstants.TEMP_F;
import static com.massivekinetics.ow.data.parser.WeatherConstants.VALUE;
import static com.massivekinetics.ow.data.parser.WeatherConstants.WEATHER_CODE;
import static com.massivekinetics.ow.data.parser.WeatherConstants.WEATHER_DESC;
import static com.massivekinetics.ow.data.parser.WeatherConstants.WIND_DIRECTION;
import static com.massivekinetics.ow.data.parser.WeatherConstants.WIND_DIRECTION_DEGREE;
import static com.massivekinetics.ow.data.parser.WeatherConstants.WIND_SPEED_KMPH;
import static com.massivekinetics.ow.data.parser.WeatherConstants.WIND_SPEED_MILES;

import org.json.JSONException;
import org.json.JSONObject;

import com.massivekinetics.ow.data.WeatherModel;

public class CurrentWeatherStrategy implements JsonParserStrategy {

	@Override
	public WeatherModel parse(JSONObject jsonObject) throws JSONException {
		WeatherModel model = new WeatherModel();
		model.setTempMaxC(jsonObject.getString(TEMP_C));
		model.setTempMaxF(jsonObject.getString(TEMP_F));
		model.setWeatherCode(jsonObject.getString(WEATHER_CODE));
		model.setDescription(jsonObject.getJSONArray(WEATHER_DESC).getJSONObject(0).getString(VALUE));
		model.setWindSpeedMiles(jsonObject.getString(WIND_SPEED_MILES));
		model.setWindSpeedKmph(jsonObject.getString(WIND_SPEED_KMPH));
		model.setWindDegree(jsonObject.getString(WIND_DIRECTION_DEGREE));
		model.setWindDirection(jsonObject.getString(WIND_DIRECTION));
		model.setPrecipitation(jsonObject.getString(PRECIPITATION));
		model.setHumidity(jsonObject.getString(HUMIDITY));
		return model;
	}

}
