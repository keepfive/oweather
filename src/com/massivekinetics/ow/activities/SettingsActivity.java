package com.massivekinetics.ow.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.massivekinetics.ow.R;
import com.massivekinetics.ow.application.OWApplication;
import com.massivekinetics.ow.data.Autocompleter;
import com.massivekinetics.ow.data.adapters.PlacesAutoCompleteAdapter;
import com.massivekinetics.ow.data.manager.NotificationService;
import com.massivekinetics.ow.data.model.Prediction;
import com.massivekinetics.ow.data.parser.geocoder.GeocoderConstants;
import com.massivekinetics.ow.data.tasks.GetLocationFromPlaceTask;
import com.massivekinetics.ow.data.tasks.LoadingListener;
import com.massivekinetics.ow.interfaces.ProgressListener;
import com.massivekinetics.ow.location.OWLocationManager;
import com.massivekinetics.ow.network.NetworkUtils;
import com.massivekinetics.ow.utils.DateUtils;
import com.massivekinetics.ow.utils.NavigationService;
import com.massivekinetics.ow.utils.StringUtils;
import com.massivekinetics.ow.widgets.ClockUpdateService;

import java.util.HashMap;
import java.util.Map;

import static com.massivekinetics.ow.data.manager.ConfigManager.*;
import static com.massivekinetics.ow.data.parser.geocoder.GeocoderConstants.COUNTRY;
import static com.massivekinetics.ow.data.parser.geocoder.GeocoderConstants.GPS_PARAMS;
import static com.massivekinetics.ow.data.parser.geocoder.GeocoderConstants.LOCALITY;

/**
 * Created with IntelliJ IDEA.
 * User: bovy
 * Date: 2/8/13
 * Time: 10:18 AM
 * To change this template use File | Settings | File Templates.
 */
public class SettingsActivity extends OWActivity implements AdapterView.OnItemClickListener {
    View rootContent, settingsNotification, btnTemperature;
    ImageButton btnBack, btnCelsius, btnFahrenheit;
    TextView tvLocationTitle, tvAutoDefineTitle, tvNotificationTitle;
    TextView tvNotificationMessage, tvNotificationTime;
    AutoCompleteTextView locationAutoCompleteView;
    View progressBar;
    boolean isLocationChanged, isCheckingLocation;
    CompoundButton switchAutoDefine, switchNotification;
    OWLocationManager locationMgr;

