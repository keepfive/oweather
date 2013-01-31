package com.massivekinetics.ow.location;

import com.massivekinetics.ow.status.OperationStatus;
import android.location.Location;

/**
 * Created with IntelliJ IDEA.
 * User: bovy
 * Date: 1/30/13
 * Time: 4:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class OWLocation {
    public OperationStatus status;
    public Location location;

    public static final OWLocation NULL = new OWLocation(null, OperationStatus.ERROR);

    public OWLocation(Location location, OperationStatus status){
        this.location = location;
        this.status = status;
    }
}
