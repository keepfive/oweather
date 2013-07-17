package com.massivekinetics.ow.data.parser.geocoder;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.massivekinetics.ow.data.parser.geocoder.GeocoderConstants.*;

public class GeocoderParser {
    private static final String TAG = "GeocoderParser";

    public String getLocationName(String json) {
        String locationName = null;
        if (!json.contains("error")) {
            try {
                JSONObject dataObject = new JSONObject(json);
                String status = dataObject.getString(STATUS);
                if (status.equalsIgnoreCase(OK)) {
                    JSONArray resultsArray = dataObject.getJSONArray(RESULTS);
                    for (int pos = 0; pos < resultsArray.length(); pos++) {
                        JSONObject addressObject = resultsArray.getJSONObject(pos);
                        JSONArray addressComponentsArray = addressObject.getJSONArray(ADDRESS_COMPONENTS);
                        for (int i = 0; i < addressComponentsArray.length(); i++){
                            JSONObject addressItem = addressComponentsArray.getJSONObject(i);
                            JSONArray typeArray = addressItem.getJSONArray(TYPES);
                            if(typeArray.getString(0).equalsIgnoreCase(LOCALITY))
                                locationName = addressItem.getString(LONG_NAME);
                        }
                    }
                }
            } catch (JSONException jse) {
                Log.e(TAG, jse.getMessage());
            }
        }
        return locationName;
    }
}
