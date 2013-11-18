package com.massivekinetics.ow.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import com.massivekinetics.ow.R;
import com.massivekinetics.ow.data.Autocompleter;
import com.massivekinetics.ow.data.adapters.PlacesAutoCompleteAdapter;
import com.massivekinetics.ow.data.model.Prediction;

/**
 * Created with IntelliJ IDEA.
 * User: bovy
 * Date: 5/10/13
 * Time: 1:17 AM
 */
public class TestActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    AutoCompleteTextView autoCompView;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);


        autoCompView = (AutoCompleteTextView) findViewById(R.id.autocomplete);
        autoCompView.setAdapter(new PlacesAutoCompleteAdapter(this, android.R.layout.simple_list_item_1));
        autoCompView.setOnItemClickListener(this);

    }




    @Override
    protected void initViews() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void initListeners() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final Prediction prediction = (Prediction) parent.getItemAtPosition(position);
        autoCompView.setText(prediction.getDescription());
        new Thread(){
            @Override
            public void run(){
                new Autocompleter().getPlaceCoordinates(prediction.getReference());
            }
        }.start();
    }
}