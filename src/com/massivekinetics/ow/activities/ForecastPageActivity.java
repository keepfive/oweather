package com.massivekinetics.ow.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.massivekinetics.ow.R;
import com.massivekinetics.ow.utils.TypefaceUtil;

public class ForecastPageActivity extends OWActivity {
	TextView text;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forecast_page);
		Typeface face=Typeface.createFromAsset(getAssets(), "titilliumthin.ttf"); 
		
		text = (TextView)findViewById(R.id.tvToday);
		TypefaceUtil.setTextViewTypeface(text, face);
		
	}

}
