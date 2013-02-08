package com.massivekinetics.ow.activities;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Window;
import com.massivekinetics.ow.application.OWApplication;

public class OWActivity extends Activity {
    public static OWActivity current;
    protected Typeface fontThin;
    protected Typeface fontItalic;
    protected Handler uiHandler = new Handler();

    private static boolean isInitialized = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        initApllication();
        initFonts();
    }

    @Override
    protected void onResume() {
        super.onResume();
        current = this;
    }

    protected void runOnMainThread(Runnable task){
        uiHandler.post(task);
    }

    void initFonts() {
        fontThin = Typeface.createFromAsset(getAssets(), "titilliumthin.ttf");
        fontItalic = Typeface.createFromAsset(getAssets(), "titiliumitalic.ttf");
    }

    void initApllication(){
        if(!isInitialized){
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            ((OWApplication)getApplication()).setDisplayMetrics(displayMetrics);
            isInitialized = true;
        }
    }
}
