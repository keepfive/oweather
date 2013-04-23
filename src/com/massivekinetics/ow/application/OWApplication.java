package com.massivekinetics.ow.application;

import android.app.Application;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import com.massivekinetics.ow.data.manager.DataManager;
import com.massivekinetics.ow.data.manager.WeatherDataManager;

public class OWApplication extends Application {
    public static OWApplication context;
    private DataManager dataManager;

    private DisplayMetrics displayMetrics;

    private Typeface fontThin;
    private Typeface fontItalic;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        initFonts();
    }

    public DataManager getDataManager() {
        return WeatherDataManager.getInstance();
    }

    public DisplayMetrics getDisplayMetrics() {
        return displayMetrics;
    }

    public void setDisplayMetrics(DisplayMetrics displayMetrics) {
        this.displayMetrics = displayMetrics;
    }

    public Typeface getFontThin() {
        if(fontThin == null)
            initFonts();
        return fontThin;
    }

    public void setFontThin(Typeface fontThin) {
        this.fontThin = fontThin;
    }

    public Typeface getFontItalic() {
        if(fontItalic == null)
            initFonts();
        return fontItalic;
    }

    public void setFontItalic(Typeface fontItalic) {
        this.fontItalic = fontItalic;
    }

    private void initFonts(){
        Typeface fontThin = Typeface.createFromAsset(getAssets(), "titilliumthin.ttf");
        Typeface fontItalic = Typeface.createFromAsset(getAssets(), "titiliumitalic.ttf");
        setFontThin(fontThin);
        setFontItalic(fontItalic);
    }

}
