package com.massivekinetics.ow.data;

import static com.massivekinetics.ow.utils.StringUtils.isNullOrEmpty;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Log;

public class WeatherModel {
	private static final String TAG = "WeatherModel";
	private static final String EMPTY = "";
	private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	private String dateString = EMPTY;
	private String tempMinC = EMPTY, tempMaxC = EMPTY;
	private String tempMinF = EMPTY, tempMaxF = EMPTY;
	private String windSpeedKmph = EMPTY, windSpeedMiles = EMPTY;
	private String windDegree = EMPTY, windDirection = EMPTY;
	private String weatherCode = EMPTY;
	private String description = EMPTY;
	private String precipitation = EMPTY;
	private String humidity = EMPTY;
	private Date date = new Date();

	public static final WeatherModel NULL = new WeatherModel();

	public String getDateString() {
		return dateString;
	}

	public void setDateString(String dateString) {
		this.dateString = dateString;
		setDate();
	}

	public Date getDate() {
		return date;
	}

	private void setDate() {
		if (!isNullOrEmpty(dateString))
			try {
				date = dateFormat.parse(dateString);
			} catch (ParseException pe) {
				Log.e(TAG, pe.getMessage());
			}
	}

	public String getTempMinC() {
		return tempMinC;
	}

	public void setTempMinC(String tempMinC) {
		this.tempMinC = tempMinC;
	}

	public String getTempMaxC() {
		return tempMaxC;
	}

	public void setTempMaxC(String tempMaxC) {
		this.tempMaxC = tempMaxC;
	}

	public String getTempMinF() {
		return tempMinF;
	}

	public void setTempMinF(String tempMinF) {
		this.tempMinF = tempMinF;
	}

	public String getTempMaxF() {
		return tempMaxF;
	}

	public void setTempMaxF(String tempMaxF) {
		this.tempMaxF = tempMaxF;
	}

	public String getWindSpeedKmph() {
		return windSpeedKmph;
	}

	public void setWindSpeedKmph(String windSpeedKmph) {
		this.windSpeedKmph = windSpeedKmph;
	}

	public String getWindSpeedMiles() {
		return windSpeedMiles;
	}

	public void setWindSpeedMiles(String windSpeedMiles) {
		this.windSpeedMiles = windSpeedMiles;
	}

	public String getWindDegree() {
		return windDegree;
	}

	public void setWindDegree(String windDegree) {
		this.windDegree = windDegree;
	}

	public String getWindDirection() {
		return windDirection;
	}

	public void setWindDirection(String windDirection) {
		this.windDirection = windDirection;
	}

	public String getWeatherCode() {
		return weatherCode;
	}

	public void setWeatherCode(String weatherCode) {
		this.weatherCode = weatherCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPrecipitation() {
		return precipitation;
	}

	public void setPrecipitation(String precipitation) {
		this.precipitation = precipitation;
	}

	public String getHumidity() {
		return humidity;
	}

	public void setHumidity(String humidity) {
		this.humidity = humidity;
	}
}
