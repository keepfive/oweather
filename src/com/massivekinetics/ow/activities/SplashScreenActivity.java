package com.massivekinetics.ow.activities;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import com.massivekinetics.ow.R;
import com.massivekinetics.ow.data.tasks.GetSessionTask;
import com.massivekinetics.ow.data.tasks.LoadingListener;
import com.massivekinetics.ow.network.NetworkUtils;
import com.massivekinetics.ow.utils.NavigationService;
import com.massivekinetics.ow.utils.StringUtils;

import static com.massivekinetics.ow.utils.Constants.ERROR;

/**
 * Created with IntelliJ IDEA.
 * User: bovy
 * Date: 2/2/13
 * Time: 12:05 AM
 * To change this template use File | Settings | File Templates.
 */
public class SplashScreenActivity extends OWActivity {
    private static boolean isInitialized = false;
    private LoadingListener<Boolean> listener = new LoadingListener<Boolean>() {
        @Override
        public void callback(Boolean isSessionSucceded) {
                goNext(2000);
        }

        @Override
        public void notifyStart() {}

        @Override
        public void notifyStop() {}
    };

    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

    }

    public void onResume() {
        super.onResume();
        initApplication();
        new GetSessionTask(configManager, listener, 5).execute();
    }

    private void goNext(final int delay) {
        uiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!NetworkUtils.isOnline())
                    NavigationService.navigate(SplashScreenActivity.this, ErrorActivity.class);
                else if (StringUtils.isNullOrEmpty(configManager.getLocationCoordinates()))
                    NavigationService.navigate(SplashScreenActivity.this, SettingsActivity.class);
                else
                    NavigationService.navigate(SplashScreenActivity.this, UpdatePageActivity.class);
                finish();
            }
        }, delay);
    }

    @Override
    protected void initViews() {
    }

    @Override
    protected void initListeners() {
    }

    public void onPause() {
        super.onPause();
        isInitialized = false;
    }

    void initApplication() {
        if (!isInitialized) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            getOWApplication().setDisplayMetrics(displayMetrics);
            getOWApplication().getDataManager().restoreForecast();
            isInitialized = true;
        }
    }

    void navigateToErrorActivity(int errorCode) {
        Bundle bundle = new Bundle();
        bundle.putInt(ERROR, errorCode);
        NavigationService.navigate(SplashScreenActivity.this, ErrorActivity.class, bundle);
    }
}