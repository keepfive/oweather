package com.massivekinetics.ow.network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: bovy
 * Date: 1/27/13
 * Time: 7:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class Downloader {
    public static String doGet(final String serverUrl) {
        StringBuffer data = new StringBuffer(256);
        try {
            URL url = new URL(serverUrl);
            //Ensures that connections are closed when switching networks
            System.setProperty("http.keepAlive", "false");

            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                data.append(line + "\n");
            }
            reader.close();
        } catch (Exception e) {

        }
        return data.toString();
    }

}
