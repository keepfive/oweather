package com.massivekinetics.ow.activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        application = (OWApplication)getApplication();
    }

    protected OWApplication getOWApplication(){
        return application;
    }

    @Override
    protected void onResume() {
        super.onResume();
        current = this;
    }

    protected void runOnMainThread(Runnable task){
        uiHandler.post(task);
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

    protected abstract void initViews();
    protected abstract void initListeners();

}
