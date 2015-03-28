package util;

import java.time.*;
import java.time.format.*;
import java.time.temporal.*;
import java.util.Locale;

public class TimeExtractor {

	public static String formatDateTime(LocalDateTime t) {
		DateTimeFormatter formatter;

		if (t.getMinute() == 0) {
			formatter = DateTimeFormatter.ofPattern("MMM d ha uu").withLocale(
					Locale.ENGLISH);
		} else {
			formatter = DateTimeFormatter.ofPattern("MMM d h.ma uu").withLocale(
					Locale.ENGLISH);
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
		return date;
	}

	private static LocalDate DateFormatter1(String str) {
		try {
			DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
			builder.parseCaseInsensitive();
			builder.parseDefaulting(ChronoField.YEAR, LocalDate.now().getYear());
			builder.appendOptional(DateTimeFormatter.ofPattern("MMM d"));
			builder.appendOptional(DateTimeFormatter.ofPattern("MMMM d"));
			builder.appendOptional(DateTimeFormatter.ofPattern("M d"));
			builder.appendOptional(DateTimeFormatter.ofPattern("M.d"));
			builder.appendOptional(DateTimeFormatter.ofPattern("d MMM"));
			builder.appendOptional(DateTimeFormatter.ofPattern("d MMMM"));
			builder.appendOptional(DateTimeFormatter.ofPattern("d M"));
			DateTimeFormatter dtf = builder.toFormatter().withLocale(
					Locale.ENGLISH);
			LocalDate date = LocalDate.parse(str, dtf);
			if (date.getDayOfYear() < LocalDate.now().getDayOfYear()) {
				date = date.withYear(2016);
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
			builder.appendOptional(DateTimeFormatter.ofPattern("d M uuuu"));
			builder.appendOptional(DateTimeFormatter.ofPattern("dd MM uuuu"));
			builder.appendOptional(DateTimeFormatter.ofPattern("dd-MM-uuuu"));
			builder.appendOptional(DateTimeFormatter.ofPattern("MMM d uuuu"));
			DateTimeFormatter dtf = builder.toFormatter().withLocale(
					Locale.ENGLISH);
			LocalDate date = LocalDate.parse(str, dtf);
			return date;
		} catch (Exception e) {
			return null;
		}
	}

	private static LocalDate DateFormatter3(String str) {
		try {
			DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
			builder.parseCaseInsensitive();
			builder.parseDefaulting(ChronoField.YEAR, LocalDate.now().getYear());
			builder.optionalStart().parseDefaulting(ChronoField.MONTH_OF_YEAR,
					LocalDate.now().getMonthValue());
			builder.optionalStart().parseDefaulting(
					ChronoField.ALIGNED_WEEK_OF_MONTH, 1);
			builder.appendOptional(DateTimeFormatter.ofPattern("EEE"));
			builder.appendOptional(DateTimeFormatter.ofPattern("EEEE"));
			DateTimeFormatter dtf = builder.toFormatter().withLocale(
					Locale.ENGLISH);
			LocalDate date = LocalDate.parse(str, dtf);
			DayOfWeek day = date.getDayOfWeek();
			date = LocalDate.now().with(TemporalAdjusters.next(day));
			return date;
		} catch (Exception e) {
			return null;
		}
	}

	public static void main(String[] args) {
		String str = "fri";
		Output.showToUser(DateFormatter3(str).toString());

	}
}
