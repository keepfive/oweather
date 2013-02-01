package com.massivekinetics.ow.activities;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

public class OWActivity extends Activity {
    public static OWActivity current;
    protected Typeface fontThin;
    protected Typeface fontItalic;
    protected Handler uiHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        initFonts();
    }

    @Override
    protected void onResume() {
        super.onResume();
        current = this;
    }

    void initFonts() {
        fontThin = Typeface.createFromAsset(getAssets(), "titilliumthin.ttf");
        fontItalic = Typeface.createFromAsset(getAssets(), "titiliumitalic.ttf");
    }
}
