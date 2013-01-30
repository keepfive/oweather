package com.massivekinetics.ow.location;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import com.massivekinetics.ow.application.OWApplication;

public class OWLocationManager {
	LocationManager locationManager;
	String provider;

	Geocoder geoCoder = new Geocoder(OWApplication.context, Locale.US);
	Criteria criteria = new Criteria();

	public OWLocationManager() {
		locationManager = (LocationManager) OWApplication.context.getSystemService(Context.LOCATION_SERVICE);
	}

	public void registerForUpdates(LocationListener listener) {
		provider = locationManager.getBestProvider(criteria, false);
		locationManager.requestLocationUpdates(provider, 1, 1, listener);
	}

	public void unregisterFromUpdates(LocationListener listener) {
		locationManager.removeUpdates(listener);
	}

	public Location getLastKnownLocation() {
		String provider = locationManager.getBestProvider(criteria, false);
		Location location = locationManager.getLastKnownLocation(provider);
		return location;
	}
	
	public String getCityName(Location location) throws IOException {
		if(location == null)
			return "unknown";
		else{
			List<Address> addressList = geoCoder.getFromLocation(location.getLatitude(), location.getLongitude(), 5);
			Address random = addressList.get(0);
			return random.getLocality();
		}
	}

}
