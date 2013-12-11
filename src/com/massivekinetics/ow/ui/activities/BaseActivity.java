package com.massivekinetics.ow.ui.activities;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.analytics.tracking.android.GAServiceManager;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;
import com.massivekinetics.ow.R;
import com.massivekinetics.ow.application.*;
import com.massivekinetics.ow.application.location.IGeoLocator;
import com.massivekinetics.ow.ui.interfaces.Notifier;
import com.massivekinetics.ow.application.location.GeoLocator;
import com.massivekinetics.ow.services.utils.OWNotifier;

public abstract class BaseActivity extends Activity {
    private static BaseActivity activeInstance;
    protected Handler uiHandler = new Handler();
    protected Notifier notifier = new OWNotifier();
    protected IGeoLocator locationManager = new GeoLocator();

    private Tracker mGaTracker;
    private GoogleAnalytics mGaInstance;
    private static boolean isGADispatchSet = false;

    protected IConfiguration mConfiguration = AppLocator.resolve(IConfiguration.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        initGoogleAnalytics();
    }

    private void initGoogleAnalytics() {
        mGaInstance = GoogleAnalytics.getInstance(this);
        mGaTracker = mGaInstance.getTracker(getString(R.string.ga_trackingId));
        if (!isGADispatchSet) {
            GAServiceManager.getInstance().setDispatchPeriod(30);
            isGADispatchSet = true;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        String screenName = this.getClass().getSimpleName();
        mGaTracker.sendView(screenName);
    }

    protected Application getOWApplication() {
        return (Application) getApplication();
    }

    @Override
    protected void onResume() {
        super.onResume();
        activeInstance = this;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    protected void runOnMainThread(Runnable task) {
        uiHandler.post(task);
    }

    public static BaseActivity getActiveInstance() {
        return activeInstance;
    }

    protected void setFont(TextView... views) {
        setFont(Font.DEFAULT, views);
    }

    protected void setFont(ViewGroup container) {
        setFont(Font.DEFAULT, container);
    }

    protected void setFont(Font font, TextView... views) {
        for (TextView textView : views)
            textView.setTypeface(font.get());
    }

    protected void setFont(Font font, ViewGroup container) {
        for (int position = 0; position < container.getChildCount(); position++) {
            View child = container.getChildAt(position);
            if (child instanceof TextView) {
                ((TextView) child).setTypeface(font.get());
            } else if (child instanceof ViewGroup)
                setFont(font, (ViewGroup) child);
        }
    }

    protected void setTouchDelegates(final View parent, final View viewToExpand, final int sizeToExpand) {
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
            }

            ;
        });
    }

    protected abstract void initViews();

    protected abstract void initListeners();

}
