package com.massivekinetics.ow.domain.adapters;

import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.massivekinetics.ow.R;
import com.massivekinetics.ow.ui.activities.BaseActivity;
import com.massivekinetics.ow.domain.model.WeatherModel;
import com.massivekinetics.ow.domain.states.WeatherState;
import com.massivekinetics.ow.services.utils.ResourcesCodeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: bovy
 * Date: 2/19/13
 * Time: 3:46 PM
 * To change this template use File | Settings | File Templates.
 */

public class WeatherPagerAdapter extends PagerAdapter {

    public final static String TAG = "WeatherPagerAdapter";
    private BaseActivity parentActivity;
    private LayoutInflater inflater;
    private List<WeatherModel> items;
    private Handler adapterHandler = new Handler();

    public WeatherPagerAdapter(BaseActivity parentActivity, List<WeatherModel> items){
        this.items = new ArrayList<WeatherModel>(items);
        this.parentActivity = parentActivity;
        this.inflater = LayoutInflater.from(parentActivity);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View layout = inflateNewLayout();
        updateView(layout, position);
        container.addView(layout);
        return layout;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


    private View inflateNewLayout() {
        View layout = inflater.inflate(R.layout.pager_item, null);
        return layout;
    }

    private void updateView(final View layout, int position) {
        WeatherState weatherState = items.get(position).getState();
        int resource = ResourcesCodeUtils.getWeatherImageResource(weatherState);
        ((ImageView)layout).setImageResource(resource);
    }
}
