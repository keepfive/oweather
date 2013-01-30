package com.massivekinetics.ow.activities;

import java.io.IOException;

import com.massivekinetics.ow.R;
import com.massivekinetics.ow.application.OWApplication;
import com.massivekinetics.ow.location.OWLocationManager;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class StartActivity extends OWActivity implements LocationListener {
	OWLocationManager locationManager;
	Handler handler = new Handler();
	
	TextView text;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start);
		text = (TextView)findViewById(R.id.text);
		
		locationManager = OWApplication.context.getLocationManager();
	}

	@Override
	protected void onPause() {
		super.onPause();
		locationManager.unregisterFromUpdates(this);
		
		locationManager.getLastKnownLocation();
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		locationManager.registerForUpdates(this);
	}

	@Override
	public void onLocationChanged(Location location) {
		String text = "Location changed : ";
		if(location!=null){
			try {
				String g =	locationManager.getCityName(location);
				if(g!=null)
					text+=g;
			} catch (IOException e) {}
		}
		
		final String val = new String(text);
		
		handler.post(new Runnable() {
			
			@Override
			public void run() {
				StartActivity.this.text.setText(val);
			}
		});
		
	}

	@Override
	public void onProviderDisabled(String provider) {}

	@Override
	public void onProviderEnabled(String provider) {}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {}

	

}
