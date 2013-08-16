package com.massivekinetics.ow.activities;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.massivekinetics.ow.R;
import com.massivekinetics.ow.application.OWApplication;
import com.massivekinetics.ow.data.Autocompleter;
import com.massivekinetics.ow.data.adapters.PlacesAutoCompleteAdapter;
import com.massivekinetics.ow.data.model.Prediction;
import com.massivekinetics.ow.data.parser.geocoder.GeocoderConstants;
import com.massivekinetics.ow.data.tasks.GetLocationFromPlaceTask;
import com.massivekinetics.ow.data.tasks.LoadingListener;
import com.massivekinetics.ow.interfaces.ProgressListener;
import com.massivekinetics.ow.location.OWLocationManager;
import com.massivekinetics.ow.utils.DateUtils;
import com.massivekinetics.ow.utils.NavigationService;
import com.massivekinetics.ow.utils.StringUtils;
import com.massivekinetics.ow.widgets.ClockUpdateService;

import java.util.HashMap;
import java.util.Map;

import static com.massivekinetics.ow.data.manager.ConfigManager.GPS_LAST_UPDATED;
import static com.massivekinetics.ow.data.parser.geocoder.GeocoderConstants.*;

/**
 * Created with IntelliJ IDEA.
 * User: bovy
 * Date: 6/11/13
 * Time: 1:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class FirstLocationActivity extends OWActivity implements AdapterView.OnItemClickListener {
    AutoCompleteTextView autoCompleteLocation;
    View progressBar;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.first_location_page);
        initViews();
    }

    @Override
    public void onResume() {
        super.onResume();
        setFont(autoCompleteLocation);
        initListeners();
        tryGetLocation();
    }

    @Override
    protected void initViews() {
        autoCompleteLocation = (AutoCompleteTextView) findViewById(R.id.location);
        ArrayAdapter adapter = new PlacesAutoCompleteAdapter(FirstLocationActivity.this, R.layout.prediction);
        autoCompleteLocation.setAdapter(adapter);
        progressBar = findViewById(R.id.progressBar);
        autoCompleteLocation.setEnabled(false);
    }

    @Override
    protected void initListeners() {
        autoCompleteLocation.setOnItemClickListener(this);
    }

    private ProgressListener progressListener = new ProgressListener() {
        @Override
        public void showProgress() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void hideProgress() {
            progressBar.setVisibility(View.GONE);
            autoCompleteLocation.setEnabled(true);
        }
    };

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
                                autoCompleteLocation.setText(cityName);
                                setUserLocation(locationInfoMap, true);
                            } else {
                                notifier.alert(getString(R.string.could_not_locate), Toast.LENGTH_LONG);
                                autoCompleteLocation.setText(configManager.getLocationName());
                            }
                            progressListener.hideProgress();
                        }
                    });
                }
            }.start();
        }
    };

    private void setUserLocation(Map<String, String> locationInfoMap, boolean isAutoDefined) {
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
        configManager.setAutoDefineLocation(isAutoDefined);

        startService(new Intent(ClockUpdateService.ACTION_LOCATION_UPDATED));

        NavigationService.navigate(FirstLocationActivity.this, UpdatePageActivity.class);
        finish();
    }

    private void tryGetLocation() {
        boolean isLocationAvailable = locationManager.getLocation(this, locationResult);
        if (isLocationAvailable) {
            progressListener.showProgress();
            autoCompleteLocation.setText(getString(R.string.retrieving_location));
        } else
            notifier.alert(getString(R.string.could_not_locate), Toast.LENGTH_LONG);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final Prediction prediction = (Prediction) parent.getItemAtPosition(position);
        autoCompleteLocation.setText(prediction.getCity());
        new GetLocationFromPlaceTask(prediction.getReference(), new Autocompleter(), autocompleteListener).execute();
        InputMethodManager imm = (InputMethodManager) OWApplication.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(autoCompleteLocation.getWindowToken(), 0);
    }

    LoadingListener<String> autocompleteListener = new LoadingListener<String>() {
        @Override
        public void callback(String result) {
            if (result != null)  {
                Map<String, String> locMap = new HashMap<String, String>(2);
                locMap.put(LOCALITY,autoCompleteLocation.getText().toString());
                locMap.put(GPS_PARAMS, result);
                setUserLocation(locMap, false);
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
}