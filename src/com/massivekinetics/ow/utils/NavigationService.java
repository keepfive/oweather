package com.massivekinetics.ow.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import com.massivekinetics.ow.application.Application;

/**
 * Created with IntelliJ IDEA.
 * User: bovy
 * Date: 4/26/13
 * Time: 12:37 PM
 */
public class NavigationService {
    private static IntentProvider intentProvider = new IntentProvider();

    public static void setIntentProvider(IntentProvider intentProvider) {
        NavigationService.intentProvider = intentProvider;
    }

    /**
     * Navigates to the specified activity
     *
     * @param from the activity navigating from
     * @param to   the class of the activity navigating to
     * @return boolean indicating if navigation succeeded
     */
    public static boolean navigate(Activity from, Class<?> to) {
        return navigate(from, new Class<?>[]{to}, new int[]{});
    }

    /**
     * Navigates to the specified activity
     *
     * @param from  the activity navigating from
     * @param to    the class of the activity navigating to
     * @param flags list of flags to add to the intent
     * @return boolean indicating if navigation succeeded
     */
    public static boolean navigate(Activity from, Class<?> to, int... flags) {
        return navigate(from, new Class<?>[]{to}, null, flags);
    }

    /**
     * Navigates to the specified activity
     *
     * @param from  the activity navigating from
     * @param to    the class of the activity navigating to
     * @param bundle info to add to the intent
     * @return boolean indicating if navigation succeeded
     */
    public static boolean navigate(Activity from, Class<?> to, Bundle bundle) {
        return navigate(from, new Class<?>[]{to}, bundle, new int[]{});
    }

    /**
     * Navigates to the specified activity
     *
     * @param from  the activity navigating from
     * @param to    the class of the activity navigating to
     * @param flags list of flags to add to the intent
     * @return boolean indicating if navigation succeeded
     */
    public static boolean navigate(Activity from, Class<?> to, Bundle bundle, int... flags) {
        return navigate(from, new Class<?>[]{to}, bundle, flags);
    }

    /**
     * Navigates to the first available activity activity
     *
     * @param from the activity navigating from
     * @param to   the target activities in order of preference. If first one is unavailable next one is used etc.
     * @return boolean indicating if navigation succeeded
     */
    public static boolean navigate(Activity from, Class<?>... to) {
        return navigate(from, to, new int[]{});
    }

    /**
     * Navigates to the first available activity activity
     *
     * @param from  the activity navigating from
     * @param to    the target activities in order of preference. If first one is unavailable next one is used etc.
     * @param flags list of flags to add to the intent
     * @return boolean indicating if navigation succeeded
     */
    public static boolean navigate(Activity from, Class<?>[] to, int... flags) {
        return navigate(from, to, null, flags);
    }

    /**
     * Navigates to the first available activity activity
     *
     * @param from   the activity navigating from
     * @param to     the target activities in order of preference. If first one is unavailable next one is used etc.
     * @param bundle info to add to the intent
     * @param flags  list of flags to add to the intent
     * @return boolean indicating if navigation succeeded
     */
    public static boolean navigate(Activity from, Class<?>[] to, Bundle bundle, int... flags) {
        for (Class<?> activity : to) {
            Intent intent = intentProvider.getIntent(from, activity);

            Intent fromIntent = from.getIntent();
            if (fromIntent != null)
                intent.setData(fromIntent.getData());

            if (bundle != null) {
                intent.putExtras(bundle);
            }

            for (int flag : flags) {
                intent.setFlags(flag);
            }

            if (from.getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
                from.startActivity(intent);
                return true;
            }
        }
        return false;
    }

    public static final void startActivityOrProposeGooglePlay(Intent intent, String packageOnGooglePlay){
        Context context = Application.getInstance();
        PackageManager pm = context.getPackageManager();
        ComponentName cn = intent.resolveActivity(pm);
        if(cn == null){
            Uri marketUri = Uri.parse(packageOnGooglePlay);
            Intent marketIntent = new Intent(Intent.ACTION_VIEW).setData(marketUri);
            if(marketIntent.resolveActivity(pm) != null)
                context.startActivity(marketIntent);
            else
                Log.e("startActivityOrProposeGooglePlay", "Market client not available");
        } else
            context.startActivity(intent);
    }


    public static final Intent alarmIntent(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent alarmClockIntent = new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER);

        // Verify clock implementation
        String clockImpls[][] = {
                {"HTC Alarm ClockDT", "com.htc.android.worldclock", "com.htc.android.worldclock.WorldClockTabControl" },
                {"Standart Alarm ClockDT", "com.android.deskclock", "com.android.deskclock.AlarmClock"},
                {"Froyo Nexus Alarm ClockDT", "com.google.android.deskclock", "com.android.deskclock.DeskClock"},
                {"Moto Blur Alarm ClockDT", "com.motorola.blur.alarmclock",  "com.motorola.blur.alarmclock.AlarmClock"},
                {"Samsung Galaxy S", "com.sec.android.app.clockpackage","com.sec.android.app.clockpackage.ClockPackage"}
        };

        boolean foundClockImpl = false;

        for(int i=0; i<clockImpls.length; i++) {
            String packageName = clockImpls[i][1];
            String className = clockImpls[i][2];
            try {
                ComponentName cn = new ComponentName(packageName, className);
                packageManager.getActivityInfo(cn, PackageManager.GET_META_DATA);
                alarmClockIntent.setComponent(cn);
                foundClockImpl = true;
            } catch (PackageManager.NameNotFoundException nf) {
            }
        }
        if (foundClockImpl)
            return alarmClockIntent;
        else
            return null;
    }
}
