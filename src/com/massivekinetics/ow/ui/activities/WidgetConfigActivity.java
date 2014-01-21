package com.massivekinetics.ow.ui.activities;

import android.app.Activity;
import android.app.ActivityManager;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import com.massivekinetics.ow.R;
import com.massivekinetics.ow.application.Configuration;
import com.massivekinetics.ow.application.IConfiguration;
import com.massivekinetics.ow.ui.widgets.ClockUpdateService;

/**
 * Created with IntelliJ IDEA.
 * User: bovy
 * Date: 8/9/13
 * Time: 12:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class WidgetConfigActivity extends Activity {
    private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private Button set;
    private RadioGroup rgBackground;
    private IConfiguration config;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.widget_config);
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }


        if(appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID)
            finish();

        set = (Button)findViewById(R.id.set);
        rgBackground = (RadioGroup)findViewById(R.id.rgBackgorundColor);
        config = Configuration.INSTANCE();


        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completedConfiguration();
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        setResult(RESULT_CANCELED, null);
    }

    private void completedConfiguration(){
        int checkedId =  rgBackground.getCheckedRadioButtonId();
        boolean isWhite = checkedId == R.id.rbWhite;
        int color = isWhite ?  R.color.widget_bg_white : R.color.widget_bg_black;

        config.setWidgetBackground(appWidgetId, getResources().getColor(color));
        Intent result = new Intent();
        result.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        setResult(RESULT_OK, result);
        startService(new Intent(ClockUpdateService.ACTION_UPDATE));
        finish();
    }
}