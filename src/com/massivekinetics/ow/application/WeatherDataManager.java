package com.massivekinetics.ow.application;

import android.content.Intent;
import android.util.Log;
import com.google.gson.Gson;
import com.massivekinetics.ow.R;
import com.massivekinetics.ow.domain.model.WeatherForecast;
import com.massivekinetics.ow.domain.model.WeatherModel;
import com.massivekinetics.ow.domain.tasks.GetWeatherTask;
import com.massivekinetics.ow.ui.interfaces.LoadingListener;
import com.massivekinetics.ow.services.network.NetworkService;
import com.massivekinetics.ow.services.utils.DateUtils;
import com.massivekinetics.ow.ui.widgets.ClockUpdateService;

import java.io.*;
import java.nio.ByteBuffer;

/**
 * Created with IntelliJ IDEA.
 * User: bovy
 * Date: 2/3/13
 * Time: 12:42 AM
 * To change this template use File | Settings | File Templates.
 */
public class WeatherDataManager implements IDataManager {
    private static final String TAG = "WeatherDataManager";
    private static WeatherDataManager instance = null;
    private WeatherForecast mWeatherForecast = WeatherForecast.NULL;
    private IConfiguration mConfiguration;

    private static final long EXPIRATION_ONLINE = 3 * 60 * 60 * 1000;  // 3 hours
    private static final long EXPIRATION_OFFLINE = 5 * (24 - 1) * 60 * 60 * 1000;  // 5 days minus 5 hours --> (24 - 1) just for convenience.

    protected WeatherDataManager() {
        this(Configuration.INSTANCE());
    }

    protected WeatherDataManager(IConfiguration configuration) {
        mConfiguration = configuration;
        restoreForecast();
    }

    @Override
    public void runUpdate(LoadingListener<WeatherForecast> listener) {
        new GetWeatherTask(Configuration.INSTANCE(), listener).execute();
    }

    @Override
    public void getWeatherForecast(LoadingListener<WeatherForecast> listener) {
        if (hasActualForecast())
            listener.onLoaded(mWeatherForecast);
        else
            runUpdate(listener);
    }

    @Override
    public void updateForecast(WeatherForecast forecast) {
        this.mWeatherForecast = forecast;
        saveForecast();
        Application.getInstance().startService(new Intent(ClockUpdateService.ACTION_UPDATE));
    }

    /*@Override
    public void saveForecast() {
        try {
            File cache = new File(Application.getInstance().getCacheDir(), "oweather.dat");
            cache.createNewFile();
            FileOutputStream fos = new FileOutputStream(cache);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(mWeatherForecast);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void restoreForecast() {
        try {
            File cache = new File(Application.getInstance().getCacheDir(), "oweather.dat");
            if (cache.exists()) {
                FileInputStream input = new FileInputStream(cache);
                ObjectInputStream ois = new ObjectInputStream(input);
                WeatherForecast forecast = (WeatherForecast) ois.readObject();
                this.mWeatherForecast = forecast;
            }

        } catch (IOException e) {

        } catch (ClassNotFoundException e) {

        } catch (Throwable t){
            //I'm a Scatman
        }
    }  */

    @Override
    public void saveForecast() {
        try {
            Gson gson = new Gson();
            String jsonString = gson.toJson(mWeatherForecast);
            File cache = new File(Application.getInstance().getCacheDir(), "oweather.json");
            cache.createNewFile();
            FileWriter fileWriter = new FileWriter(cache);
            fileWriter.write(jsonString);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        } finally {
        }
    }

    @Override
    public void restoreForecast() {
        try {

            Gson gson = new Gson();
            File cache = new File(Application.getInstance().getCacheDir(), "oweather.json");
            if (cache.exists()) {
                FileInputStream fis = new FileInputStream(cache);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader bufferedReader = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                bufferedReader.close();
                String json = sb.toString();
                WeatherForecast restored = gson.fromJson(json, WeatherForecast.class);
                this.mWeatherForecast = restored;
            }

        } catch (IOException e) {

        } catch (Throwable t) {
            //I'm a Scatman
        }
    }

    public boolean hasActualForecast() {
        if (mWeatherForecast == null || mWeatherForecast == WeatherForecast.NULL)
            restoreForecast();

        String gpsLocation = mConfiguration.getLocationCoordinates();
        long weatherLifeInMsec = DateUtils.getCurrentInMillis() - mWeatherForecast.getTimeStamp();
        long expirationLimit = NetworkService.isOnline() ? EXPIRATION_ONLINE : EXPIRATION_OFFLINE;

        boolean isActual = !mWeatherForecast.getForecastList().isEmpty()
                && gpsLocation.equals(mWeatherForecast.getLocationString())
                && weatherLifeInMsec < expirationLimit;

        return isActual;
    }

    @Override
    public WeatherModel getCurrentWeather() {
        if (hasActualForecast()) {
            DateUtils dateUtils = new DateUtils();
            for (WeatherModel model : mWeatherForecast.getForecastList()) {
                if (dateUtils.isToday(model.getDate())) {
                    return model;
                }
            }
        } else
            runUpdate(null);
        return WeatherModel.NULL;
    }

    @Override
    public String getNotification() {
        String notification = null;

        if (hasActualForecast()) {
            DateUtils dateUtils = new DateUtils();
            for (WeatherModel model : mWeatherForecast.getForecastList()) {
                if (dateUtils.isToday(model.getDate())) {
                    char degree = '\u00B0';
                    String degreeName = degree + (mConfiguration.isTemperatureFahrengeitMode() ? "F" : "C");
                    String notificationPattern = Application.getInstance().getString(R.string.notification_pattern);

                    notification = notificationPattern.replace("[DESC]", model.getState().getDisplayName())
                            .replace("[TEMP_MAX]", model.getMaxTemperature() + degreeName)
                            .replace("[TEMP_MIN]", model.getMinTemperature() + degreeName);
                }
            }
        }

        return notification;

    }
}
