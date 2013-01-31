package com.massivekinetics.ow.location;

import android.content.Context;
import android.location.*;
import com.massivekinetics.ow.application.OWApplication;
import com.massivekinetics.ow.status.OperationStatus;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class OWLocationManager {
	LocationManager locationManager;
	Geocoder geoCoder = new Geocoder(OWApplication.context, Locale.US);
    String provider = LocationManager.NETWORK_PROVIDER;
	Criteria criteria = new Criteria();

	public OWLocationManager() {
		locationManager = (LocationManager) OWApplication.context.getSystemService(Context.LOCATION_SERVICE);
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
	}

	public void registerForUpdates(LocationListener listener) {
		locationManager.requestLocationUpdates(provider, 0, 0, listener);
	}

	public void unregisterFromUpdates(LocationListener listener) {
		locationManager.removeUpdates(listener);
	}

	public OWLocation getLastKnownLocation() {
        List<String> providers = locationManager.getProviders(true);
        OWLocation location = OWLocation.NULL;

        Location l = null;

        for (int i=providers.size()-1; i>=0; i--) {
            l = locationManager.getLastKnownLocation(providers.get(i));
            if (l != null){
                location = new OWLocation(l, OperationStatus.SUCCESS);
                break;
            }
        }
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
