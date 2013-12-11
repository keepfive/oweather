package com.massivekinetics.ow.application.location;

import android.content.Context;
import android.content.res.Resources;
import android.location.*;
import android.os.Bundle;
import android.os.Handler;
import com.massivekinetics.ow.R;
import com.massivekinetics.ow.application.Application;
import com.massivekinetics.ow.domain.parser.geocoder.GeocoderConstants;
import com.massivekinetics.ow.domain.parser.geocoder.GeocoderParser;
import com.massivekinetics.ow.services.network.NetworkUtils;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: bovy
 * Date: 2/13/13
 * Time: 2:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class GeoLocator implements IGeoLocator {
    String TAG = "GeoLocator";
    Timer timer;
    LocationManager lm;
    LocationResult locationResult;
    boolean gps_enabled = false;
    boolean network_enabled = false;
    boolean passive_enabled = false;
    Handler handler;
    Resources resources;


    LocationListener locationListenerGps = new LocationListener() {
        public void onLocationChanged(Location location) {
            if (timer != null)
                synchronized (GeoLocator.class) {
                    timer.cancel();
                }
            locationResult.gotLocation(location);
            removeLocationUpdates();
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };
    LocationListener locationListenerNetwork = new LocationListener() {
        public void onLocationChanged(Location location) {
            if (timer != null)
                synchronized (GeoLocator.class) {
                    timer.cancel();
                }
            locationResult.gotLocation(location);
            removeLocationUpdates();
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };
    private int tries = 0;

    public GeoLocator(){
        this.resources = Application.getInstance().getResources();
    }

    @Override
    public boolean getLocation(Context context, LocationResult result) {
        if (lm == null)
            lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        this.locationResult = result;

        //exceptions will be thrown if provider is not permitted.
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }
        try {
            passive_enabled = lm.isProviderEnabled(LocationManager.PASSIVE_PROVIDER);
        } catch (Exception ex) {
        }

        //don't start listeners if no provider is enabled
        if (!gps_enabled && !network_enabled && !passive_enabled)
            return false;


        requestLocationUpdates();
       /* timer = new Timer();
        timer.schedule(new GetLastLocation(result), 3000);  */

        handler = new Handler();
        handler.postDelayed(new GetLastLocation(result), 3000);

        return true;
    }

    @Override
    public Map<String, String> getLocationInfo(Location location) {
        if (location != null) {
            return getLocationInfoFromGoogle(location);
        }
        return null;
    }

    private Map<String, String> getLocationInfoFromGoogle(Location location){
        String gps = getGpsCoordinatesAsParams(location);
        String request = resources.getString(R.string.google_geocoder);
        String locale = resources.getString(R.string.locale);
        request = request.replace("[LOCATION]", gps);
        request = request.replace("[LANG]", locale);
        String response = NetworkUtils.doGet(request);
        Map<String, String> locationMap = new GeocoderParser().getLocationInfoMap(response);
        String gpsParams = getGpsCoordinatesAsParams(location);
        locationMap.put(GeocoderConstants.GPS_PARAMS, gpsParams);
        return locationMap;
    }

    @Override
    public String getGpsCoordinatesAsParams(Location location){
        if (location == null)
            return null;

        String params = "[lat],[long]";
        params = params.replace("[lat]", "" + location.getLatitude());
        params = params.replace("[long]", "" + location.getLongitude());

        return params;
    }

    //--------------------------------------------------------------------------------------

    private void requestLocationUpdates() {
        if (gps_enabled)
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListenerGps);
        if (network_enabled)
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListenerNetwork);
        if (passive_enabled)
            lm.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0, locationListenerNetwork);
    }

    private void removeLocationUpdates() {
        lm.removeUpdates(locationListenerGps);
        lm.removeUpdates(locationListenerNetwork);
    }

    class GetLastLocation extends TimerTask {
        private LocationResult locationResult;

        public GetLastLocation(LocationResult locationResult) {
            this.locationResult = locationResult;
        }

        @Override
        public void run() {
            removeLocationUpdates();

            Location net_loc = null, gps_loc = null;
            if (gps_enabled)
                gps_loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (network_enabled)
                net_loc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (passive_enabled)
                net_loc = lm.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            //if there are both values use the latest one
            if (gps_loc != null && net_loc != null) {
                if (gps_loc.getTime() > net_loc.getTime())
                    locationResult.gotLocation(gps_loc);
                else
                    locationResult.gotLocation(net_loc);
                return;
            }

            if (gps_loc != null) {
                locationResult.gotLocation(gps_loc);
                return;
            }
            if (net_loc != null) {
                locationResult.gotLocation(net_loc);
                return;
            }

            if (tries < 5) {
                tries++;
                requestLocationUpdates();
                handler.postDelayed(new GetLastLocation(locationResult), 3000);
                //timer.schedule(new GetLastLocation(locationResult), 3000);
                return;
            }
            locationResult.gotLocation(null);
        }
    }
}