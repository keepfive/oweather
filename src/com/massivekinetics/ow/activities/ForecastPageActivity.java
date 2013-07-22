package com.massivekinetics.ow.activities;

import android.app.NotificationManager;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.TouchDelegate;
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
import com.massivekinetics.ow.utils.*;
import com.massivekinetics.ow.views.SwipeIndicatorPresenter;

public class ForecastPageActivity extends OWActivity {
    public static final String ACTION = "com.massivekinetics.ow.forecast";

    ViewGroup weatherContainer, updateLayout;
    TextView tvDate, tvCurrentTemp, tvDaytime, tvNightTemp, tvWeatherDescription, tvMinus, tvLocation;
    TextView tvHumidity, tvWindSpeed, tvMoonPhase, tvWindDirection, tvLocationName;
    ImageView ivWindDirection, ivHumidity, ivRefreshIndicator, ivLunarState;
    ImageButton ibSettings, ibPrevious, ibNext, ibRefresh;

    ViewPager viewPager;
    DataManager dataManager;
    SwipeIndicatorPresenter swipeIndicatorPresenter;
    WeatherForecast weatherForecast;
    LoadingListener<WeatherForecast> listener = new LoadingListener<WeatherForecast>() {
        @Override
        public void callback(WeatherForecast result) {
            if(!result.isSuccessed()){
                Bundle data = new Bundle();
                data.putCharSequence(ErrorActivity.ERROR_DESCRIPTION, getString(R.string.no_cache));
                NavigationService.navigate(ForecastPageActivity.this, ErrorActivity.class);
                finish();
            }
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
        checkIntent();
        initViews();
        initListeners();
    }

    private void checkIntent() {
        Bundle extras = getIntent().getExtras();
        if(extras != null && extras.containsKey(OWNotification.NOTIFICATION_ID_KEY)){
            //---look up the notification manager service---
            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            //---cancel the notification---
            nm.cancel(extras.getInt(OWNotification.NOTIFICATION_ID_KEY));
        }
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
        ivLunarState = (ImageView) findViewById(R.id.ivLunarState);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(10);

        ibSettings = (ImageButton) findViewById(R.id.ibSettings);
        ibNext = (ImageButton) findViewById(R.id.ibNext);
        ibPrevious = (ImageButton) findViewById(R.id.ibPrevious);
        ibRefresh = (ImageButton) findViewById(R.id.ibRefresh);

        indicatorLayout = (ViewGroup) findViewById(R.id.indicator);
        if(isTablet)
            tvLocation = (TextView) findViewById(R.id.tvLocationMain);
        setFont(weatherContainer);
        setFont(updateLayout);

        setTouchDelegates(findViewById(R.id.weather_container), ibSettings, 150);
        setTouchDelegates(findViewById(R.id.header), ibRefresh, 150);
        setTouchDelegates(findViewById(R.id.weather_desc_block), viewPager, 200);
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
                //NavigationService.navigate(ForecastPageActivity.this, TestActivity.class);
                NavigationService.navigate(ForecastPageActivity.this, SettingsActivity.class);
                finish();
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
            notifier.alert(getString(R.string.weather_not_updated), Toast.LENGTH_LONG);
            return;
        }

        weatherContainer.setVisibility(View.VISIBLE);
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
        int size = weatherForecast.getForecastList().size();

        if(position == 0)
            ibPrevious.setAlpha(128);
        else if(position == size-1)
            ibNext.setAlpha(128);
        else{
            ibPrevious.setAlpha(255);
            ibNext.setAlpha(255);
        }

        WeatherModel model = weatherForecast.getForecastList().get(position);

        DateUtils utils = new DateUtils();
        String nameOfDay = utils.getDayName(model.getDate());

        tvDate.setText(nameOfDay);

        int currentTemp = Integer.parseInt(model.getMaxTemperature());

        if (currentTemp < 0)
            tvMinus.setVisibility(View.VISIBLE);
        else{
            int invis = isTablet ? View.GONE : View.INVISIBLE;
            tvMinus.setVisibility(invis);
        }
        currentTemp = Math.abs(currentTemp);
        //int margin = resolveMargin(currentTemp);

        tvCurrentTemp.setText("" + currentTemp);

        //setMarginParams(margin, tvCurrentTemp);

        if(isTablet){
            String locationName = configManager.getStringConfig(ConfigManager.CITY_NAME);
            locationName = (locationName == null) ? "" : locationName;
            tvLocation.setText(locationName);
        }

        tvNightTemp.setText(model.getMinTemperature());
        if (position == 0) {
            tvHumidity.setText(model.getHumidity() + " %");
            ivHumidity.setImageResource(getHumidityResource((model.getHumidity())));
        } else {
            tvHumidity.setText(model.getPrecipitation());
            ivHumidity.setImageResource(R.drawable.precipmm);
        }

        tvWindSpeed.setText(model.getWindSpeed());
        tvWindDirection.setText(model.getWindDirection());
        ivLunarState.setImageResource(ResourcesCodeUtils.getLunarStateImageResource(model.getLunarState()));

        WeatherState weatherState = model.getState();
        tvWeatherDescription.setText(weatherState.getValue());
        int backgroundColor = ResourcesCodeUtils.getWeatherBackgroundColor(weatherState);

        OWAnimationUtils.rotate(ivWindDirection, Float.parseFloat(model.getWindDegree())+180);
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

        int margin = (temperature >= 10) ? 20 : 45;
        return margin * density;
    }

    private void setMarginParams(int margin, TextView textView) {
        textView.setPadding(textView.getPaddingLeft(), textView.getPaddingTop(), margin, textView.getPaddingBottom());
    }


}
