package com.massivekinetics.ow.domain.enums;

import com.massivekinetics.ow.R;
import com.massivekinetics.ow.application.Application;

/**
 * Created with IntelliJ IDEA.
 * User: bovy
 * Date: 11/19/13
 * Time: 2:05 PM
 * To change this template use File | Settings | File Templates.
 */
public enum WindDirection {
     N(R.string.direction_n), NE(R.string.direction_ne), E(R.string.direction_e),
    SE(R.string.direction_se), S(R.string.direction_s), SW(R.string.direction_sw),
    W(R.string.direction_w), NW(R.string.direction_nw);

    WindDirection(int resId){
       mDisplayName = Application.getInstance().getString(resId);
    }

    /**
     * Method to parse string data to localized enum.
     * As there will be only 8 states forever,
     * I decided this method should live.
     * */
    public static WindDirection parse(String value){
        if (value.equalsIgnoreCase("n"))
            return N;
        else if (value.equalsIgnoreCase("ne"))
            return NE;
        else if (value.equalsIgnoreCase("e"))
            return E;
        else if (value.equalsIgnoreCase("se"))
            return SE;
        else if (value.equalsIgnoreCase("s"))
            return S;
        else if (value.equalsIgnoreCase("sw"))
            return SW;
        else if (value.equalsIgnoreCase("w"))
            return W;
        else if (value.equalsIgnoreCase("nw"))
            return NW;
        else
            throw new IllegalArgumentException("Cant parse income value to WindDirection; value: " + value);
    }

    public String getDisplayName(){
        return mDisplayName;
    }

    String mDisplayName;
}
