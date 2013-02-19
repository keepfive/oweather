package com.massivekinetics.ow.data.parser.strategies;

import org.json.JSONException;
import org.json.JSONObject;

import com.massivekinetics.ow.data.model.WeatherModel;

public interface JsonParserStrategy {
	WeatherModel parse(JSONObject jsonObject) throws JSONException;
}
