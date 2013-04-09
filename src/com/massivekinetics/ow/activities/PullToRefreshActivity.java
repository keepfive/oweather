package com.massivekinetics.ow.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.massivekinetics.ow.R;
import com.massivekinetics.ow.data.manager.ConfigManager;
import com.massivekinetics.ow.utils.OWAnimationUtils;
import com.massivekinetics.ow.views.PullToRefreshLayout;


/**
 * Created with IntelliJ IDEA.
 * User: bovy
 * Date: 2/19/13
 * Time: 10:49 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class PullToRefreshActivity extends OWActivity implements PullToRefreshLayout.OnPullListener, PullToRefreshLayout.OnPullStateListener {

    protected final static int MSG_LOADING = 1;
    protected final static int MSG_LOADED = 2;

    protected Handler handler = new Handler();

    protected PullToRefreshLayout mPullLayout;
    private TextView tvRefresh;
    private ImageView ivRefresh;

    private boolean mInLoading = false;


    @Override
    public void onCreate(Bundle saved) {
        super.onCreate(saved);
        //setContentView(R.layout.test);
        initViews();
    }

    @Override
    protected void initViews() {
        ivRefresh = (ImageView) findViewById(R.id.ivRefresh);
        tvRefresh = (TextView) findViewById(R.id.tvRefresh);
        mPullLayout = (PullToRefreshLayout) findViewById(R.id.pull_container);
        mPullLayout.setOnActionPullListener(this);
        mPullLayout.setOnPullStateChangeListener(this);

        tvRefresh.setText(configManager.getStringConfig(ConfigManager.CITY_NAME));
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOADING:
                    loadData();
                    //sendMessageDelayed(obtainMessage(MSG_LOADED), 2000);
                    break;
                case MSG_LOADED:
                    dataLoaded();
                    break;
            }
        }
    };

    private void startLoading() {
        if (!mInLoading) {
            mInLoading = true;
            mPullLayout.setEnableStopInActionView(true);
            ivRefresh.clearAnimation();
            OWAnimationUtils.startRotation(ivRefresh);
            ivRefresh.setVisibility(View.VISIBLE);
            mHandler.sendEmptyMessage(MSG_LOADING);
        }
    }

    protected abstract void loadData();

    protected void onDataLoaded(){
        mHandler.sendEmptyMessage(MSG_LOADED);
    }

    private void dataLoaded() {
        if (mInLoading) {
            mInLoading = false;
            mPullLayout.setEnableStopInActionView(false);
            mPullLayout.hideActionView();


            if (mPullLayout.isPullOut()) {
                ivRefresh.clearAnimation();
            } else {
                //tvRefresh.setText(R.string.pull_to_refresh);
            }
        }
    }

    @Override
    public void onPullOut() {
        if (!mInLoading) {
            //tvRefresh.setText(R.string.release_to_refresh);
            ivRefresh.clearAnimation();
        }
    }

    @Override
    public void onPullIn() {
        if (!mInLoading) {
            tvRefresh.setText(R.string.pull_to_refresh);
            ivRefresh.clearAnimation();
            OWAnimationUtils.startRotation(ivRefresh);
        }
    }

    @Override
    public void onSnapToTop() {
        startLoading();
    }

    @Override
    public void onShow() {

    }

    @Override
    public void onHide() {

    }
}
