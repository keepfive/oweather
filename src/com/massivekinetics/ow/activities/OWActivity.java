package com.massivekinetics.ow.activities;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.GAServiceManager;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;
import com.massivekinetics.ow.R;
import com.massivekinetics.ow.application.OWApplication;
import com.massivekinetics.ow.data.manager.ConfigManager;
import com.massivekinetics.ow.data.manager.OWConfigManager;
import com.massivekinetics.ow.interfaces.Notifier;
import com.massivekinetics.ow.location.OWLocationManager;
import com.massivekinetics.ow.utils.OWNotifier;

public abstract class OWActivity extends Activity {
    public static OWActivity current;
    protected Handler uiHandler = new Handler();
    protected Notifier notifier = new OWNotifier();
    protected ConfigManager configManager = new OWConfigManager();
    protected OWLocationManager locationManager = new OWLocationManager();
    private OWApplication application;

    protected boolean isTablet = false;

    private Tracker mGaTracker;
    private GoogleAnalytics mGaInstance;
    private static boolean isGADispatchSet = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        mGaInstance = GoogleAnalytics.getInstance(this);
        mGaTracker = mGaInstance.getTracker(getString(R.string.ga_trackingId));
        if(!isGADispatchSet){
            GAServiceManager.getInstance().setDispatchPeriod(30);
            isGADispatchSet = true;
        }
        application = (OWApplication)getApplication();
        isTablet = getResources().getBoolean(R.bool.isTablet);
    }

    @Override
    protected void onStart(){
        super.onStart();
        String screenName = this.getClass().getSimpleName();
        mGaTracker.sendView(screenName);
    }

    protected OWApplication getOWApplication(){
        return application;
    }

    @Override
    protected void onResume() {
        super.onResume();
        current = this;
    }

    @Override
    protected void onStop(){
        super.onStop();
    }

    protected void runOnMainThread(Runnable task){
        uiHandler.post(task);
    }

    public boolean isTablet(){
        return isTablet;
    }

    protected void setFont(TextView... views){
        for(TextView textView:views)
            textView.setTypeface(((OWApplication)getApplication()).getFontThin());
    }

    protected void setFont(ViewGroup container){
        for(int position = 0;position<container.getChildCount();position++){
            View child = container.getChildAt(position);
            if(child instanceof TextView){
                 ((TextView)child).setTypeface(((OWApplication)getApplication()).getFontThin());
            } else if(child instanceof ViewGroup)
                setFont((ViewGroup)child);
        }
    }

    protected void setTouchDelegates(final View parent, final View viewToExpand, final int sizeToExpand){
        parent.post(new Runnable() {
            public void run() {
                // Post in the parent's message queue to make sure the parent
                // lays out its children before we call getHitRect()
                Rect delegateArea = new Rect();
                View delegate = viewToExpand;
                delegate.getHitRect(delegateArea);
                delegateArea.top -= sizeToExpand;
                delegateArea.bottom += sizeToExpand;
                delegateArea.left -= sizeToExpand;
                delegateArea.right += sizeToExpand;
                TouchDelegate expandedArea = new TouchDelegate(delegateArea, delegate);
                // give the delegate to an ancestor of the view we're
                // delegating the area to
                parent.setTouchDelegate(expandedArea);

            };
        });
    }

    protected abstract void initViews();
    protected abstract void initListeners();

}
