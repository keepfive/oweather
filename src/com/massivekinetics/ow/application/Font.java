package com.massivekinetics.ow.application;

import android.graphics.Typeface;
import com.massivekinetics.ow.R;

/**
 * Created with IntelliJ IDEA.
 * User: bovy
 * Date: 11/15/13
 * Time: 5:18 PM
 * To change this template use File | Settings | File Templates.
 */
public enum Font {
    DEFAULT(R.string.default_font),
    NUMERIC(R.string.numeric_font);

    Font(int fontResId) {
        mFontFileName = Application.getInstance().getResources().getString(fontResId);
    }

    public Typeface get() {
        if (instance == null) {
            synchronized (Font.class) {
                if (instance == null)
                    instance = Typeface.createFromAsset(Application.getInstance().getAssets(), mFontFileName);
            }
        }

        return instance;
    }

    private String mFontFileName;
    private Typeface instance;
}
