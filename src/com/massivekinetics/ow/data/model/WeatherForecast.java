package com.massivekinetics.ow.data.model;

import com.massivekinetics.ow.utils.DateUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WeatherForecast implements Serializable {
    public static final transient WeatherForecast NULL = new WeatherForecast();
    private long timeStamp;

	private List<WeatherModel> forecastList = new ArrayList<WeatherModel>();
    private String locationString;

    public WeatherForecast(){
        timeStamp = new Date().getTime();
    }
	
	public List<WeatherModel> getForecastList() {
		return forecastList;
	}

	public void setForecastList(List<WeatherModel> forecastList) {
		if(forecastList!=null && !forecastList.isEmpty())
			this.forecastList = new ArrayList<WeatherModel>(forecastList);
	}

    public boolean isSuccessed(){
        boolean succeeded = this != NULL && !forecastList.isEmpty();
        return succeeded;
    }

    public long getTimeStamp(){
        return timeStamp;
    }

    public String getLocationString(){
        return locationString;
    }

    public void setLocationString(String locationString){
        this.locationString = locationString;
    }

    public boolean isActual(){
        long today = DateUtils.getCurrentInMillis();
        return !forecastList.isEmpty() && (today - timeStamp) < 5 * 24 * 60 * 60 * 1000;
    }

}
