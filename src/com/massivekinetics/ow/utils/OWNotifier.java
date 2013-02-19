package com.massivekinetics.ow.utils;

import android.widget.Toast;
import com.massivekinetics.ow.activities.OWActivity;
import com.massivekinetics.ow.interfaces.Notifier;

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
        Toast.makeText(OWActivity.current, message, duration).show();
    }
}
