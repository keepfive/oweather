package com.massivekinetics.ow.data;

import android.util.Log;
import com.massivekinetics.ow.R;
import com.massivekinetics.ow.application.OWApplication;
import com.massivekinetics.ow.data.manager.OWConfigManager;
import com.massivekinetics.ow.data.model.Prediction;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: bovy
 * Date: 5/10/13
 * Time: 1:30 AM
 */
public class Autocompleter {

    private static final String LOG_TAG = "Autocompleter";

    public ArrayList<Prediction> getPlacePredictions(String input) {
        ArrayList<Prediction> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            input = URLEncoder.encode(input, "utf8");
            String key = OWApplication.getInstance().getString(R.string.autocomplete_key);
            String offset = "" + input.length();
            String serverUrl = OWApplication.getInstance().getString(R.string.autocomplete_url).replace("[KEY]", key).replace("[INPUT]", input).replace("[OFFSET]", offset);

            URL url = new URL(serverUrl);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList<Prediction>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                JSONObject jsonObject = predsJsonArray.getJSONObject(i);
                String description = jsonObject.getString("description");
                String reference = jsonObject.getString("reference");

                String city = description;
                try{
                    city = jsonObject.getJSONArray("terms").getJSONObject(0).getString("value");
                }catch (Exception e){}

                resultList.add(new Prediction(description, reference, city));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }

    public String getPlaceCoordinates(String reference) {
        String result = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {

            String key = OWApplication.getInstance().getString(R.string.autocomplete_key);
            String serverUrl = OWApplication.getInstance().getString(R.string.autocomplete_details_url).replace("[KEY]", key).replace("[REFERENCE]", reference);

            URL url = new URL(serverUrl);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return result;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return result;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONObject resultObj = jsonObj.getJSONObject("result");
            JSONArray jsonAddress = resultObj.getJSONArray("address_components");

            String admin_area_1 = null;
            String country = null;
            for(int i=0; i < jsonAddress.length(); i++){
                JSONObject address = jsonAddress.getJSONObject(i);
                String types = address.getString("types");
                if(types.contains("administrative_area_1"))
                    admin_area_1 = address.getString("long_name");
                if(types.contains("country"))
                    country = address.getString("long_name");
            }

            if(country == null){
                if(admin_area_1 == null)
                    country = "";
                else
                    country = admin_area_1;
            }

            new OWConfigManager().setLocationCountry(country);

            JSONObject geometryObj = resultObj.getJSONObject("geometry");
            JSONObject locationObj =  geometryObj.getJSONObject("location");
            String lat = locationObj.getString("lat");
            String lng = locationObj.getString("lng");
            result = lat + "," + lng;

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return result;
    }
}
