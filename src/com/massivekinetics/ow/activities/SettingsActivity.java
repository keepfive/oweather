package com.massivekinetics.ow.activities;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import com.massivekinetics.ow.R;
import com.massivekinetics.ow.location.OWLocation;
import com.massivekinetics.ow.location.OWLocationManager;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: bovy
 * Date: 2/8/13
 * Time: 10:18 AM
 * To change this template use File | Settings | File Templates.
 */
public class SettingsActivity extends OWActivity {
    ImageButton btnBack;
    TextView tvLocationTitle, tvAutoDefine, tvNotification;
    EditText etUserLocation;
    View progressBar;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        initViews();
        initListeners();

        tryGetLocation();
    }

    private void tryGetLocation() {
        OWLocationManager locationManager = new OWLocationManager();
        OWLocation loc = locationManager.getLastKnownLocation();
        if (loc != OWLocation.NULL) {
            try {
                final String city = locationManager.getCityName(loc.location);
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        etUserLocation.setText(city);
                    }
                });
            } catch (IOException e) {

            }
        }
    }

    @Override
    protected void initViews() {
        btnBack = (ImageButton) findViewById(R.id.ibBack);
        tvLocationTitle = (TextView) findViewById(R.id.tvLocationTitle);
        tvAutoDefine = (TextView) findViewById(R.id.tvAutoDefine);
        tvNotification = (TextView) findViewById(R.id.tvNotification);
        etUserLocation = (EditText) findViewById(R.id.etUserLocation);
        progressBar = findViewById(R.id.progressBar);
        setFont(tvLocationTitle, tvAutoDefine, tvNotification);
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
                    finish();
                    return true;
                }
                return false;
            }
        });
    }


}