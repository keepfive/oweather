package com.massivekinetics.ow.states;

/**
 * Created with IntelliJ IDEA.
 * User: bovy
 * Date: 2/1/13
 * Time: 3:55 PM
 * To change this template use File | Settings | File Templates.
 */
public enum WeatherState {

    SUNNY("Sunny"), MOSTLY_CLOUDLY("Mostly Cloudy"), PARTLY_CLOUDLY("Partly Cloudy"), RAIN("Rain"), RAIN_AND_SUN("Rain And Sun"), SLEET("Sleet"),
    SNOW("Snow"), SNOW_FALL("Snow Fall"), LIGHTNING_STORM("Lightning Storm"), LIGHT_RAIN("Light Rain"), DOWNPOUR("Downpour"), FOG("Fog"), HURRICANE("Hurricane"), NONE("none");

    private final String value;

    WeatherState(String value){
       this.value = value;
    }

    public String getValue(){
        return value;
    }
}
