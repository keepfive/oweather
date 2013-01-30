package com.massivekinetics.ow.application;

import com.massivekinetics.ow.location.OWLocationManager;

import android.app.Application;
import android.content.Context;

public class OWApplication extends Application {
	public static OWApplication context;
	private OWLocationManager locationManager;
	
	@Override
	public void onCreate() {
		super.onCreate();
		context = this;
		
		locationManager = new OWLocationManager();
	}
	
	public OWLocationManager getLocationManager(){
		return locationManager;
	}
	
}
