package com.massivekinetics.ow.domain.parser.strategies;

import org.json.JSONException;
import org.json.JSONObject;

import com.massivekinetics.ow.domain.model.WeatherModel;

public interface JsonParserStrategy {
	WeatherModel parse(JSONObject jsonObject) throws JSONException;
}
