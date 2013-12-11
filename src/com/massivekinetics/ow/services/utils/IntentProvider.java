package com.massivekinetics.ow.services.utils;

import android.app.Activity;
import android.content.Intent;

/**
 * Created with IntelliJ IDEA.
 * User: bovy
 * Date: 4/26/13
 * Time: 12:38 PM
 */
public class IntentProvider {

    public Intent getIntent(Activity from, Class<?> to) {
        return new Intent(from, to);
    }

}
