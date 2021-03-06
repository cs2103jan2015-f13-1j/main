package util;

import java.time.*;
import java.time.format.*;
import java.time.temporal.*;
import java.util.Locale;

public class TimeExtractor {

	// @author A0105952H
	public static String formatDate(LocalDate t) {
		DateTimeFormatter formatter;

		formatter = DateTimeFormatter.ofPattern("d MMM uuuu").withLocale(
				Locale.ENGLISH);
		return t.format(formatter);
	}

	public static String formatDateTime(LocalDateTime t) {
		DateTimeFormatter formatter;

		if (t.getMinute() == 0) {
			formatter = DateTimeFormatter.ofPattern("d MMM uuuu ha")
					.withLocale(Locale.ENGLISH);
		} else {
			formatter = DateTimeFormatter.ofPattern("d MMM uuuu h.ma")
					.withLocale(Locale.ENGLISH);
		}
		return t.format(formatter);
	}

	public static LocalTime extractTime(String str) {
		try {
			DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
			builder.parseCaseInsensitive();
			builder.appendOptional(DateTimeFormatter.ofPattern("Hmm"));
			builder.appendOptional(DateTimeFormatter.ofPattern("HHmm"));
			builder.appendOptional(DateTimeFormatter.ofPattern("H mm"));
			builder.appendOptional(DateTimeFormatter.ofPattern("HH mm"));
			builder.appendOptional(DateTimeFormatter.ofPattern("H:mm"));
			builder.appendOptional(DateTimeFormatter.ofPattern("HH:mm"));
			builder.appendOptional(DateTimeFormatter.ofPattern("ha"));
			builder.appendOptional(DateTimeFormatter.ofPattern("h.ma"));
			DateTimeFormatter dtf = builder.toFormatter().withLocale(
					Locale.ENGLISH);
			LocalTime time = LocalTime.parse(str, dtf);
			return time;
		} catch (Exception e) {
			return null;
		}
	}

	public static LocalDate extractDate(String str) {
		LocalDate date = null;
		date = DateFormatter1(str);
		if (date == null) {
			date = DateFormatter2(str);
		}
		if (date == null) {
			date = DateFormatter3(str);
		}
		if (date == null) {
			date = DateFormatter4(str);
		}
		if (date == null) {
			date = DateFormatter5(str);
		}
		return date;
	}

	private static LocalDate DateFormatter1(String str) {
		try {
			DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
			builder.parseCaseInsensitive();
			builder.parseDefaulting(ChronoField.YEAR, LocalDate.now().getYear());
			builder.appendOptional(DateTimeFormatter.ofPattern("d MMMM"));
			builder.appendOptional(DateTimeFormatter.ofPattern("d MMM"));
			builder.appendOptional(DateTimeFormatter.ofPattern("d M"));

			DateTimeFormatter dtf = builder.toFormatter().withLocale(
					Locale.ENGLISH);
			str = str.replaceAll("[\\s/\\-\\.]", " ");
			LocalDate date = LocalDate.parse(str, dtf);
			if (date.getDayOfYear() < LocalDate.now().getDayOfYear()) {
				date = date.plusYears(1);
			}
			return date;
		} catch (Exception e) {
			return null;
		}
	}

	private static LocalDate DateFormatter2(String str) {
		try {
			DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
			builder.parseCaseInsensitive();
			builder.parseDefaulting(ChronoField.YEAR, LocalDate.now().getYear());

			builder.appendOptional(DateTimeFormatter.ofPattern("M d"));
			builder.appendOptional(DateTimeFormatter.ofPattern("MMMM d"));
			builder.appendOptional(DateTimeFormatter.ofPattern("MMM d"));
			DateTimeFormatter dtf = builder.toFormatter().withLocale(
					Locale.ENGLISH);
			str = str.replaceAll("[\\s/\\-\\.]", " ");
			LocalDate date = LocalDate.parse(str, dtf);
			if (date.getDayOfYear() < LocalDate.now().getDayOfYear()) {
				date = date.plusYears(1);
			}
			return date;
		} catch (Exception e) {
			return null;
		}
	}

	private static LocalDate DateFormatter3(String str) {
		try {
			DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
			builder.parseCaseInsensitive();
			builder.appendOptional(DateTimeFormatter.ofPattern("d M uuuu"));
			builder.appendOptional(DateTimeFormatter.ofPattern("dd MM uuuu"));
			builder.appendOptional(DateTimeFormatter.ofPattern("d MMM uuuu"));
			builder.appendOptional(DateTimeFormatter.ofPattern("MMM d uuuu"));
			DateTimeFormatter dtf = builder.toFormatter().withLocale(
					Locale.ENGLISH);
			str = str.replaceAll("[\\s/\\-\\.]", " ");
			LocalDate date = LocalDate.parse(str, dtf);
			return date;
		} catch (Exception e) {
			return null;
		}
	}

	private static LocalDate DateFormatter4(String str) {
		try {
			DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
			builder.parseCaseInsensitive();
			builder.parseDefaulting(ChronoField.YEAR, LocalDate.now().getYear());
			builder.parseDefaulting(ChronoField.MONTH_OF_YEAR, LocalDate.now()
					.getMonthValue());
			builder.parseDefaulting(ChronoField.ALIGNED_WEEK_OF_MONTH, 1);
			builder.appendOptional(DateTimeFormatter.ofPattern("eeee"));
			builder.appendOptional(DateTimeFormatter.ofPattern("EEE"));
			DateTimeFormatter dtf = builder.toFormatter().withLocale(
					Locale.ENGLISH);
			LocalDate date = LocalDate.parse(str, dtf);
			DayOfWeek day = date.getDayOfWeek();
			date = LocalDate.now().with(TemporalAdjusters.nextOrSame(day));
			return date;
		} catch (Exception e) {
			return null;
		}
	}

	private static LocalDate DateFormatter5(String str) {

		LocalDate date = null;
		String[] spcdt = { "tomorrow", "tmr", "today", "tdy" };
		str = str.trim().toLowerCase();

		if (str.equalsIgnoreCase(spcdt[2]) || str.equalsIgnoreCase(spcdt[3])) {
			date = LocalDate.now();
		} else if (str.equalsIgnoreCase(spcdt[1])
				|| str.equalsIgnoreCase(spcdt[0])) {
			date = LocalDate.now().plusDays(1);
		} else {
			date = null;
		}
		return date;
	}

	/** for testing purpose */
	public static void main(String[] args) {
		// String str = "03 03 2015";
		// Output.showToUser(DateFormatter3(str).toString());

	}
}