    OWLocationManager.LocationResult locationResult = new OWLocationManager.LocationResult() {

        @Override
        public void gotLocation(final Location location) {
            new Thread() {
                public void run() {
                    final Map<String, String> locationInfoMap = new HashMap<String, String>();
                    try {
                        locationInfoMap.putAll(locationManager.getLocationInfo(location));
                    } catch (Exception e) {

                    }

                    final String cityName = locationInfoMap.get(GeocoderConstants.LOCALITY);
                    final String gpsParams = locationInfoMap.get(GeocoderConstants.GPS_PARAMS);

                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (!StringUtils.isNullOrEmpty(cityName) && !StringUtils.isNullOrEmpty(gpsParams)) {
                                locationAutoCompleteView.setText(cityName);
                                setUserLocation(locationInfoMap);
                            } else {
                                notifier.alert(getString(R.string.could_not_locate), Toast.LENGTH_LONG);
                                locationAutoCompleteView.setText(configManager.getLocationName());
                                switchAutoDefine.setChecked(false);
                            }
                            progressListener.hideProgress();
                        }
                    });
                }
            }.start();
        }
    };

    LoadingListener<String> autocompleteListener = new LoadingListener<String>() {
        @Override
        public void callback(String result) {
            if (result != null)  {
                Map<String, String> locMap = new HashMap<String, String>(2);
                locMap.put(LOCALITY, locationAutoCompleteView.getText().toString());
                locMap.put(GPS_PARAMS, result);
                setUserLocation(locMap);
            }
        }

        @Override
        public void notifyStart() {
            progressListener.showProgress();
        }

        @Override
        public void notifyStop() {
            progressListener.hideProgress();
        }
    };
    private View.OnClickListener temperatureModeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean isFahrenheit = configManager.getBooleanConfig(TEMPERATURE_MODE_FAHRENHEIT);
            isFahrenheit = !isFahrenheit;
            configManager.setConfig(TEMPERATURE_MODE_FAHRENHEIT, isFahrenheit);
            final int resIdC = isFahrenheit ? R.drawable.celcius_dark : R.drawable.celcius_light;
            final int resIdF = isFahrenheit ? R.drawable.fahrenheit_light : R.drawable.fahrenheit_dark;
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    btnCelsius.setImageResource(resIdC);
                    btnFahrenheit.setImageResource(resIdF);
                }
            });
            startService(new Intent(ClockUpdateService.ACTION_UPDATE));
        }
    };
    private ProgressListener progressListener = new ProgressListener() {
        @Override
        public void showProgress() {
            progressBar.setVisibility(View.VISIBLE);
            switchAutoDefine.setEnabled(false);
            switchNotification.setEnabled(false);
            tvNotificationTime.setEnabled(false);
            isCheckingLocation = true;
        }

        @Override
        public void hideProgress() {
            progressBar.setVisibility(View.GONE);
            switchAutoDefine.setEnabled(true);
            switchNotification.setEnabled(true);
            tvNotificationTime.setEnabled(true);
            isCheckingLocation = false;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.settings);
        locationMgr = new OWLocationManager();
        initViews();
    }

    @Override
    public void onResume() {
        super.onResume();
        setFont(tvLocationTitle, tvAutoDefineTitle, tvNotificationTitle, tvNotificationMessage, tvNotificationTime, locationAutoCompleteView);
        checkConfig();
        initListeners();
        locationAutoCompleteView.clearListSelection();
        isLocationChanged = false;
    }

    private void tryGetLocation() {
        locationMgr = new OWLocationManager();
        boolean isLocationAvailable = locationMgr.getLocation(this, locationResult);
        if (isLocationAvailable) {
            progressListener.showProgress();
            locationAutoCompleteView.setText(getString(R.string.retrieving_location));
        } else
            notifier.alert(getString(R.string.could_not_locate), Toast.LENGTH_LONG);
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

        locationAutoCompleteView = (AutoCompleteTextView) findViewById(R.id.etUserLocation);
        progressBar = findViewById(R.id.progressBar);
        switchAutoDefine = (CompoundButton) findViewById(R.id.switchAutoDefine);
        switchNotification = (CompoundButton) findViewById(R.id.switchNotification);
        settingsNotification = findViewById(R.id.settingsNotification);

        btnTemperature = findViewById(R.id.btnTemperature);
        tvLocationTitle.requestFocus();

    }

    @Override
    protected void initListeners() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resolveBackClick();
            }
        });

        locationAutoCompleteView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && locationAutoCompleteView.getAdapter() == null){
                    ArrayAdapter adapter = (configManager.getAutoDefineLocation()) ? null : new PlacesAutoCompleteAdapter(SettingsActivity.this, R.layout.prediction);
                    locationAutoCompleteView.setAdapter(adapter);
                }

                return false;
            }
        });


        locationAutoCompleteView.setOnItemClickListener(this);

        //.setOnClickListener(temperatureModeListener);

       // btnFahrenheit.setOnClickListener(temperatureModeListener);
        btnTemperature.setOnClickListener(temperatureModeListener);

        switchAutoDefine.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                configManager.setAutoDefineLocation(isChecked);
                checkAutocompleteMode();
                if (isChecked) {
                    tryGetLocation();
                }
            }
        });

        switchNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                configManager.setNotificationEnabled(isChecked);
                NotificationService.turnNotification(isChecked);

                int notificationVisibility = isChecked ? View.VISIBLE : View.INVISIBLE;
                settingsNotification.setVisibility(notificationVisibility);
            }
        });

        tvNotificationTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(SettingsActivity.this, NotificationSettingsActivity.class);
                settingsIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(settingsIntent);
            }
        });

        setTouchDelegates(findViewById(android.R.id.content), btnBack, 200);
        setTouchDelegates(findViewById(R.id.settingsHeader), btnTemperature, 200);
    }

    private void checkConfig() {
        boolean isAutoDefineLocation = configManager.getAutoDefineLocation();
        boolean isNotificationEnabled = configManager.isNotificationEnabled();
        boolean isFahrenheit = configManager.getBooleanConfig(TEMPERATURE_MODE_FAHRENHEIT);
        String cityName = configManager.getStringConfig(CITY_NAME);

        switchAutoDefine.setChecked(isAutoDefineLocation);
        checkAutocompleteMode();

        switchNotification.setChecked(isNotificationEnabled);

        int notificationVisibility = isNotificationEnabled ? View.VISIBLE : View.INVISIBLE;
        settingsNotification.setVisibility(notificationVisibility);

        final int resIdC = isFahrenheit ? R.drawable.celcius_dark : R.drawable.celcius_light;
        final int resIdF = isFahrenheit ? R.drawable.fahrenheit_light : R.drawable.fahrenheit_dark;

        btnCelsius.setImageResource(resIdC);
        btnFahrenheit.setImageResource(resIdF);

        if (cityName != null)
            locationAutoCompleteView.setText(cityName);

        if (isAutoDefineLocation)
            tryGetLocation();

        tvNotificationTime.setText(configManager.getNotificationTimeAsString());
    }

    @Override
    public void onBackPressed() {
        if (isCheckingLocation)
            return;

        resolveBackClick();
        super.onBackPressed();
    }

    private void resolveBackClick() {
        Class nextPageClass;

        if (StringUtils.isNullOrEmpty(configManager.getLocationCoordinates()) || !NetworkUtils.isOnline())
            nextPageClass = ErrorActivity.class;

        else if (isLocationChanged)
            nextPageClass = UpdatePageActivity.class;

        else
            nextPageClass = ForecastPageActivity.class;

        NavigationService.navigate(this, nextPageClass);
        this.finish();
    }

    private void setUserLocation(Map<String, String> locationInfoMap) {
        String locality = locationInfoMap.get(LOCALITY);
        String country = locationInfoMap.get(COUNTRY);
        String gpsMarams = locationInfoMap.get(GPS_PARAMS);

        if(!StringUtils.isNullOrEmpty(locality))
            configManager.setLocationName(locality);
        if(!StringUtils.isNullOrEmpty(country))
            configManager.setLocationCountry(country);
        if(!StringUtils.isNullOrEmpty(gpsMarams))
            configManager.setLocationCoordinates(gpsMarams);

        configManager.setConfig(GPS_LAST_UPDATED, DateUtils.getCurrentInMillis());
        isLocationChanged = true;

        startService(new Intent(ClockUpdateService.ACTION_UPDATE));

        notifier.alert(getString(R.string.location_saved), Toast.LENGTH_SHORT);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final Prediction prediction = (Prediction) parent.getItemAtPosition(position);
        locationAutoCompleteView.setText(prediction.getCity());
        new GetLocationFromPlaceTask(prediction.getReference(), new Autocompleter(), autocompleteListener).execute();
        InputMethodManager imm = (InputMethodManager) OWApplication.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(locationAutoCompleteView.getWindowToken(), 0);
    }

    private void checkAutocompleteMode() {
        boolean isAutoDefineEnabled = configManager.getAutoDefineLocation();
        locationAutoCompleteView.setEnabled(!isAutoDefineEnabled);
        locationAutoCompleteView.setAdapter((ArrayAdapter<String>)null);
        Drawable leftDrawable = (isAutoDefineEnabled) ? getResources().getDrawable(R.drawable.location_gps) : null;
        locationAutoCompleteView.setCompoundDrawablesWithIntrinsicBounds(leftDrawable, null, null, null);
    }
}