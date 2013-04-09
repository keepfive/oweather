package com.massivekinetics.ow.data.enums;

/**
 * Created with IntelliJ IDEA.
 * User: bovy
 * Date: 3/22/13
 * Time: 3:15 AM
 */
public enum DayName {
    TODAY, TOMORROW, YESTERDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY;

    private String displayName;

    DayName() {
       setDisplayName(toString());
    }

    DayName(String displayName){
       setDisplayName(displayName);
    }

    public String getDisplayName(){
        return displayName;

        /*String first = displayName.substring(0,1).toUpperCase();
        String last = displayName.substring(1).toLowerCase();
        return first.concat(last);*/
    }

    private void setDisplayName(String displayName){
        String first = displayName.substring(0,1).toUpperCase();
        String last = displayName.substring(1).toLowerCase();
        this.displayName = first.concat(last);


        //this.displayName = displayName;
    }
}
