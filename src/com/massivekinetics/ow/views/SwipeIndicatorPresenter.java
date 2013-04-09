package com.massivekinetics.ow.views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.massivekinetics.ow.R;
import com.massivekinetics.ow.application.OWApplication;

/**
 * Created with IntelliJ IDEA.
 * User: bovy
 * Date: 4/9/13
 * Time: 10:42 PM
 */
public class SwipeIndicatorPresenter {
    private int indicatorCount;
    private int currentActivePosition;
    private ViewGroup layout;
    private int activeInicatorDrawableId, passiveIndicatorDrawableId;

    public SwipeIndicatorPresenter(int indicatorCount) {
        this.indicatorCount = indicatorCount;
        activeInicatorDrawableId = R.drawable.indicator_active;
        passiveIndicatorDrawableId = R.drawable.indicator_passive;
        inflateLayout();
        setCurrentActivePosition(0);
    }

    private void inflateLayout() {
        LayoutInflater inflater = LayoutInflater.from(OWApplication.context);
        layout = (ViewGroup) inflater.inflate(R.layout.indicator_layout, null);
        for (int i = 0; i < indicatorCount; i++) {
            ImageView imageView = (ImageView) inflater.inflate(R.layout.image_view, null);
            imageView.setImageResource(passiveIndicatorDrawableId);
            layout.addView(imageView);
        }
    }

    public View getView() {
        return layout;
    }

    public void setCurrentActivePosition(int newCurrentActivePosition) {
        if (newCurrentActivePosition < 0 || newCurrentActivePosition >= indicatorCount)
            return;

        ((ImageView) layout.getChildAt(currentActivePosition)).setImageResource(passiveIndicatorDrawableId);
        ((ImageView) layout.getChildAt(newCurrentActivePosition)).setImageResource(activeInicatorDrawableId);
        this.currentActivePosition = newCurrentActivePosition;
        //layout.requestLayout();
    }
}
