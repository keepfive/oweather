package com.massivekinetics.ow.data;

import com.massivekinetics.ow.data.parser.ParserStatus;

import java.util.ArrayList;
import java.util.List;

public class WeatherForecast {
	private List<WeatherModel> forecastList = new ArrayList<WeatherModel>();
	private ParserStatus status = ParserStatus.SUCCESS;
	
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

}
