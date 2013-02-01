package com.massivekinetics.ow.activities;

import android.content.Intent;
import android.os.Bundle;
import com.massivekinetics.ow.R;

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
        uiHandler.postDelayed(showForecastRunnable, delay);
    }
}