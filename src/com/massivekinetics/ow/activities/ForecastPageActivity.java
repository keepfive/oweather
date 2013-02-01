package com.massivekinetics.ow.activities;

import android.os.Bundle;
import android.widget.TextView;
import com.massivekinetics.ow.R;
import com.massivekinetics.ow.utils.TypefaceUtil;

public class ForecastPageActivity extends OWActivity {
    TextView tvToday, tvTemp, tvDaytime, tvNightTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forecast_page);
        initLayout();
    }

    private void initLayout() {
        tvToday = (TextView) findViewById(R.id.tvToday);
        tvDaytime = (TextView) findViewById(R.id.tvDaytime);
        tvNightTemp = (TextView) findViewById(R.id.tvNightTemp);
        tvTemp = (TextView) findViewById(R.id.tvTemp);
        setLayoutFont(tvToday, tvTemp, tvNightTemp, tvDaytime);
    }

    private void setLayoutFont(TextView... textViews) {
        for (TextView textView : textViews)
            TypefaceUtil.setTextViewTypeface(textView, fontThin);
    }

}
