package com.massivekinetics.ow.activities;

import android.location.Location;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import com.massivekinetics.ow.R;
import com.massivekinetics.ow.data.manager.ConfigManager;
import com.massivekinetics.ow.data.tasks.GetSessionTask;
import com.massivekinetics.ow.data.tasks.LoadingListener;
import com.massivekinetics.ow.location.OWLocationManager;
import com.massivekinetics.ow.network.NetworkUtils;
import com.massivekinetics.ow.utils.DateUtils;
import com.massivekinetics.ow.utils.NavigationService;

import static com.massivekinetics.ow.data.manager.ConfigManager.*;
import static com.massivekinetics.ow.utils.Constants.ERROR;

/**
 * Created with IntelliJ IDEA.
 * User: bovy
 * Date: 2/2/13
 * Time: 12:05 AM
 * To change this template use File | Settings | File Templates.
 */
public class SplashScreenActivity extends OWActivity {
    private static boolean isInitialized = false;
    OWLocationManager.LocationResult locationResult = new OWLocationManager.LocationResult() {
        @Override
        public void gotLocation(Location location) {
            String cityName = null;
            String gpsParams = null;
            try {
                cityName = locationManager.getLocationName(location);
                gpsParams = locationManager.getGpsCoordinatesAsParams(location);
            } catch (Exception e) {

            }

            if (cityName != null && gpsParams != null) {
                configManager.setConfig(CITY_NAME, cityName);
                configManager.setConfig(GPS_PARAMS, gpsParams);
                configManager.setConfig(GPS_LAST_UPDATED, DateUtils.getCurrentInMillis());
            }

            goNext(100);
        }
    };
    private LoadingListener<Boolean> listener = new LoadingListener<Boolean>() {
        @Override
        public void callback(Boolean isSessionSucceded) {
            if (isSessionSucceded) {

                checkLocation();

            } else {
                goNext(2000);
            }
        }

        @Override
        public void notifyStart() {

        }

        @Override
        public void notifyStop() {

        }
    };

    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

    }

    private void checkLocation() {
        if (configManager.getBooleanConfig(AUTO_DEFINE_LOCATION_ENABLED)) {
            locationManager.getLocation(this, locationResult);
        } else {
            goNext(2000);
        }
    }

    public void onResume() {
        super.onResume();
        initApplication();
        new GetSessionTask(configManager, listener, 5).execute();
    }

    private void goNext(final int delay) {
        uiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!NetworkUtils.isOnline() || configManager.getStringConfig(ConfigManager.GPS_PARAMS) == null)
                    NavigationService.navigate(SplashScreenActivity.this, ErrorActivity.class);//
                   // navigateToErrorActivity(LOCATION_ERROR);
                else
                    NavigationService.navigate(SplashScreenActivity.this, UpdatePageActivity.class);

                finish();
            }
        }, delay);
    }

    @Override
    protected void initViews() {
    }

    @Override
    protected void initListeners() {
    }

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
            isInitialized = true;
        }
    }

    void navigateToErrorActivity(int errorCode) {
        Bundle bundle = new Bundle();
        bundle.putInt(ERROR, errorCode);
        NavigationService.navigate(SplashScreenActivity.this, ErrorActivity.class, bundle);
    }
}