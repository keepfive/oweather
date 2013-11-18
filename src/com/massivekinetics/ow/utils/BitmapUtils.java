package com.massivekinetics.ow.utils;

import android.content.res.Resources;
import android.graphics.*;
import android.util.TypedValue;
import com.massivekinetics.ow.R;
import com.massivekinetics.ow.application.Application;

/**
 * Created with IntelliJ IDEA.
 * User: bovy
 * Date: 2/8/13
 * Time: 2:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class BitmapUtils {
    public static Bitmap doGreyscale(Bitmap src) {
        // constant factors
        final double GS_RED = 0.299;
        final double GS_GREEN = 0.587;
        final double GS_BLUE = 0.114;

        // create output bitmap
        Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
        // pixel information
        int A, R, G, B;
        int pixel;

        // get image size
        int width = src.getWidth();
        int height = src.getHeight();

        // scan through every single pixel
        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) {
                // get one pixel color
                pixel = src.getPixel(x, y);
                // retrieve color of all channels
                A = Color.alpha(pixel);
                R = Color.red(pixel);
                G = Color.green(pixel);
                B = Color.blue(pixel);
                // take conversion up to one single value
                R = G = B = (int)(GS_RED * R + GS_GREEN * G + GS_BLUE * B);
                // set new pixel color to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }
        // return final image
        return bmOut;
    }

    public static Bitmap doGreyscale(int resourceId) {
        Bitmap src = BitmapFactory.decodeResource(Application.getInstance().getResources(), R.drawable.settings);
        // constant factors
        final double GS_RED = 0.299;
        final double GS_GREEN = 0.587;
        final double GS_BLUE = 0.114;

        // create output bitmap
        Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
        // pixel information
        int A, R, G, B;
        int pixel;

        // get image size
        int width = src.getWidth();
        int height = src.getHeight();

        // scan through every single pixel
        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) {
                // get one pixel color
                pixel = src.getPixel(x, y);
                // retrieve color of all channels
                A = Color.alpha(pixel);
                R = Color.red(pixel);
                G = Color.green(pixel);
                B = Color.blue(pixel);
                // take conversion up to one single value
                R = G = B = (int)(GS_RED * R + GS_GREEN * G + GS_BLUE * B);
                // set new pixel color to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }

        src.recycle();
        // return final image
        return bmOut;
    }

    public static Bitmap getStringAsBitmap(String text, int textSize, int xOffset, int yOffset, int trimSize)  {
        int width = dipToPx(155);
        int height = dipToPx(30);
        if(trimSize != -1 && text.length() >= (trimSize+1)){
            text = text.substring(0, trimSize-1) + "...";
        }
        Bitmap myBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas myCanvas = new Canvas(myBitmap);
        Paint paint = new Paint();
        Typeface typeface = Application.getInstance().getFontThin();
        paint.setAntiAlias(true);
        paint.setSubpixelText(true);
        paint.setTypeface(typeface);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setTextSize(textSize);
        paint.setAntiAlias(true);
        paint.setSubpixelText(true);
        paint.setTextAlign(Paint.Align.LEFT);
        myCanvas.drawText(text, xOffset, yOffset, paint);
        return myBitmap;
    }

    public static Bitmap getStringAsBitmap2(String text, int textSize, int xOffset, int yOffset)  {
        int width = dipToPx(95);
        int height = dipToPx(70);
        Bitmap myBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas myCanvas = new Canvas(myBitmap);
        Paint paint = new Paint();
        Typeface typeface = Application.getInstance().getFontThin();
        paint.setAntiAlias(true);
        paint.setSubpixelText(true);
        paint.setTypeface(typeface);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setTextSize(textSize);
        paint.setAntiAlias(true);
        paint.setSubpixelText(true);
        paint.setTextAlign(Paint.Align.LEFT);
        myCanvas.drawText(text, xOffset, yOffset, paint);
        return myBitmap;
    }

    public static Bitmap getStringAsBitmapCenter
            (String text, int textSize, int xOffset, int yOffset)  {
        int width = dipToPx(80);
        int height = dipToPx(70);
        Bitmap myBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas myCanvas = new Canvas(myBitmap);
        Paint paint = new Paint();
        Typeface typeface = Application.getInstance().getFontThin();
        paint.setAntiAlias(true);
        paint.setSubpixelText(true);
        paint.setTypeface(typeface);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setTextSize(textSize);
        paint.setAntiAlias(true);
        paint.setSubpixelText(true);
        paint.setTextAlign(Paint.Align.LEFT);
        myCanvas.drawText(text, xOffset, yOffset, paint);
        return myBitmap;
    }

    public static Bitmap getStringAsBitmapWithSize(String text, int textSize, int xOffset, int yOffset, int trimSize, int widhtDp, int heightDp)  {
        int width = dipToPx(widhtDp);
        int height = dipToPx(heightDp);
        if(trimSize != -1 && text.length() >= (trimSize+1)){
            text = text.substring(0, trimSize-1) + "...";
        }
        Bitmap myBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas myCanvas = new Canvas(myBitmap);
        Paint paint = new Paint();
        Typeface typeface = Application.getInstance().getFontThin();
        paint.setAntiAlias(true);
        paint.setSubpixelText(true);
        paint.setTypeface(typeface);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setTextSize(textSize);
        paint.setAntiAlias(true);
        paint.setSubpixelText(true);
        paint.setTextAlign(Paint.Align.LEFT);
        myCanvas.drawText(text, xOffset, yOffset, paint);
        return myBitmap;
    }

    public static final int dipToPx(int dp) {

        Resources r = Application.getInstance().getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                r.getDisplayMetrics());

    }

    public static final int spToPx(int sp) {
        Resources r = Application.getInstance().getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                r.getDisplayMetrics());
    }

}
