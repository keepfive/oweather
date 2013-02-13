package com.massivekinetics.ow.activities;

import android.location.Location;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import com.massivekinetics.ow.R;
import com.massivekinetics.ow.location.MyLocation;
import com.massivekinetics.ow.location.OWLocationManager;

/**
 * Created with IntelliJ IDEA.
 * User: bovy
 * Date: 2/8/13
 * Time: 10:18 AM
 * To change this template use File | Settings | File Templates.
 */
public class SettingsActivity extends OWActivity {
    ImageButton btnBack, btnCelcius, btnFahrenheit;
    TextView tvLocationTitle, tvAutoDefineTitle, tvNotificationTitle;
    TextView tvNotificationMessage, tvNotificationTime;
    EditText etUserLocation;
    View progressBar;
    CompoundButton switchAutoDefine, switchNotification;
    MyLocation location;
    MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {
        @Override
        public void gotLocation(Location location) {
            String loc = null;

            try {
                loc = locMgr.getCityName(location);
            } catch (Exception e) {
            }

            final String locVal = loc;
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (locVal != null)
                        etUserLocation.setText(locVal);
                    else
                        etUserLocation.setText("Could not find");

                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    };
    OWLocationManager locMgr = new OWLocationManager();

    private View.OnClickListener temperatureModeListener = new View.OnClickListener() {
        boolean isCelsius = true;
        @Override
        public void onClick(View v) {
            isCelsius = !isCelsius;
            final int resIdC = isCelsius ? R.drawable.celcius_light : R.drawable.celcius_dark;
            final int resIdF = isCelsius ? R.drawable.fahrenheit_dark : R.drawable.fahrenheit_light;
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    btnCelcius.setImageResource(resIdC);
                    btnFahrenheit.setImageResource(resIdF);
                }
            });
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        initViews();
        initListeners();

        tryGetLocation();
    }

    private void tryGetLocation() {
        progressBar.setVisibility(View.VISIBLE);

        location = new MyLocation();
        location.getLocation(this, locationResult);
    }

    @Override
    protected void initViews() {
        btnBack = (ImageButton) findViewById(R.id.ibBack);
        btnCelcius = (ImageButton) findViewById(R.id.ibCelcius);
        btnFahrenheit = (ImageButton) findViewById(R.id.ibFahrenheit);

        tvLocationTitle = (TextView) findViewById(R.id.tvLocationTitle);
        tvAutoDefineTitle = (TextView) findViewById(R.id.tvAutoDefine);
        tvNotificationTitle = (TextView) findViewById(R.id.tvNotification);
        tvNotificationMessage = (TextView) findViewById(R.id.tvNotificationMsg);
        tvNotificationTime = (TextView) findViewById(R.id.tvNotificationTime);

        tvNotificationMessage.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.reminder_arrow, 0);

        etUserLocation = (EditText) findViewById(R.id.etUserLocation);
        progressBar = findViewById(R.id.progressBar);
        switchAutoDefine = (CompoundButton) findViewById(R.id.switchAutoDefine);
        switchNotification = (CompoundButton) findViewById(R.id.switchNotification);

        setFont(tvLocationTitle, tvAutoDefineTitle, tvNotificationTitle, tvNotificationMessage, tvNotificationTime, etUserLocation);
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
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    setLocation(etUserLocation.getText().toString());
                    return true;
                }
                return false;
            }
        });

        btnCelcius.setOnClickListener(temperatureModeListener);

        btnFahrenheit.setOnClickListener(temperatureModeListener);
    }

    private void setLocation(String areaName) {
    }


}