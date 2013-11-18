package com.massivekinetics.ow.data.model;

import com.massivekinetics.ow.application.Configuration;
import com.massivekinetics.ow.application.IConfiguration;
import com.massivekinetics.ow.states.WeatherState;
import com.massivekinetics.ow.utils.ResourcesCodeUtils;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WeatherModel implements Serializable {

	private static final transient String TAG = "WeatherModel";
	private static final transient String EMPTY = "";
	private static final transient DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	private String dateString = EMPTY;
	private String tempMinC = EMPTY, tempMaxC = EMPTY;
	private String tempMinF = EMPTY, tempMaxF = EMPTY;
	private String windSpeedKmph = EMPTY, windSpeedMiles = EMPTY;
	private String windDegree = EMPTY, windDirection = EMPTY;
	private String weatherCode = EMPTY;
	private String description = EMPTY;
	private String precipitationMM = EMPTY;
    private String precipitationIN = EMPTY;
    public int lunarState = 0;

    private String humidity = EMPTY;
	private transient Date date;
    private long timeStamp;
    private WeatherState state;

	public transient static final WeatherModel NULL = new WeatherModel();

	public String getDateString() {
		return dateString;
	}

	public void setDateString(String dateString) {
		this.dateString = dateString;
		setDate();
	}

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
        setDate();
    }

	public Date getDate() {
        if(date==null)
            setDate();
		return date;
	}

	private void setDate() {
        if(timeStamp != 0)
            date = new Date(timeStamp);
	}

    private IConfiguration getConfigManager(){
        return new Configuration();
    }

    public String getMaxTemperature(){
        boolean isFahrenheit = getConfigManager().isTemperatureFahrengeitMode();
        return isFahrenheit ? tempMaxF : tempMaxC;
    }

    public String getMinTemperature(){
        boolean isFahrenheit = getConfigManager().isTemperatureFahrengeitMode();
        return isFahrenheit ? tempMinF : tempMinC;
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
        if(windDirection.length()>2)
            windDirection = windDirection.substring(0, 2);
		this.windDirection = windDirection;
	}

	public String getWeatherCode() {
		return weatherCode;
	}

	public void setWeatherCode(String weatherCode) {
		this.weatherCode = weatherCode;
        setState(ResourcesCodeUtils.getWeatherState(Integer.parseInt(weatherCode)));
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPrecipitationMM() {
            return precipitationMM;
        }

    public void setPrecipitationMM(String precipitation) {
        this.precipitationMM = precipitation;
	}

    public String getPrecipitationIN() {
        return precipitationIN;
    }

    public void setPrecipitationIN(String precipitation) {
        this.precipitationIN = precipitation;
    }

    public String getPrecipitation(){
        boolean isFahrenheit = getConfigManager().isTemperatureFahrengeitMode();
        return isFahrenheit ? precipitationIN  + " in" : precipitationMM  + " mm";
    }

    public String getWindSpeed(){
        boolean isFahrenheit = getConfigManager().isTemperatureFahrengeitMode();
        return isFahrenheit ? windSpeedMiles  + " mph" : windSpeedKmph  + " kmph";
    }

	public String getHumidity() {
		return humidity;
	}

	public void setHumidity(String humidity) {
		this.humidity = humidity;
	}

    public int getLunarState() {
        return lunarState;
    }

    public void setLunarState(int lunarState) {
        this.lunarState = lunarState;
    }

    public WeatherState getState() {
        return state;
    }

    public void setState(WeatherState state) {
        this.state = state;
    }
}
