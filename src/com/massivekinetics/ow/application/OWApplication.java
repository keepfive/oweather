package com.massivekinetics.ow.application;

import android.app.Application;
import android.graphics.Typeface;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Config;
import android.util.DisplayMetrics;
import com.massivekinetics.ow.data.manager.ConfigManager;
import com.massivekinetics.ow.data.manager.DataManager;
import com.massivekinetics.ow.data.manager.OWConfigManager;
import com.massivekinetics.ow.data.manager.WeatherDataManager;

public class OWApplication extends Application {
    private static OWApplication context;
    private DataManager dataManager;
    private ConfigManager configManager;

    private DisplayMetrics displayMetrics;

    private Typeface fontThin;
    private Typeface fontItalic;


    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        configManager = new OWConfigManager();
        initFonts();
    }

    public String getDeviceId() {

            TelephonyManager mTelephonyMgr = (TelephonyManager)getSystemService(android.app.Service.TELEPHONY_SERVICE);
            //Log.d(TAG, "DeviceId: " + mTelephonyMgr.getDeviceId());

            if (mTelephonyMgr.getDeviceId() != null)
                return mTelephonyMgr.getDeviceId();

			/*
			 * Taken from http://www.pocketmagic.net/?p=1662 Pseudo-Unique ID, that works on all Android devices Some
			 * devices don't have a phone (eg. Tablets) or for some reason you don't want to include the
			 * READ_PHONE_STATE permission. You can still read details like ROM Version, Manufacturer name, CPU type,
			 * and other hardware details, that will be well suited if you want to use the ID for a serial key check, or
			 * other general purposes. The ID computed in this way won't be unique: it is possible to find two devices
			 * with the same ID (based on the same hardware and rom image) but the chances in real world applications
			 * are negligible. For this purpose Build class is used:
			 */

            String pseudoDeviceIMEI = "35" + // we make this look like a valid IMEI
                    Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +
                    Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +
                    Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +
                    Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +
                    Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +
                    Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +
                    Build.USER.length() % 10; // 13 digits

            return pseudoDeviceIMEI;

    }

    public static OWApplication getInstance(){
        return context;
    }

    public DataManager getDataManager() {
        return WeatherDataManager.getInstance();
    }

    public ConfigManager getConfigManager(){
        return configManager;
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
        /*Typeface fontThin = Typeface.createFromAsset(getAssets(), "northern_light.otf");
        Typeface fontItalic = Typeface.createFromAsset(getAssets(), "northern_light.otf");
        */
        setFontThin(fontThin);
        setFontItalic(fontItalic);
    }

}
