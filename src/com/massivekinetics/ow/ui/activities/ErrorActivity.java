package com.massivekinetics.ow.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;
import com.massivekinetics.ow.R;
import com.massivekinetics.ow.application.AppLocator;
import com.massivekinetics.ow.application.IDataManager;
import com.massivekinetics.ow.services.network.NetworkService;
import com.massivekinetics.ow.services.utils.NavigationService;

/**
 * Created with IntelliJ IDEA.
 * User: bovy
 * Date: 4/24/13
 * Time: 4:28 PM
 */
public class ErrorActivity extends BaseActivity {
    public static final String ERROR_DESCRIPTION = "error_description";
    ViewGroup rootContainer;
    Button btnSettings, btnSkip;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.error_screen);
        checkIntent(getIntent());
        initViews();
        initListeners();
    }

    private void checkIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null && extras.containsKey(ERROR_DESCRIPTION))
            notifier.alert(extras.getString(ERROR_DESCRIPTION), Toast.LENGTH_SHORT);

    }

    @Override
    protected void initViews() {
        btnSettings = (Button) findViewById(R.id.btnSettings);
        btnSkip = (Button) findViewById(R.id.btnSkip);
        rootContainer = (ViewGroup) findViewById(R.id.rootContainer);
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
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IDataManager dataManager = AppLocator.resolve(IDataManager.class);
                if (dataManager.hasActualForecast() && !NetworkService.isOnline()) {
                    NavigationService.navigate(ErrorActivity.this, ForecastPageActivity.class);
                    finish();
                } else if (NetworkService.isOnline()) {
                    NavigationService.navigate(ErrorActivity.this, UpdatePageActivity.class);
                    finish();
                } else {
                    notifier.alert(getString(R.string.no_cache), Toast.LENGTH_SHORT);
                }
            }
        }

        );
    }
}