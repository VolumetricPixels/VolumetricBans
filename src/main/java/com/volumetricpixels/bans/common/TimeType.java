package com.volumetricpixels.bans.common;

/**
 * Utility enum for time measurements
 * 
 * @author DziNeIT
 */
public enum TimeType {
    MINUTES(), HOURS(), DAYS();

    private TimeType() {
    }

    public static TimeType parse(String string) {
        String s = String.valueOf(string.toLowerCase().charAt(0));

        if (s.equals("m")) {
            return TimeType.MINUTES;
        } else if (s.equals("h")) {
            return TimeType.HOURS;
        } else if (s.equals("d")) {
            return TimeType.DAYS;
        }

        return null;
    }
}
