package com.massivekinetics.ow.services.utils;

import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TypefaceUtil {

    public static void setViewGroupTypeface(ViewGroup container, Typeface typeface) {
        final int children = container.getChildCount();

        for (int i = 0; i < children; i++) {
            View child = container.getChildAt(i);

            if (child instanceof TextView) {
                setTextViewTypeface((TextView) child, typeface);
            } else if (child instanceof ViewGroup) {
                setViewGroupTypeface((ViewGroup) child, typeface);
            }
        }
    }

    public static void setTextViewTypeface(TextView textView, Typeface typeface) {
        textView.setTypeface(typeface);
    }

}