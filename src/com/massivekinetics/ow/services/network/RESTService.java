package com.massivekinetics.ow.services.network;

import android.util.Base64;
import com.massivekinetics.ow.R;
import com.massivekinetics.ow.application.AppLocator;
import com.massivekinetics.ow.application.Application;
import com.massivekinetics.ow.application.IConfiguration;

import javax.crypto.Cipher;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;

/**
 * Created with IntelliJ IDEA.
 * User: bovy
 * Date: 12/4/13
 * Time: 3:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class RESTService {

    public static final String getWeatherRequestEncryptedBody() {
        final String encodedCoordinates = getEncodedCoordinates();
        return encryptToRSA(encodedCoordinates);
    }

    public static final String encryptToRSA(String message) {
        Application application = Application.getInstance();
        IConfiguration configuration = AppLocator.resolve(IConfiguration.class);
        String modulus = configuration.getActiveSession();
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
            byte[] encryptedByteData = cipher.doFinal(message.getBytes());

            String outputEncrypted = Base64.encodeToString(encryptedByteData, Base64.URL_SAFE);
            return outputEncrypted;
        } catch (Exception exc) {
            return null;
        }
    }

    public static final String encodeCoordinate(float coordinate) {
        final int NEEDED_INT_DIGITS = 3;
        final int NEEDED_FRACTIONAL_DIGITS = 6;

        StringBuilder coordinateBuilder = new StringBuilder();
        //Adding sign
        coordinateBuilder.append((coordinate > 0) ? 0 : 1);
        coordinate = Math.abs(coordinate);

        //Constructing integral part of gps
        int integral = (int) coordinate;
        int integralDigitLength = String.valueOf(integral).length();
        while(integralDigitLength < NEEDED_INT_DIGITS){
            coordinateBuilder.append(0);
            integralDigitLength++;
        }
        coordinateBuilder.append(integral);

        //Constructing fractional part of gps
        float fractional = coordinate - integral;
        //String fractionalPart = "" + (int)(fractional * Math.pow(10, NEEDED_FRACTIONAL_DIGITS));
        String fractionalPart = (""+fractional).substring(2);
        coordinateBuilder.append(fractionalPart);

        int resultSize = NEEDED_FRACTIONAL_DIGITS + NEEDED_INT_DIGITS + 1;
        while(coordinateBuilder.length() < resultSize){
            coordinateBuilder.append(0);
        }

        return coordinateBuilder.toString().substring(0, resultSize);
    }

    public static final String getEncodedCoordinates() {
        Application application = Application.getInstance();
        IConfiguration configuration = AppLocator.resolve(IConfiguration.class);

        String result = "";
        String locationString = configuration.getLocationCoordinates();
        String[] coordinateArray = locationString.split(",");
        for (String coordinate : coordinateArray) {
            float value = Float.parseFloat(coordinate);
            result += encodeCoordinate(value);
        }

        String key = application.getString(R.string.rsa_token);
        return result + key;
    }
}
