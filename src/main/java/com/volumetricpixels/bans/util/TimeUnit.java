package com.volumetricpixels.bans.util;

/**
 * Utility enum for time measurements, used in temp bans / mutes
 */
public enum TimeUnit {
    /** Represents seconds */
    SECONDS,
    /** Represents minutes */
    MINUTES,
    /** Represents hours */
    HOURS,
    /** Represents days */
    DAYS;

    /** Constructs a new TimeUnit */
    private TimeUnit() {
    }

    /**
     * Converts the given amount of time to minutes
     * 
     * @param l
     *            The amount of time, in this TimeUnit
     * 
     * @return The amount of minutes in the given amount of time
     */
    public double toMinutes(final double l) {
        switch (this) {
            case SECONDS:
                return l / 60;
            case MINUTES:
                return l;
            case HOURS:
                return l * 60;
            case DAYS:
                return l * 60 * 24;
            default:
                return l;
        }
    }

    /**
     * Converts the given amount of time to milliseconds
     * 
     * @param time
     *            The amount of time in this TimeUnit
     * 
     * @return The amount of milliseconds in the given amount of time
     */
    public long toMillis(final long time) {
        switch (this) {
            case SECONDS:
                return time * 1000;
            case MINUTES:
                return time * 60 * 1000;
            case HOURS:
                return time * 60 * 60 * 1000;
            case DAYS:
                return time * 24 * 60 * 60 * 1000;
            default:
                return time;
        }
    }

    /**
     * Parses a TimeUnit from a string
     * 
     * @param string
     *            The type of time as a string (e.g "hours")
     * 
     * @return The enum value for the given TimeUnit string
     */
    public static TimeUnit parse(final String string) {
        switch (string.toLowerCase().charAt(0)) {
            case 's':
                return SECONDS;
            case 'm':
                return MINUTES;
            case 'h':
                return HOURS;
            case 'd':
                return DAYS;
            default:
                return null;
        }
    }
}
