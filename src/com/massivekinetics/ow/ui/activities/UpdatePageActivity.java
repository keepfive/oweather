package com.massivekinetics.ow.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import com.massivekinetics.ow.R;
import com.massivekinetics.ow.application.AppLocator;
import com.massivekinetics.ow.application.Font;
import com.massivekinetics.ow.application.IDataManager;
import com.massivekinetics.ow.domain.model.WeatherForecast;
import com.massivekinetics.ow.ui.interfaces.LoadingListener;
import com.massivekinetics.ow.services.utils.NavigationService;
import com.massivekinetics.ow.services.utils.OWAnimationUtils;

/**
 * Created with IntelliJ IDEA.
 * User: bovy
 * Date: 4/30/13
 * Time: 1:31 PM
 */
public class UpdatePageActivity extends BaseActivity {
    ImageView image;
    LoadingListener<WeatherForecast> listener = new LoadingListener<WeatherForecast>() {
        @Override
        public void onLoaded(WeatherForecast result) {
            IDataManager dataManager = AppLocator.resolve(IDataManager.class);
            if (!result.isSuccessed() && !dataManager.hasActualForecast()) {
                NavigationService.navigate(UpdatePageActivity.this, ErrorActivity.class);
            } else {
                NavigationService.navigate(UpdatePageActivity.this, ForecastPageActivity.class, null, Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            }
            finish();
        }

        @Override
        public void notifyStart() {
            OWAnimationUtils.startRotation(image);
        }

        @Override
        public void notifyStop() {

        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.update_page);
        initViews();

        IDataManager dataManager = AppLocator.resolve(IDataManager.class);
        dataManager.runUpdate(listener);
    }

    @Override
    protected void initViews() {
        image = (ImageView) findViewById(R.id.image);
        ((TextView) findViewById(R.id.updating)).setTypeface(Font.DEFAULT.get());
    }

    @Override
    protected void initListeners() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}