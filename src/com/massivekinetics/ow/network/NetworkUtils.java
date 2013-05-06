package com.massivekinetics.ow.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;
import com.massivekinetics.ow.R;
import com.massivekinetics.ow.application.OWApplication;

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
    public static String doGet(final String serverUrl) {
        StringBuffer data = new StringBuffer(256);
        try {
            URL url = new URL(serverUrl);
            //Ensures that connections are closed when switching networks
            System.setProperty("http.keepAlive", "false");
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setConnectTimeout(4000);
            connection.setReadTimeout(10000);

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                data.append(line + "\n");
            }
            reader.close();
        } catch (Exception e) {
              String m = e.getMessage();
            boolean b = true;
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
            connection.setConnectTimeout(4000);
            connection.setReadTimeout(10000);

            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty ("Accept", "application/json");
            connection.setRequestProperty("Content-Length", Integer.toString(body.length()));
            connection.setRequestProperty("Content-Type","text/plain");

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

        } finally {
            try {
                if (writer != null)
                    writer.close();
                if (reader != null)
                    reader.close();
            } catch (Exception e) {
                Log.e("Security", "Exception while closing reader or writer", e);
            }
        }

        return data.toString();
    }


    public static String getSession(){
        String requestURL = OWApplication.getInstance().getString(R.string.test_server_url_get_session);
        String base64 = doGet(requestURL);
        return base64;
    }

    public static boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager)OWApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        return (cm.getActiveNetworkInfo() == null) ? false : true;
    }

}
