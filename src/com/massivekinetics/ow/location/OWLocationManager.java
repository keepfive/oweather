package com.massivekinetics.ow.location;

import android.content.Context;
import android.location.*;
import android.os.Bundle;
import com.massivekinetics.ow.application.OWApplication;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created with IntelliJ IDEA.
 * User: bovy
 * Date: 2/13/13
 * Time: 2:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class OWLocationManager {
    String TAG = "OWLocationManager";

    private int tries = 0;

    Timer timer;
    LocationManager lm;
    LocationResult locationResult;
    Geocoder geoCoder = new Geocoder(OWApplication.context, Locale.US);

    boolean gps_enabled=false;
    boolean network_enabled=false;
    boolean passive_enabled = false;

    public boolean getLocation(Context context, LocationResult result)
    {
        //I use LocationResult callback class to pass location value from OWLocationManager to user code.
        locationResult=result;
        if(lm==null)
            lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        //exceptions will be thrown if provider is not permitted.
        try{gps_enabled=lm.isProviderEnabled(LocationManager.GPS_PROVIDER);}catch(Exception ex){}
        try{network_enabled=lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);}catch(Exception ex){}
        try{passive_enabled=lm.isProviderEnabled(LocationManager.PASSIVE_PROVIDER);}catch(Exception ex){}

        //don't start listeners if no provider is enabled
        if(!gps_enabled && !network_enabled &&!passive_enabled)
            return false;

        requestLocationUpdates();

        timer = new Timer();
        timer.schedule(new GetLastLocation(), 3000);

        return true;
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

    private void requestLocationUpdates(){
        if(gps_enabled)
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListenerGps);
        if(network_enabled)
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListenerNetwork);
        if(passive_enabled)
            lm.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0, locationListenerNetwork);
    }
    private void removeLocationUpdates(){
        lm.removeUpdates(locationListenerGps);
        lm.removeUpdates(locationListenerNetwork);
    }

    //--------------------------------------------------------------------------------------

    LocationListener locationListenerGps = new LocationListener() {
        public void onLocationChanged(Location location) {
            timer.cancel();
            locationResult.gotLocation(location);
            removeLocationUpdates();
        }
        public void onProviderDisabled(String provider) {}
        public void onProviderEnabled(String provider) {}
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };

    LocationListener locationListenerNetwork = new LocationListener() {
        public void onLocationChanged(Location location) {
            timer.cancel();
            locationResult.gotLocation(location);
            removeLocationUpdates();
        }
        public void onProviderDisabled(String provider) {}
        public void onProviderEnabled(String provider) {}
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };

    class GetLastLocation extends TimerTask {
        @Override
        public void run() {
            removeLocationUpdates();

            Location net_loc=null, gps_loc=null;
            if(gps_enabled)
                gps_loc=lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(network_enabled)
                net_loc=lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if(passive_enabled)
                net_loc=lm.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            //if there are both values use the latest one
            if(gps_loc!=null && net_loc!=null){
                if(gps_loc.getTime()>net_loc.getTime())
                    locationResult.gotLocation(gps_loc);
                else
                    locationResult.gotLocation(net_loc);
                return;
            }

            if(gps_loc!=null){
                locationResult.gotLocation(gps_loc);
                return;
            }
            if(net_loc!=null){
                locationResult.gotLocation(net_loc);
                return;
            }

            if(tries < 5){
                tries++;
                requestLocationUpdates();
                timer.schedule(new GetLastLocation(), 3000);
                return;
            }
            locationResult.gotLocation(null);
        }
    }

    public static abstract class LocationResult{
        public abstract void gotLocation(Location location);
    }
}