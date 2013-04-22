package com.massivekinetics.ow.data.model;

import com.massivekinetics.ow.data.parser.ParserStatus;
import com.massivekinetics.ow.utils.DateUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WeatherForecast implements Serializable {
    public static final transient WeatherForecast NULL = new WeatherForecast();
    private long timeStamp;

	private List<WeatherModel> forecastList = new ArrayList<WeatherModel>();
	private ParserStatus status = ParserStatus.SUCCESS;
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
	
	public void setStatus(ParserStatus status){
		this.status = status;
	}
	
	public ParserStatus getStatus() {
		return status;
	}

    public boolean isSuccessed(){
        return status == ParserStatus.SUCCESS;
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
        return (today - timeStamp) < 5 * 24 * 60 * 60 * 1000;
    }

}
