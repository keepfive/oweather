package com.massivekinetics.ow.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.massivekinetics.ow.R;
import com.massivekinetics.ow.application.OWApplication;
import com.massivekinetics.ow.data.WeatherForecastChangedListener;
import com.massivekinetics.ow.data.WeatherModel;
import com.massivekinetics.ow.data.manager.DataManager;
import com.massivekinetics.ow.states.WeatherState;
import com.massivekinetics.ow.utils.TypefaceUtil;
import com.massivekinetics.ow.utils.WeatherCodeUtils;

public class ForecastPageActivity extends OWActivity implements WeatherForecastChangedListener {
    TextView tvToday, tvCurrentTemp, tvDaytime, tvNightTemp, tvWeatherDescription;
    TextView tvHumidity, tvWindSpeed, tvMoonPhase;
    ImageView ivWeatherState;
    DataManager dataManager;

    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forecast_page);
        dataManager = OWApplication.context.getDataManager();

        initLayout();
        updateWeatherInfo();
        uiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
               if(i<5){
                   onWeatherForecastChanged();
                   uiHandler.postDelayed(this, 3000);
               }
            }
        }, 3000);
    }

    @Override
    public void onWeatherForecastChanged() {
        updateWeatherInfo();
    }

    private void initLayout() {
        tvToday = (TextView) findViewById(R.id.tvToday);
        tvDaytime = (TextView) findViewById(R.id.tvDaytime);
        tvNightTemp = (TextView) findViewById(R.id.tvNightTemp);
        tvCurrentTemp = (TextView) findViewById(R.id.tvTemp);
        tvWeatherDescription = (TextView) findViewById(R.id.tvWeatherDesctiption);
        tvHumidity = (TextView) findViewById(R.id.tvHumidity);
        tvWindSpeed = (TextView) findViewById(R.id.tvWindSpeed);
        tvMoonPhase = (TextView) findViewById(R.id.tvMoonPhase);
        ivWeatherState = (ImageView) findViewById(R.id.ivWeatherState);

        setLayoutFont(tvToday, tvCurrentTemp, tvNightTemp, tvDaytime, tvWeatherDescription, tvHumidity, tvWindSpeed, tvMoonPhase);
    }

    private void setLayoutFont(TextView... textViews) {
        for (TextView textView : textViews)
            TypefaceUtil.setTextViewTypeface(textView, fontThin);
    }

    private void updateWeatherInfo() {
        WeatherModel model = dataManager.getWeatherForecast().getForecastList().get(i++);
        tvCurrentTemp.setText(model.getTempMaxC());
        tvNightTemp.setText(model.getTempMinC());
        tvHumidity.setText(model.getHumidity() + "%");
        tvWindSpeed.setText(model.getWindSpeedMiles() + " mph");
        WeatherState weatherState = WeatherCodeUtils.getWeatherState(Integer.parseInt(model.getWeatherCode()));
        int resource = WeatherCodeUtils.getWeatherImageResource(weatherState);
        ivWeatherState.setImageResource(resource);
    }


}
