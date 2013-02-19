package com.massivekinetics.ow.data.model;

import com.massivekinetics.ow.data.parser.ParserStatus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WeatherForecast implements Serializable {
    public static final transient WeatherForecast NULL = new WeatherForecast();
    private long timeStamp;

	private List<WeatherModel> forecastList = new ArrayList<WeatherModel>();
	private ParserStatus status = ParserStatus.SUCCESS;


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

}
