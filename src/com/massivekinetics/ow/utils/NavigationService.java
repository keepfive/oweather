package com.massivekinetics.ow.utils;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

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
}
