package com.massivekinetics.ow.utils;

import com.massivekinetics.ow.states.WeatherState;

/**
 * Created with IntelliJ IDEA.
 * User: bovy
 * Date: 2/1/13
 * Time: 3:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class WeatherCodeUtils {

    public static WeatherState getWeatherResource(int weatherCode) {

        switch (weatherCode) {
            case 113:
                return WeatherState.SUNNY;

            case 116:
                return WeatherState.PARTLY_CLOUDLY;

            case 119:
                return WeatherState.MOSTLY_CLOUDLY;

            case 122:
                return WeatherState.MOSTLY_CLOUDLY;

            case 143:
                return WeatherState.MOSTLY_CLOUDLY;

            case 176:
                return WeatherState.RAIN_AND_SUN;

            case 179:
                return WeatherState.SNOW;

            case 182:
                return WeatherState.SLEET;

            case 185:
                return WeatherState.SLEET;

            case 200:
                return WeatherState.RAIN_AND_SUN;

            case 227:
                return WeatherState.SNOW;

            case 230:
                return WeatherState.SNOW_FALL;

            case 248:
                return WeatherState.MOSTLY_CLOUDLY;

            case 260:
                return WeatherState.MOSTLY_CLOUDLY;

            case 263:
                return WeatherState.RAIN_AND_SUN;

            case 266:
                return WeatherState.RAIN_AND_SUN;

            case 281:
                return WeatherState.DOWNPOUR;

            case 284:
                return WeatherState.DOWNPOUR;

            case 293:
                return WeatherState.RAIN;

            case 296:
                return WeatherState.RAIN;

            case 299:
                return WeatherState.RAIN;

            case 302:
                return WeatherState.RAIN;

            case 305:
                return WeatherState.LIGHT_RAIN;

            case 308:
                return WeatherState.RAIN;

            case 311:
                return WeatherState.SLEET;

            case 314:
                return WeatherState.DOWNPOUR;

            case 317:
                return WeatherState.SLEET;

            case 320:
                return WeatherState.SLEET;

            case 323:
                return WeatherState.SNOW;

            case 326:
                return WeatherState.SNOW;

            case 329:
                return WeatherState.SNOW_FALL;

            case 332:
                return WeatherState.SNOW_FALL;

            case 338:
                return WeatherState.SNOW_FALL;

            case 350:
                return WeatherState.SLEET;

            case 353:
                return WeatherState.DOWNPOUR;

            case 359:
                return WeatherState.DOWNPOUR;

            case 362:
                return WeatherState.DOWNPOUR;

            case 365:
                return WeatherState.SLEET;

            case 368:
                return WeatherState.SLEET;

            case 371:
                return WeatherState.SLEET;

            case 374:
                return WeatherState.SLEET;

            case 377:
                return WeatherState.SLEET;

            case 386:
                return WeatherState.RAIN;

            case 389:
                return WeatherState.DOWNPOUR;

            case 392:
                return WeatherState.SNOW;

            case 395:
                return WeatherState.SNOW_FALL;

            default:
                return WeatherState.NONE;

        }
    }
}
