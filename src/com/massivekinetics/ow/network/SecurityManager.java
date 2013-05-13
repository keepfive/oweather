package com.massivekinetics.ow.network;

import android.util.Base64;
import com.massivekinetics.ow.R;
import com.massivekinetics.ow.application.OWApplication;
import com.massivekinetics.ow.data.manager.ConfigManager;

import javax.crypto.Cipher;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;

/**
 * Created with IntelliJ IDEA.
 * User: bovy
 * Date: 4/27/13
 * Time: 12:17 AM
 */
public class SecurityManager {

    public static final String getWeatherRequestEncryptedBody() {
        final String encodedCoordinates = getEncodedCoordinates();
        return getRSAEncryptedMessage(encodedCoordinates);
    }

    public static final String getRSAEncryptedMessage(String input) {
        OWApplication application = OWApplication.getInstance();
        ConfigManager configManager = application.getConfigManager();
        String modulus = configManager.getActiveSession();
        String exponent = application.getString(R.string.rsa_exponent);
        byte[] modulusBytes = Base64.decode(modulus.getBytes(), Base64.DEFAULT);

        BigInteger e = new BigInteger(exponent);
        BigInteger m = new BigInteger(modulusBytes);

        try {
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(m, e);
            KeyFactory fact = KeyFactory.getInstance("RSA");
            PublicKey pubKeyn = fact.generatePublic(keySpec);

            Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, pubKeyn);
            byte[] encryptedByteData = cipher.doFinal(input.getBytes());

            String outputEncrypted = Base64.encodeToString(encryptedByteData, Base64.URL_SAFE);
            return outputEncrypted;
        } catch (Exception exc) {
            return null;
        }
    }

    public static final String getEncodedCoordinate(float coordinate) {
        StringBuilder coordinateBuilder = new StringBuilder();
        coordinateBuilder.append((coordinate > 0) ? 0 : 1);
        coordinate = Math.abs(coordinate);

        if (coordinate < 100) {
            coordinateBuilder.append(0);
            if (coordinate < 10)
                coordinateBuilder.append(0);
        }

        int real = (int) coordinate;
        coordinateBuilder.append(real);

        float decimal = coordinate - real;
        String decimalPart = ("" + decimal).substring(2);

        coordinateBuilder.append(decimalPart);
        if (coordinateBuilder.length() > 10) {
            String result = coordinateBuilder.substring(0, 10);
            return result;
        } else {
            while (coordinateBuilder.length() < 10) {
                coordinateBuilder.append(0);
            }
            return coordinateBuilder.toString();
        }
    }

    public static final String getEncodedCoordinates() {
        OWApplication application = OWApplication.getInstance();
        ConfigManager configManager = application.getConfigManager();

        String result = "";
        String locationString = configManager.getLocationCoordinates();
        String[] coordinateArray = locationString.split(",");
        for (String coordinate : coordinateArray) {
            float value = Float.parseFloat(coordinate);
            result += getEncodedCoordinate(value);
        }

        String key = application.getString(R.string.rsa_token);
        return result + key;
    }
}
