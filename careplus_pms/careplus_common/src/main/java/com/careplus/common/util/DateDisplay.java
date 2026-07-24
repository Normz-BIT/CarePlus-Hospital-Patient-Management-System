package com.careplus.common.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/*
 * How we show dates on screen, in one place.
 *
 * Before this we were dropping LocalDateTime straight into tables, so cells came
 * out as "2026-06-25T10:30" which nobody wants to read. Everything goes through
 * here now: day, month, year, then hour and minute.
 *
 * Locale.UK so the month always comes out in English no matter what the machine
 * running it is set to. Without it the same table could read "Jun" on one
 * laptop and something else on another.
 */
public final class DateDisplay {

	/** e.g. 25 Jun 2026 10:30 */
	private static final DateTimeFormatter DAY_MONTH_YEAR_TIME =
			DateTimeFormatter.ofPattern("dd MMM uuuu HH:mm", Locale.UK);

	/*
	 * For the columns behind a DATE rather than a DATETIME (date of birth, hire
	 * date, follow up date). Those never had a real time saved, so printing
	 * "00:00" after them would just be noise pretending to be information.
	 */
	private static final DateTimeFormatter DAY_MONTH_YEAR =
			DateTimeFormatter.ofPattern("dd MMM uuuu", Locale.UK);

	/* Nobody should be creating one of these. */
	private DateDisplay() {
	}

	/*
	 * Day month year plus the time. Empty String for null rather than the word
	 * "null", since these go straight into table cells and an empty cell reads as
	 * "nothing here" which is what null actually means.
	 */
	public static String withTime(LocalDateTime value) {

		return value == null ? "" : DAY_MONTH_YEAR_TIME.format(value);
	}

	/* Day month year on its own, for the date-only columns. */
	public static String dateOnly(LocalDateTime value) {

		return value == null ? "" : DAY_MONTH_YEAR.format(value);
	}
}
