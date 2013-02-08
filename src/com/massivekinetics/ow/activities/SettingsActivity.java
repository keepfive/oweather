package com.massivekinetics.ow.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import com.massivekinetics.ow.R;

/**
 * Created with IntelliJ IDEA.
 * User: bovy
 * Date: 2/8/13
 * Time: 10:18 AM
 * To change this template use File | Settings | File Templates.
 */
public class SettingsActivity extends OWActivity {
    ImageButton btnBack;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        btnBack = (ImageButton)findViewById(R.id.ibBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsActivity.this.finish();
            }
        });
    }
}