package com.massivekinetics.ow.activities;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.massivekinetics.ow.R;
import com.massivekinetics.ow.application.OWApplication;
import com.massivekinetics.ow.interfaces.ProgressListener;
import com.massivekinetics.ow.location.OWLocationManager;
import com.massivekinetics.ow.utils.StringUtils;

import static com.massivekinetics.ow.data.manager.ConfigManager.*;

/**
 * Created with IntelliJ IDEA.
 * User: bovy
 * Date: 2/8/13
 * Time: 10:18 AM
 * To change this template use File | Settings | File Templates.
 */
public class SettingsActivity extends OWActivity {
    View rootContent;

    ImageButton btnBack, btnCelsius, btnFahrenheit;
    TextView tvLocationTitle, tvAutoDefineTitle, tvNotificationTitle;
    TextView tvNotificationMessage, tvNotificationTime;
    EditText etUserLocation;
    View progressBar;
    CompoundButton switchAutoDefine, switchNotification;

    OWLocationManager locationMgr;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.settings);
        initViews();
    }

    @Override
    public void onResume(){
        super.onResume();
        setFont(tvLocationTitle, tvAutoDefineTitle, tvNotificationTitle, tvNotificationMessage, tvNotificationTime, etUserLocation);
        checkConfig();
        initListeners();
    }

    private void tryGetLocation() {
        progressBar.setVisibility(View.VISIBLE);
        locationMgr = new OWLocationManager();
        boolean isLocationAvailable = locationMgr.getLocation(this, locationResult);
        if(isLocationAvailable)
            progressListener.showProgress();
        else
            notifier.alert("Your location could not been retrieved. Please add location manually", Toast.LENGTH_LONG);
    }

    @Override
    protected void initViews() {
        rootContent = findViewById(android.R.id.content);

        btnBack = (ImageButton) findViewById(R.id.ibBack);
        btnCelsius = (ImageButton) findViewById(R.id.ibCelcius);
        btnFahrenheit = (ImageButton) findViewById(R.id.ibFahrenheit);

        tvLocationTitle = (TextView) findViewById(R.id.tvLocationTitle);
        tvAutoDefineTitle = (TextView) findViewById(R.id.tvAutoDefine);
        tvNotificationTitle = (TextView) findViewById(R.id.tvNotification);
        tvNotificationMessage = (TextView) findViewById(R.id.tvNotificationMsg);
        tvNotificationTime = (TextView) findViewById(R.id.tvNotificationTime);

        tvNotificationTime.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.reminder_arrow, 0);

        etUserLocation = (EditText) findViewById(R.id.etUserLocation);
        progressBar = findViewById(R.id.progressBar);
        switchAutoDefine = (CompoundButton) findViewById(R.id.switchAutoDefine);
        switchNotification = (CompoundButton) findViewById(R.id.switchNotification);


    }

    @Override
    protected void initListeners() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsActivity.this.finish();
            }
        });

        etUserLocation.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN  ) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    setUserLocation(etUserLocation.getText().toString(), "temp");
                    InputMethodManager imm =
                            (InputMethodManager)OWApplication.context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etUserLocation.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

        btnCelsius.setOnClickListener(temperatureModeListener);

        btnFahrenheit.setOnClickListener(temperatureModeListener);
        switchAutoDefine.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                configManager.setConfig(AUTO_DEFINE_LOCATION_ENABLED, isChecked);
                if(isChecked)
                    tryGetLocation();
            }
        });

        switchNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                configManager.setConfig(NOTIFICATION_ENABLED, isChecked);
                if(isChecked)
                    turnOnNotification();
            }
        });

    }

    private void turnOnNotification() {
    }

    private void checkConfig() {
        boolean isAutoDefineLocation = configManager.getBooleanConfig(AUTO_DEFINE_LOCATION_ENABLED);
        boolean isNotificationEnabled = configManager.getBooleanConfig(NOTIFICATION_ENABLED);
        boolean isFahrenheit = configManager.getBooleanConfig(TEMPERATURE_MODE_FAHRENHEIT);
        String cityName = configManager.getStringConfig(CITY_NAME);
        int notificationHour = configManager.getIntConfig(NOTIFICATION_TIME_HOUR);
        int notificationMin = configManager.getIntConfig(NOTIFICATION_TIME_MINUTE);

        switchAutoDefine.setChecked(isAutoDefineLocation);
        switchNotification.setChecked(isNotificationEnabled);

        final int resIdC = isFahrenheit ? R.drawable.celcius_dark : R.drawable.celcius_light;
        final int resIdF = isFahrenheit ? R.drawable.fahrenheit_light : R.drawable.fahrenheit_dark;

        btnCelsius.setImageResource(resIdC);
        btnFahrenheit.setImageResource(resIdF);

        etUserLocation.setText(cityName);

        String notificationTime = notificationHour + ":" + ((notificationMin<10) ? "0" + notificationMin : notificationMin);
        tvNotificationTime.setText(notificationTime);
    }


    private void setUserLocation(String cityName, String gpsParams) {
        configManager.setConfig(CITY_NAME, cityName);
        configManager.setConfig(GPS_PARAMS, gpsParams);
        notifier.alert(getString(R.string.location_saved), Toast.LENGTH_SHORT);
    }

    OWLocationManager.LocationResult locationResult = new OWLocationManager.LocationResult() {
        @Override
        public void gotLocation(Location location) {
            String cityName = null;
            String gpsParams = null;
            try {
                cityName = locationMgr.getCityName(location);
                gpsParams = locationMgr.getGpsCoordinatesAsParams(location);
            } catch (Exception e) {}

            final String finalCityName = cityName;
            final String finalGpsParams = gpsParams;

            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (!StringUtils.isNullOrEmpty(finalCityName) && !StringUtils.isNullOrEmpty(finalGpsParams)){
                        etUserLocation.setText(finalCityName);
                        setUserLocation(finalCityName, finalGpsParams);
                    }
                    else
                        notifier.alert(getString(R.string.could_not_locate), Toast.LENGTH_LONG);
                    progressListener.hideProgress();
                }
            });
        }
    };

    private View.OnClickListener temperatureModeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean isFahrenheit = configManager.getBooleanConfig(TEMPERATURE_MODE_FAHRENHEIT);
            isFahrenheit = !isFahrenheit;
            configManager.setConfig(TEMPERATURE_MODE_FAHRENHEIT, isFahrenheit);
            final int resIdC = isFahrenheit ? R.drawable.celcius_dark :  R.drawable.celcius_light;
            final int resIdF = isFahrenheit ? R.drawable.fahrenheit_light : R.drawable.fahrenheit_dark;
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    btnCelsius.setImageResource(resIdC);
                    btnFahrenheit.setImageResource(resIdF);
                }
            });
        }
    };

    private ProgressListener progressListener = new ProgressListener() {
        @Override
        public void showProgress() {
            progressBar.setVisibility(View.VISIBLE);
            rootContent.setEnabled(false);
        }

        @Override
        public void hideProgress() {
            progressBar.setVisibility(View.GONE);
            rootContent.setEnabled(true);
        }
    };


}