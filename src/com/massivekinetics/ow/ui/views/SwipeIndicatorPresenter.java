package com.massivekinetics.ow.ui.views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.massivekinetics.ow.R;
import com.massivekinetics.ow.application.Application;

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

    public SwipeIndicatorPresenter(ViewGroup layout, int indicatorCount) {
        this.indicatorCount = indicatorCount;
        activeInicatorDrawableId = R.drawable.indicator_active;
        passiveIndicatorDrawableId = R.drawable.indicator_passive;
        this.layout = layout;

        inflateLayout();
        setCurrentActivePosition(0);
    }

    private void inflateLayout() {
        LayoutInflater inflater = LayoutInflater.from(Application.getInstance());
        layout.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 5, 10, 5);
        for (int i = 0; i < indicatorCount; i++) {
            ImageView imageView = (ImageView) inflater.inflate(R.layout.image_view, null);
            imageView.setImageResource(passiveIndicatorDrawableId);

            layout.addView(imageView, params);
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
    }
}
