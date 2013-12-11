package com.massivekinetics.ow.domain.states;

import com.massivekinetics.ow.R;
import com.massivekinetics.ow.application.Application;

/**
 * Created with IntelliJ IDEA.
 * User: bovy
 * Date: 2/1/13
 * Time: 3:55 PM
 * To change this template use File | Settings | File Templates.
 */
public enum WeatherState {

    SUNNY(R.string.sunny), MOSTLY_CLOUDLY(R.string.mostly_cloudy), PARTLY_CLOUDLY(R.string.partly_cloudy),
    RAIN(R.string.rain), RAIN_AND_SUN(R.string.rain_and_sun), SLEET(R.string.sleet),
    SNOW(R.string.snow), SNOW_FALL(R.string.snow_fall), LIGHTNING_STORM(R.string.lightning_storm),
    LIGHT_RAIN(R.string.light_rain), DOWNPOUR(R.string.downpour), FOG(R.string.fog),
    HURRICANE(R.string.hurricane), NONE(R.string.none);

    private final String mDisplayName;

    WeatherState(int resId) {
        String displayName = Application.getInstance().getString(resId);
        mDisplayName = displayName;
    }

    public String getDisplayName() {
        return mDisplayName;
    }
}
