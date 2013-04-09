package com.massivekinetics.ow.utils;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.massivekinetics.ow.R;

/**
 * Created with IntelliJ IDEA.
 * User: bovy
 * Date: 2/20/13
 * Time: 12:01 AM
 * To change this template use File | Settings | File Templates.
 */
public class OWAnimationUtils {
    public static void stopAnimation(View view) {
        view.getAnimation().cancel();
    }

   /* public static void startRotation(View view) {
        RotateAnimation rAnim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rAnim.setRepeatCount(Animation.INFINITE);
        rAnim.setInterpolator(new LinearInterpolator());
        rAnim.setDuration(1500);
        view.startAnimation(rAnim);
    } */

    public static void startRotation(View view) {
       Animation rotation = AnimationUtils.loadAnimation(view.getContext(), R.anim.rotation);
        view.startAnimation(rotation);
    }
}
