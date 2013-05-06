package com.massivekinetics.ow.activities;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import com.massivekinetics.ow.R;
import com.massivekinetics.ow.utils.NavigationService;

/**
 * Created with IntelliJ IDEA.
 * User: bovy
 * Date: 4/24/13
 * Time: 4:28 PM
 */
public class ErrorActivity extends OWActivity {
    ViewGroup rootContainer;
    Button btnSettings;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.error_screen);
        initViews();
        initListeners();
    }

    @Override
    protected void initViews() {
        btnSettings = (Button)findViewById(R.id.btnSettings);
        rootContainer = (ViewGroup)findViewById(R.id.rootContainer);
        setFont(rootContainer);
    }

    @Override
    protected void initListeners() {
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationService.navigate(ErrorActivity.this, SettingsActivity.class);
                finish();
            }
        });
    }
}