package com.massivekinetics.ow.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.*;
import com.massivekinetics.ow.R;
import com.massivekinetics.ow.application.OWApplication;
import com.massivekinetics.ow.data.adapters.WeatherPagerAdapter;
import com.massivekinetics.ow.data.manager.ConfigManager;
import com.massivekinetics.ow.data.manager.DataManager;
import com.massivekinetics.ow.data.manager.WeatherDataManager;
import com.massivekinetics.ow.data.model.WeatherForecast;
import com.massivekinetics.ow.data.model.WeatherModel;
import com.massivekinetics.ow.data.tasks.LoadingListener;
import com.massivekinetics.ow.states.WeatherState;
import com.massivekinetics.ow.utils.DateUtils;
import com.massivekinetics.ow.utils.OWAnimationUtils;
import com.massivekinetics.ow.utils.StringUtils;
import com.massivekinetics.ow.utils.WeatherCodeUtils;
import com.massivekinetics.ow.views.SwipeIndicatorPresenter;

public class ForecastPageActivity extends OWActivity {
    ViewGroup weatherContainer, updateLayout;
    TextView tvDate, tvCurrentTemp, tvDaytime, tvNightTemp, tvWeatherDescription, tvMinus;
    TextView tvHumidity, tvWindSpeed, tvMoonPhase, tvWindDirection, tvLocationName;
    ImageView ivWindDirection, ivHumidity, ivRefreshIndicator;
    ImageButton ibSettings, ibPrevious, ibNext, ibRefresh;
    ViewPager viewPager;
    DataManager dataManager;
    RelativeLayout content;
    SwipeIndicatorPresenter swipeIndicatorPresenter;
    WeatherForecast weatherForecast;
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
    private ViewGroup indicatorLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.forecast_page);
        super.onCreate(savedInstanceState);

        dataManager = WeatherDataManager.getInstance();

        initViews();
        initListeners();
    }

    @Override
    public void onResume() {
        super.onResume();
        dataManager.getWeatherForecast(listener);
    }

    @Override
    protected void initViews() {

        weatherContainer = (ViewGroup) findViewById(R.id.weather_container);
        updateLayout = (ViewGroup) findViewById(R.id.updateLayout);
        tvDate = (TextView) findViewById(R.id.tvDate);
        tvDaytime = (TextView) findViewById(R.id.tvDaytime);
        tvNightTemp = (TextView) findViewById(R.id.tvNightTemp);
        tvCurrentTemp = (TextView) findViewById(R.id.tvTemp);
        tvMinus = (TextView) findViewById(R.id.tvMinus);
        tvWeatherDescription = (TextView) findViewById(R.id.tvWeatherDesctiption);
        tvHumidity = (TextView) findViewById(R.id.tvHumidity);
        tvWindSpeed = (TextView) findViewById(R.id.tvWindSpeed);
        tvMoonPhase = (TextView) findViewById(R.id.tvMoonPhase);
        tvWindDirection = (TextView) findViewById(R.id.tvWindDirection);
        tvLocationName = (TextView) findViewById(R.id.tvLocationName);

        ivWindDirection = (ImageView) findViewById(R.id.ivWindDirection);
        ivHumidity = (ImageView) findViewById(R.id.ivHumidity);
        ivRefreshIndicator = (ImageView) findViewById(R.id.ivRefresh);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(10);

        ibSettings = (ImageButton) findViewById(R.id.ibSettings);
        ibNext = (ImageButton) findViewById(R.id.ibNext);
        ibPrevious = (ImageButton) findViewById(R.id.ibPrevious);
        ibRefresh = (ImageButton) findViewById(R.id.ibRefresh);
        content = (RelativeLayout) findViewById(R.id.content);

        indicatorLayout = (ViewGroup) findViewById(R.id.indicator);
        setFont(weatherContainer);
        setFont(updateLayout);
        //setFont(tvDate, tvCurrentTemp, tvMinus, tvNightTemp, tvDaytime, tvWeatherDescription, tvHumidity, tvWindSpeed, tvMoonPhase);
    }

    protected void runWeatherUpdate() {
        LoadingListener<WeatherForecast> listener = new LoadingListener<WeatherForecast>() {
            @Override
            public void callback(WeatherForecast result) {
                updateWeatherInfo(result);
            }

            @Override
            public void notifyStart() {
                OWAnimationUtils.startRotation(ivRefreshIndicator);
                String locationName = configManager.getStringConfig(ConfigManager.CITY_NAME);
                locationName = (locationName == null) ? "" : locationName;
                tvLocationName.setText(locationName);

                updateLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void notifyStop() {
                ivRefreshIndicator.clearAnimation();
                updateLayout.setVisibility(View.GONE);

            }
        };

        dataManager.runUpdate(listener);
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
                int pos = viewPager.getCurrentItem() + 1;
                viewPager.setCurrentItem(pos);

            }
        });


        ibPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = viewPager.getCurrentItem() - 1;
                viewPager.setCurrentItem(pos);

            }
        });

        ibRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runWeatherUpdate();
            }
        });


    }

    private void updateWeatherInfo(WeatherForecast weatherForecast) {
        if (!weatherForecast.isSuccessed()) {
            notifier.alert("Network error", Toast.LENGTH_LONG);
            return;
        } else if (weatherForecast == WeatherForecast.NULL) {
            Intent startSettings = new Intent(ForecastPageActivity.this, SettingsActivity.class);
            startActivity(startSettings);
        }

        this.weatherForecast = weatherForecast;
        swipeIndicatorPresenter = new SwipeIndicatorPresenter(indicatorLayout, weatherForecast.getForecastList().size());

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

    private void updateUI() {
        updateUI(0);
    }

    private void updateUI(final int position) {
        if (!weatherForecast.isSuccessed() || weatherForecast == WeatherForecast.NULL)
            return;
        swipeIndicatorPresenter.setCurrentActivePosition(position);
        WeatherModel model = weatherForecast.getForecastList().get(position);

        DateUtils utils = new DateUtils();
        String nameOfDay = utils.getDayName(model.getDate());

        tvDate.setText(nameOfDay);

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
        tvWindDirection.setText(model.getWindDirection());
        WeatherState weatherState = model.getState();
        tvWeatherDescription.setText(weatherState.getValue());
        int backgroundColor = WeatherCodeUtils.getWeatherBackgroundColor(weatherState);

        OWAnimationUtils.rotate(ivWindDirection, Float.parseFloat(model.getWindDegree()));
        weatherContainer.setBackgroundColor(getResources().getColor(backgroundColor));

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
        int density = 1;

        try {
            density = (int) ((OWApplication) getApplication()).getDisplayMetrics().density;
        } catch (NullPointerException npe) {
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            getOWApplication().setDisplayMetrics(dm);
            density = (int) dm.density;
        }

        int margin = (temperature >= 10) ? 20 : 55;
        return margin * density;
    }

    private void setMarginParams(int margin, TextView textView) {
        textView.setPadding(textView.getPaddingLeft(), textView.getPaddingTop(), margin, textView.getPaddingBottom());
    }


}
