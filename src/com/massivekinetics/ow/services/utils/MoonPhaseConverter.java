package com.massivekinetics.ow.services.utils;

/**
 * Created with IntelliJ IDEA.
 * User: bovy
 * Date: 1/25/13
 * Time: 1:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class MoonPhaseConverter {
    public enum MOONPHASE {NONE, NEW_MOON, WAXING_MOON, FIRST_QUARTER, FULL_MOON, WANING_MOON, LAST_QUARTER, DARK_MOON}

    public static MOONPHASE getMoonPhase(int d, int m, int y) {
        double approximate, precise;

        int julianDate = getJulianDate(d, m, y);
        //Calculate the approximate phase of the moon
        approximate = (julianDate + 4.867) / 29.53059;
        approximate = approximate - Math.floor(approximate);
        //After several trials I've seen to add the following lines,
        //which gave the result was not bad
        if (approximate < 0.5)
            precise = approximate * 29.53059 + 29.53059 / 2;
        else
            precise = approximate * 29.53059 - 29.53059 / 2;
        // Moon's age in days
        precise = Math.floor(precise) + 1;
        MOONPHASE moonphase = MOONPHASE.NONE;

        if (precise == 0 || precise == 30)
            moonphase = MOONPHASE.NEW_MOON;
        else if (precise == 15)
            moonphase = MOONPHASE.FULL_MOON;
        else if (precise > 0 || precise < 7)
            moonphase = MOONPHASE.WAXING_MOON;
        else if (precise >= 7 || precise < 15)
            moonphase = MOONPHASE.FIRST_QUARTER;
        else if (precise > 15 && precise <= 22)
            moonphase = MOONPHASE.LAST_QUARTER;
        else if (precise > 22 && precise < 30)
            moonphase = MOONPHASE.LAST_QUARTER;

        return moonphase;
    }

    private static int getJulianDate(int d, int m, int y) {
        int mm, yy;
        int k1, k2, k3;
        int j;

        yy = y - (int) ((12 - m) / 10);
        mm = m + 9;
        if (mm >= 12) {
            mm = mm - 12;
        }
        k1 = (int) (365.25 * (yy + 4712));
        k2 = (int) (30.6001 * mm + 0.5);
        k3 = (int) ((int) ((yy / 100) + 49) * 0.75) - 38;
        // 'j' for dates in Julian calendar:
        j = k1 + k2 + d + 59;
        if (j > 2299160) {
            // For Gregorian calendar:
            j = j - k3; // 'j' is the Julian date at 12h UT (Universal Time)
        }
        return j;
    }
}
