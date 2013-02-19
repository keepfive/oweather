package com.massivekinetics.ow.activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.TextView;
import com.massivekinetics.ow.application.OWApplication;
import com.massivekinetics.ow.data.manager.ConfigManager;
import com.massivekinetics.ow.data.manager.OWConfigManager;
import com.massivekinetics.ow.interfaces.Notifier;
import com.massivekinetics.ow.utils.OWNotifier;

public abstract class OWActivity extends Activity {
    public static OWActivity current;
    protected Handler uiHandler = new Handler();
    protected Notifier notifier = new OWNotifier();
    protected ConfigManager configManager = new OWConfigManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
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

    protected abstract void initViews();
    protected abstract void initListeners();

}
