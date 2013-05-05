package com.massivekinetics.ow.utils;

import com.massivekinetics.ow.R;
import com.massivekinetics.ow.states.WeatherState;

/**
 * Created with IntelliJ IDEA.
 * User: bovy
 * Date: 2/1/13
 * Time: 3:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class ResourcesCodeUtils {

    public static int getWeatherImageResource(WeatherState weatherState) {
        switch (weatherState) {
            case SUNNY:
                return R.drawable.weather_state_0;

            case DOWNPOUR: {
                return R.drawable.weather_state_1;
            }

            case LIGHT_RAIN: {
                return R.drawable.weather_state_6;
            }

            case MOSTLY_CLOUDLY: {
                return R.drawable.weather_state_7;
            }

            case PARTLY_CLOUDLY: {
                return R.drawable.weather_state_8;
            }

            case RAIN: {
                return R.drawable.weather_state_5;
            }

            case RAIN_AND_SUN: {
                return R.drawable.weather_state_9;
            }

            case SLEET: {
                return R.drawable.weather_state_2;
            }

            case SNOW: {
                return R.drawable.weather_state_4;
            }

            case SNOW_FALL: {
                return R.drawable.weather_state_3;
            }

            case FOG:
                return R.drawable.weather_state_10;

            case LIGHTING_STORM:
                return R.drawable.weather_state_12;

            case HURRICANE:
                return R.drawable.weather_state_11;

            default:
                return R.drawable.weather_state_0;
        }
    }

    public static int getWeatherBackgroundColor(WeatherState weatherState) {
        switch (weatherState) {
            case SUNNY:
                return R.color.ow_sunny;

            case DOWNPOUR: {
                return R.color.ow_downpour;
            }

            case LIGHT_RAIN: {
                return R.color.ow_light_rain;
            }

            case MOSTLY_CLOUDLY: {
                return R.color.ow_mostly_cloudy;
            }

            case PARTLY_CLOUDLY: {
                return R.color.ow_partly_cloudy;
            }

            case RAIN: {
                return R.color.ow_rain;
            }

            case RAIN_AND_SUN: {
                return R.color.ow_rain_and_sun;
            }

            case SLEET: {
                return R.color.ow_sleet;
            }

            case SNOW: {
                return R.color.ow_snow;
            }

            case SNOW_FALL: {
                return R.color.ow_snowfall;
            }

            case LIGHTING_STORM:
                return R.color.ow_lighting_storm;

            case FOG:
                return R.color.ow_fog;

            case HURRICANE:
                return R.color.ow_hurricane;

            default:
                return R.color.ow_sunny;
        }
    }

    public static WeatherState getWeatherState(int weatherCode) {

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
                return WeatherState.LIGHTING_STORM;

            case 227:
                return WeatherState.SNOW;

            case 230:
                return WeatherState.SNOW_FALL;

            case 248:
                return WeatherState.FOG;

            case 260:
                return WeatherState.FOG;

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
                return WeatherState.LIGHTING_STORM;

            case 389:
                return WeatherState.LIGHTING_STORM;

            case 392:
                return WeatherState.SNOW;

            case 395:
                return WeatherState.SNOW_FALL;

            default:
                return WeatherState.NONE;

        }
    }

    public static int getLunarStateImageResource(int lunarState) {
        switch (lunarState) {
            case 0:
                return R.drawable.mn0;

            case 1:
                return R.drawable.mn1;

            case 2:
                return R.drawable.mn2;

            case 3:
                return R.drawable.mn3;

            case 4:
                return R.drawable.mn4;

            case 5:
                return R.drawable.mn5;

            case 6:
                return R.drawable.mn6;

            case 7:
                return R.drawable.mn7;

            case 8:
                return R.drawable.mn8;

            case 9:
                return R.drawable.mn9;

            case 10:
                return R.drawable.mn10;

            case 11:
                return R.drawable.mn11;

            case 12:
                return R.drawable.mn12;

            case 13:
                return R.drawable.mn13;

            case 14:
                return R.drawable.mn14;

            case 15:
                return R.drawable.mn15;

            case 16:
                return R.drawable.mn16;

            case 17:
                return R.drawable.mn17;

            case 18:
                return R.drawable.mn18;

            case 19:
                return R.drawable.mn19;

            case 20:
                return R.drawable.mn20;

            case 21:
                return R.drawable.mn21;

            case 22:
                return R.drawable.mn22;

            case 23:
                return R.drawable.mn23;

            case 24:
                return R.drawable.mn24;

            case 25:
                return R.drawable.mn25;

            case 26:
                return R.drawable.mn26;

            case 27:
                return R.drawable.mn27;

            case 28:
                return R.drawable.mn28;

            case 29:
                return R.drawable.mn29;

            case 30:
                return R.drawable.mn30;

            default:
                return R.drawable.mn0;
        }
    }
}
