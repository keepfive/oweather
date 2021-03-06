package com.massivekinetics.ow.services.utils;

import android.widget.Toast;
import com.massivekinetics.ow.ui.activities.BaseActivity;
import com.massivekinetics.ow.ui.interfaces.Notifier;

/**
 * Created with IntelliJ IDEA.
 * User: bovy
 * Date: 2/14/13
 * Time: 9:09 AM
 * To change this template use File | Settings | File Templates.
 */
public class OWNotifier implements Notifier {
    @Override
    public void alert(String message, int duration) {
        Toast.makeText(BaseActivity.getActiveInstance(), message, duration).show();
    }
}
