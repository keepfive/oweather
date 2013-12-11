package com.massivekinetics.ow.ui.activities;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import com.massivekinetics.ow.R;
import com.massivekinetics.ow.application.Font;
import com.massivekinetics.ow.application.manager.NotificationService;
import com.massivekinetics.ow.ui.views.timepicker.TimePicker;

/**
 * Created with IntelliJ IDEA.
 * User: bovy
 * Date: 4/24/13
 * Time: 1:43 PM
 */
public class NotificationSettingsActivity extends BaseActivity {
    Button btnSet;
    TimePicker timePicker;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.notification_page);

        initViews();
        initListeners();
    }

    @Override
    protected void initViews() {
        btnSet = (Button)findViewById(R.id.btnSet);
        btnSet.setTypeface(Font.DEFAULT.get());
        timePicker = (TimePicker)findViewById(R.id.timePicker);
        boolean is24hour = DateFormat.is24HourFormat(this);
        timePicker.setIs24HourView(is24hour);
        setFont((ViewGroup)findViewById(R.id.rootContainer));
    }

    @Override
    protected void initListeners() {
        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = timePicker.getCurrentHour();
                int minute = timePicker.getCurrentMinute();
                mConfiguration.setNotificationTime(hour, minute);
                NotificationService.reset();
                finish();
            }
        });
    }
}