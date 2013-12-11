package com.massivekinetics.ow.application.location;

import android.content.Context;
import android.location.Location;

import java.util.Map;

/**
 * Created by bovy on 12/9/13.
 */
public interface IGeoLocator {

    boolean getLocation(Context context, LocationResult outResult);
    Map<String, String> getLocationInfo(Location location);
    String getGpsCoordinatesAsParams(Location location);

    public static abstract class LocationResult {
        public abstract void gotLocation(Location location);
    }
}
