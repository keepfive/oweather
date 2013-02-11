package com.massivekinetics.ow.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
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
public class SplashScreenActivity extends Activity {
    private static boolean isInitialized = false;

    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        initApllication();

        new GetWeatherTask(new LoadingListener<WeatherForecast>() {
            @Override
            public void callback(WeatherForecast result) {
                if (result.isSuccessed()) {
                    DataManager dataManager = new WeatherDataManager(result);
                    OWApplication.context.setDataManager(dataManager);
                } else {

                }
                notifyStop();
            }

            @Override
            public void notifyStart() {
            }

            @Override
            public void notifyStop() {
                Intent showForecastIntent = new Intent(SplashScreenActivity.this, ForecastPageActivity.class);
                startActivity(showForecastIntent);
            }
        }).execute("kiev", "5");

    }

    void initApllication() {
        if (!isInitialized) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            ((OWApplication) getApplication()).setDisplayMetrics(displayMetrics);
            initFonts();
            isInitialized = true;
        }
    }

    void initFonts() {
        Typeface fontThin = Typeface.createFromAsset(getAssets(), "titilliumthin.ttf");
        Typeface fontItalic = Typeface.createFromAsset(getAssets(), "titiliumitalic.ttf");
        ((OWApplication) getApplication()).setFontThin(fontThin);
        ((OWApplication) getApplication()).setFontItalic(fontItalic);
    }
}