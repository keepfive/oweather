package com.massivekinetics.ow.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import com.massivekinetics.ow.R;
import com.massivekinetics.ow.application.OWApplication;
import com.massivekinetics.ow.data.manager.ConfigManager;
import com.massivekinetics.ow.location.OWLocationManager;
import com.massivekinetics.ow.utils.DateUtils;

import static com.massivekinetics.ow.data.manager.ConfigManager.*;

/**
 * Created with IntelliJ IDEA.
 * User: bovy
 * Date: 2/2/13
 * Time: 12:05 AM
 * To change this template use File | Settings | File Templates.
 */
public class SplashScreenActivity extends OWActivity {
    private static boolean isInitialized = false;

    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

    }

    private void checkLocation() {
        goNext(2000);

      /*  if(configManager.getBooleanConfig(AUTO_DEFINE_LOCATION_ENABLED)){
            locationManager.getLocation(this, locationResult);
        } else {
            goNext(2000);
        }*/
    }

    public void onResume() {
        super.onResume();
        initApplication();
        checkLocation();
    }

    private void goNext(final int delay){

        uiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent nextActivity;
                if (configManager.getStringConfig(ConfigManager.GPS_PARAMS) == null) {
                    nextActivity = new Intent(SplashScreenActivity.this, SettingsActivity.class);

                } else {
                    nextActivity = new Intent(SplashScreenActivity.this, ForecastPageActivity.class);
                }

                startActivity(nextActivity);
                finish();
            }
        }, delay);
    }

    @Override
    protected void initViews() {}

    @Override
    protected void initListeners() {}

    public void onPause() {
        super.onPause();
        isInitialized = false;
    }

    void initApplication() {
        if (!isInitialized) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            getOWApplication().setDisplayMetrics(displayMetrics);
            getOWApplication().getDataManager().restoreForecast();
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

    OWLocationManager.LocationResult locationResult = new OWLocationManager.LocationResult() {
        @Override
        public void gotLocation(Location location) {
            String cityName = null;
            String gpsParams = null;
            try {
                cityName = locationManager.getCityName(location);
                gpsParams = locationManager.getGpsCoordinatesAsParams(location);
            } catch (Exception e) {

            }

            if(cityName!=null && gpsParams!=null){
                configManager.setConfig(CITY_NAME, cityName);
                configManager.setConfig(GPS_PARAMS, gpsParams);
                configManager.setConfig(GPS_LAST_UPDATED, DateUtils.getCurrentInMillis());
            }

            goNext(100);
        }
    };
}