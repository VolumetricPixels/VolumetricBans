package com.volumetricpixels.bans.util;

/** Utility enum for time measurements, used in temp bans / mutes */
public enum TimeType {
    /** Represents minutes */
    MINUTES,
    /** Represents hours */
    HOURS,
    /** Represents days */
    DAYS;

    /** Constructs a new TimeType */
    private TimeType() {
    }

    /**
     * Converts the given amount of time to minutes
     *
     * @param l The amount of time, in this TimeType
     *
     * @return The amount of minutes in the given amount of time
     */
    public long toMinutes(long l) {
        switch (this) {
            case MINUTES:
                return l;
            case HOURS:
                return l * 60;
            case DAYS:
                return (l * 60) * 24;
            default:
                return l;
        }
    }

    /**
     * Parses a TimeType from a string
     *
     * @param string The type of time as a string (e.g "hours")
     *
     * @return The enum value for the given TimeType string
     */
    public static TimeType parse(String string) {
        String s = String.valueOf(string.toLowerCase().charAt(0));
        if (s.equals("m")) {
            return MINUTES;
        } else if (s.equals("h")) {
            return HOURS;
        } else if (s.equals("d")) {
            return DAYS;
        }
        return null;
    }
}
