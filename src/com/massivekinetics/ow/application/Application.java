package com.massivekinetics.ow.application;

import android.graphics.Typeface;
import android.os.Build;
import android.util.DisplayMetrics;
import com.massivekinetics.ow.data.manager.DataManager;
import com.massivekinetics.ow.data.manager.WeatherDataManager;

public class Application extends android.app.Application {
    private static Application context;
    private IConfiguration IConfiguration;

    private DisplayMetrics displayMetrics;

    private Typeface fontThin;
    private Typeface fontItalic;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        IConfiguration = new Configuration();
        initFonts();
    }

    public static Application getInstance(){
        return context;
    }

    public DataManager getDataManager() {
        return WeatherDataManager.getInstance();
    }

    public IConfiguration getConfiguration(){
        return IConfiguration;
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
        Typeface fontThin = Typeface.createFromAsset(getAssets(), "lintel_light.otf");
        Typeface fontItalic = Typeface.createFromAsset(getAssets(), "titiliumitalic.ttf");
        /*Typeface fontThin = Typeface.createFromAsset(getAssets(), "northern_light.otf");
        Typeface fontItalic = Typeface.createFromAsset(getAssets(), "northern_light.otf");
        */
        setFontThin(fontThin);
        setFontItalic(fontItalic);
    }


}
