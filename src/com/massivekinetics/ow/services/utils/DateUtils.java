package com.massivekinetics.ow.services.utils;

import com.massivekinetics.ow.domain.enums.DayName;

import java.util.Calendar;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: bovy
 * Date: 3/22/13
 * Time: 3:10 AM
 */
public class DateUtils {
    public boolean isToday(Date inputDate){
        String name = getDayName(inputDate);
        return name.equalsIgnoreCase(DayName.TODAY.getDisplayName());
    }

    public String getDayName(Date inputDate) {
        String name = "";
        DayName dayName = DayName.TODAY;

        Calendar cal = Calendar.getInstance();
        int today = cal.get(Calendar.DAY_OF_WEEK);
        cal.setTime(inputDate);
        int inputDay = cal.get(Calendar.DAY_OF_WEEK);

        int diff = today - inputDay;
        if(isNearestFuture(diff)){
            switch (diff){
                case 0:
                    dayName = DayName.TODAY;
                    break;
                case 1:
                case -6:
                    dayName = DayName.YESTERDAY;
                    break;
                case -1:
                case 6:
                    dayName = DayName.TOMORROW;
                    break;
            }
        } else {
            switch (inputDay){
                case 1:
                    dayName = DayName.SUNDAY;
                    break;
                case 2:
                    dayName = DayName.MONDAY;
                    break;
                case 3:
                    dayName = DayName.TUESDAY;
                    break;
                case 4:
                    dayName = DayName.WEDNESDAY;
                    break;
                case 5:
                    dayName = DayName.THURSDAY;
                    break;
                case 6:
                    dayName = DayName.FRIDAY;
                    break;
                case 7:
                    dayName = DayName.SATURDAY;
                    break;
                default:
                    dayName = DayName.TODAY;
            }
        }

        return dayName.getDisplayName();
    }

    private boolean isNearestFuture(int diff) {
        return diff == 0 || diff == 1 || diff == -6 || diff == -1 || diff == 6;
    }

    public static long getCurrentInMillis(){
        return new Date().getTime();
    }
}
