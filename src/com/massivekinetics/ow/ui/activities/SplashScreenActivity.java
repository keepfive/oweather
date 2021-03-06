package com.massivekinetics.ow.ui.activities;

import android.os.Bundle;
import android.view.Window;
import com.massivekinetics.ow.R;
import com.massivekinetics.ow.domain.tasks.GetSessionTask;
import com.massivekinetics.ow.ui.interfaces.LoadingListener;
import com.massivekinetics.ow.services.network.NetworkService;
import com.massivekinetics.ow.services.utils.NavigationService;
import com.massivekinetics.ow.services.utils.StringUtils;

/**
 * Created with IntelliJ IDEA.
 * User: bovy
 * Date: 2/2/13
 * Time: 12:05 AM
 * To change this template use File | Settings | File Templates.
 */
public class SplashScreenActivity extends BaseActivity {

    private LoadingListener<Boolean> listener = new LoadingListener<Boolean>() {
        @Override
        public void onLoaded(Boolean isSessionSucceded) {
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
        new GetSessionTask(mConfiguration, listener, 5).execute();
    }

    private void goNext(final int delay) {
        uiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!NetworkService.isOnline())
                    NavigationService.navigate(SplashScreenActivity.this, ErrorActivity.class);
                else if (StringUtils.isNullOrEmpty(mConfiguration.getLocationCoordinates()))
                    NavigationService.navigate(SplashScreenActivity.this, FirstLocationActivity.class);
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
    }
}