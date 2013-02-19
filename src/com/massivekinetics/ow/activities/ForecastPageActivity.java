package com.massivekinetics.ow.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.massivekinetics.ow.R;
import com.massivekinetics.ow.application.OWApplication;
import com.massivekinetics.ow.data.adapters.WeatherPagerAdapter;
import com.massivekinetics.ow.data.manager.DataManager;
import com.massivekinetics.ow.data.manager.WeatherDataManager;
import com.massivekinetics.ow.data.model.WeatherForecast;
import com.massivekinetics.ow.data.model.WeatherModel;
import com.massivekinetics.ow.data.tasks.LoadingListener;
import com.massivekinetics.ow.utils.StringUtils;

public class ForecastPageActivity extends OWActivity {
    TextView tvDate, tvCurrentTemp, tvDaytime, tvNightTemp, tvWeatherDescription, tvMinus;
    TextView tvHumidity, tvWindSpeed, tvMoonPhase;
    ImageView /*ivWeatherState,*/ ivHumidity;
    ImageButton ibSettings, ibPrevious, ibNext;

    ViewPager viewPager;

    DataManager dataManager;

    WeatherForecast weatherForecast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forecast_page);
        dataManager = WeatherDataManager.getInstance();

        initViews();
        initListeners();
        LoadingListener<WeatherForecast> listener = new LoadingListener<WeatherForecast>() {
            @Override
            public void callback(WeatherForecast result) {
                updateWeatherInfo(result);
            }

            @Override
            public void notifyStart() {

            }

            @Override
            public void notifyStop() {

            }
        };
        dataManager.getWeatherForecast(listener);

    }

    @Override
    protected void initViews() {
        tvDate = (TextView) findViewById(R.id.tvDate);
        tvDaytime = (TextView) findViewById(R.id.tvDaytime);
        tvNightTemp = (TextView) findViewById(R.id.tvNightTemp);
        tvCurrentTemp = (TextView) findViewById(R.id.tvTemp);
        tvMinus = (TextView) findViewById(R.id.tvMinus);
        tvWeatherDescription = (TextView) findViewById(R.id.tvWeatherDesctiption);
        tvHumidity = (TextView) findViewById(R.id.tvHumidity);
        tvWindSpeed = (TextView) findViewById(R.id.tvWindSpeed);
        tvMoonPhase = (TextView) findViewById(R.id.tvMoonPhase);
       // ivWeatherState = (ImageView) findViewById(R.id.ivWeatherState);

        ivHumidity = (ImageView) findViewById(R.id.ivHumidity);
        viewPager = (ViewPager)findViewById(R.id.viewPager);

        ibSettings = (ImageButton) findViewById(R.id.ibSettings);
        ibNext = (ImageButton) findViewById(R.id.ibNext);
        ibPrevious = (ImageButton) findViewById(R.id.ibPrevious);

        setFont(tvDate, tvCurrentTemp, tvMinus, tvNightTemp, tvDaytime, tvWeatherDescription, tvHumidity, tvWindSpeed, tvMoonPhase);
    }

    @Override
    protected void initListeners() {
        ibSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(ForecastPageActivity.this, SettingsActivity.class);
                settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(settingsIntent);
            }
        });

        ibNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = viewPager.getCurrentItem()+1;
                viewPager.setCurrentItem(pos);

            }
        });


        ibPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = viewPager.getCurrentItem()-1;
                viewPager.setCurrentItem(pos);

            }
        });




    }

    private void updateWeatherInfo(WeatherForecast weatherForecast) {
       this.weatherForecast = weatherForecast;
       updateUI();
       viewPager.setAdapter(new WeatherPagerAdapter(this, weatherForecast.getForecastList()));
       viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
           @Override
           public void onPageScrolled(int i, float v, int i2) {
           }

           @Override
           public void onPageSelected(int i) {
               updateUI(i);
           }

           @Override
           public void onPageScrollStateChanged(int i) {
           }
       });
    }

    private void updateUI(){
        updateUI(0);
    }

    private void updateUI(final int position) {
        WeatherModel model = weatherForecast.getForecastList().get(position);


        tvDate.setText(model.getDateString());

        int currentTemp = Integer.parseInt(model.getMaxTemperature());
        if (currentTemp < 0)
            tvMinus.setVisibility(View.VISIBLE);
        else
            tvMinus.setVisibility(View.INVISIBLE);

        currentTemp = Math.abs(currentTemp);
        int margin = resolveMargin(currentTemp);

        tvCurrentTemp.setText("" + currentTemp);

        setMarginParams(margin, tvCurrentTemp);


        tvNightTemp.setText(model.getMinTemperature());
        if (model.getHumidity() != null && model.getHumidity() != "") {
            tvHumidity.setText(model.getHumidity() + "%");
            ivHumidity.setImageResource(getHumidityResource((model.getHumidity())));
        } else {
            tvHumidity.setText(model.getPrecipitation() + " mm");
            ivHumidity.setImageResource(R.drawable.precipmm);
        }

        tvWindSpeed.setText(model.getWindSpeedMiles() + " mph");
    }

    private int getHumidityResource(String humidity) {
        if (StringUtils.isNullOrEmpty(humidity))
            return R.drawable.weather_drop_1;

        int resId = Integer.parseInt(humidity);

        if (resId >= 0 && resId <= 33)
            return R.drawable.weather_drop_1;
        else if (resId >= 34 && resId < 67)
            return R.drawable.weather_drop_2;
        else
            return R.drawable.weather_drop_3;
    }

    private int resolveMargin(int temperature) {
        int density = (int) ((OWApplication) getApplication()).getDisplayMetrics().density;
        int margin = (temperature >= 10) ? 20 : 55;
        return margin * density;
    }

    private void setMarginParams(int margin, TextView textView) {
        textView.setPadding(textView.getPaddingLeft(), textView.getPaddingTop(), margin, textView.getPaddingBottom());
    }


}
