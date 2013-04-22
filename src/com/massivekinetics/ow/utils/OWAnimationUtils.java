package com.massivekinetics.ow.utils;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import com.massivekinetics.ow.R;

/**
 * Created with IntelliJ IDEA.
 * User: bovy
 * Date: 2/20/13
 * Time: 12:01 AM
 * To change this template use File | Settings | File Templates.
 */
public class OWAnimationUtils {
    private static Object lock = new Object();

    public static void stopAnimation(View view) {
        view.getAnimation().cancel();
    }

    public static void startRotation(View view) {
        Animation rotation = AnimationUtils.loadAnimation(view.getContext(), R.anim.rotation);
        view.startAnimation(rotation);
    }

    private static void internalRotate(final View view, final float toDegrees) {
        synchronized (lock) {
            float fromDegrees = (view.getTag() == null) ? 0f : (Float) view.getTag();
            RotateAnimation rotateAnimation = new RotateAnimation(fromDegrees, toDegrees, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnimation.setDuration(1200);
            rotateAnimation.setFillAfter(true);
            view.setTag(toDegrees);
            view.startAnimation(rotateAnimation);
        }
    }

    public static void rotate(final View view, final float toDegrees) {
        RotateAnimation current = (RotateAnimation) view.getAnimation();

        if (current != null && !current.hasEnded())
            view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    rotate(view, toDegrees);
                }
            }, 200);
        else
            view.post(new AnimationWorker(view, toDegrees));
    }

    static class AnimationWorker implements Runnable {
        private View view;
        private float toDegrees;


        public AnimationWorker(View view, float toDegrees) {
            this.view = view;
            this.toDegrees = toDegrees;
        }

        @Override
        public void run() {
            internalRotate(view, toDegrees);
        }
    }

}
