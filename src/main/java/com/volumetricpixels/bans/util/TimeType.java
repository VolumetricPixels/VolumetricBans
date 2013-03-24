package com.volumetricpixels.bans.util;

/**
 * Utility enum for time measurements, used in temp bans / mutes
 */
public enum TimeType {
	/** Represents minutes */
	MINUTES,
	/** Represents hours */
	HOURS,
	/** Represents days */
	DAYS;

	/**
	 * Constructs a new TimeType
	 */
	private TimeType() {
	}

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
