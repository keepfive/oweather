package com.massivekinetics.ow.services.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;
import com.massivekinetics.ow.R;
import com.massivekinetics.ow.application.Application;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: bovy
 * Date: 1/27/13
 * Time: 7:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class NetworkUtils {
    public static final String CODE_403 = "session_expired";
    private static final String TAG = "NetworkUtils";

    public static String doGet(final String serverUrl) {
        return doGet(serverUrl, 7000);
    }

    public static String doGet(final String serverUrl, int timeout) {
        StringBuffer data = new StringBuffer(256);
        try {
            URL url = new URL(serverUrl);
            //Ensures that connections are closed when switching networks
            System.setProperty("http.keepAlive", "false");
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                data.append(line + "\n");
            }
            reader.close();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return data.toString();
    }

    public static String doPost(final String serverUrl, final String body) {
        final StringBuffer data = new StringBuffer(256);


        DataOutputStream writer = null;
        BufferedReader reader = null;

        try {
            // POST data.
            final URL url = new URL(serverUrl);

            // Ensures that connections are closed when switching networks
            System.setProperty("http.keepAlive", "false");

            final HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setConnectTimeout(7000);
            connection.setReadTimeout(7000);

            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty ("Accept", "application/json");
            connection.setRequestProperty("Content-Length", Integer.toString(body.length()));
            connection.setRequestProperty("Content-Type","text/plain");
            connection.setRequestProperty("OS-TYPE","android");

            writer = new DataOutputStream(connection.getOutputStream());
            writer.writeBytes(body);
            writer.flush();
            // Get the response

            if(connection.getResponseCode() == 403)
                return CODE_403;

            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is));
            String line;

            while ((line = reader.readLine()) != null) {
                data.append(line);
                data.append('\r');
            }
            reader.close();


        } catch (Exception e) {
            /*if(e != null)
                Log.e(TAG, e.getMessage()); */
        } finally {
            try {
                if (writer != null)
                    writer.close();
                if (reader != null)
                    reader.close();
            } catch (Exception e) {
                Log.e(TAG, "Exception while closing reader or writer", e);
            }
        }

        return data.toString();
    }


    public static String getSession(){
        String requestURL = Application.getInstance().getString(R.string.ow_url_get_session);
        String base64 = doGet(requestURL, 2000);
        return base64;
    }

    public static boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager) Application.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        return (cm.getActiveNetworkInfo() == null) ? false : true;
    }

}
