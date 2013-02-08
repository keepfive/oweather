package com.massivekinetics.ow.activities;

import android.content.Intent;
import android.os.Bundle;
import com.massivekinetics.ow.R;
import com.massivekinetics.ow.application.OWApplication;
import com.massivekinetics.ow.data.WeatherForecast;
import com.massivekinetics.ow.data.manager.DataManager;
import com.massivekinetics.ow.data.manager.WeatherDataManager;
import com.massivekinetics.ow.data.tasks.GetWeatherTask;
import com.massivekinetics.ow.data.tasks.LoadingListener;

/**
 * Created with IntelliJ IDEA.
 * User: bovy
 * Date: 2/2/13
 * Time: 12:05 AM
 * To change this template use File | Settings | File Templates.
 */
public class SplashScreenActivity extends OWActivity {
    int delay = 5000;
    Runnable showForecastRunnable = new Runnable() {
        @Override
        public void run() {
            Intent showForecastIntent = new Intent(SplashScreenActivity.this, ForecastPageActivity.class);
            startActivity(showForecastIntent);
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        new GetWeatherTask(new LoadingListener<WeatherForecast>() {
            @Override
            public void callback(WeatherForecast result) {
                if (result.isSuccessed()) {
                    DataManager dataManager = new WeatherDataManager(result);
                    OWApplication.context.setDataManager(dataManager);
                }
                else{

                }
                notifyStop();
            }

            @Override
            public void notifyStart() {
            }

            @Override
            public void notifyStop() {
                uiHandler.post(showForecastRunnable);
                SplashScreenActivity.this.finish();
            }
        }).execute("kiev", "5");

    }
}