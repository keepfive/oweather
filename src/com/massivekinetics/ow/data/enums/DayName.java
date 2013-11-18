package com.massivekinetics.ow.data.enums;

import com.massivekinetics.ow.R;
import com.massivekinetics.ow.application.Application;

/**
 * Created with IntelliJ IDEA.
 * User: bovy
 * Date: 3/22/13
 * Time: 3:15 AM
 */
public enum DayName {
    TODAY(R.string.today), TOMORROW(R.string.tomorrow), YESTERDAY(R.string.yesterday),
    MONDAY(R.string.monday), TUESDAY(R.string.tuesday), WEDNESDAY(R.string.wednesday),
    THURSDAY(R.string.thursday), FRIDAY(R.string.friday), SATURDAY(R.string.saturday),
    SUNDAY(R.string.sunday);

    DayName(int resId) {
        String displayName = Application.getInstance().getString(resId);
        setDisplayName(displayName);
    }

    public String getDisplayName() {
        return mDisplayName;
    }

    private void setDisplayName(String displayName) {
        mDisplayName = displayName;
    }

    private String mDisplayName;
}
