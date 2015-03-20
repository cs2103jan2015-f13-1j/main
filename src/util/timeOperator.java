package util;

import java.time.*;
import java.time.format.*;
import java.time.temporal.*;
import java.util.Locale;

public class timeOperator {

	public static String formatDateTime(LocalDateTime t) {
		DateTimeFormatter formatter;
		
		if (t.getMinute() == 0) {
			formatter = DateTimeFormatter.ofPattern("MMM d ha").withLocale(
					Locale.ENGLISH);
		}
		else{
			formatter = DateTimeFormatter.ofPattern("MMM d h.ma").withLocale(
					Locale.ENGLISH);
		}
		return t.format(formatter);
	}

	public static LocalDateTime getTime(String str) {
		DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
		builder.parseDefaulting(ChronoField.YEAR, 2015);
		builder.parseCaseInsensitive();
		builder.appendOptional(DateTimeFormatter.ofPattern("dd MM uuuu HHmm"));
		builder.appendOptional(DateTimeFormatter.ofPattern("dd-MM-uuuu HHmm"));
		builder.appendOptional(DateTimeFormatter.ofPattern("MMM d"));
		builder.appendOptional(DateTimeFormatter.ofPattern("M d"));
		// builder.optionalStart().parseDefaulting(ChronoField.ALIGNED_WEEK_OF_YEAR,10);
		// builder.appendOptional(DateTimeFormatter.ofPattern("EEE"));
		builder.optionalStart().appendValue(ChronoField.HOUR_OF_DAY, 1)
				.optionalEnd();
		builder.optionalStart().appendValue(ChronoField.MINUTE_OF_HOUR, 1)
				.optionalEnd();
		// builder.optionalStart().appendValue(ChronoField.SECOND_OF_MINUTE,1).optionalEnd();
		builder.optionalStart().parseDefaulting(ChronoField.HOUR_OF_DAY, 0);
		DateTimeFormatter dtf = builder.toFormatter()
				.withLocale(Locale.ENGLISH);
		LocalDateTime date = LocalDateTime.parse(str, dtf);
		return date;
	}

	public static LocalTime extractTime(String str) {
		DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
		builder.parseCaseInsensitive();
		builder.appendOptional(DateTimeFormatter.ofPattern("HHmm"));
		builder.appendOptional(DateTimeFormatter.ofPattern("HH mm"));
		builder.appendOptional(DateTimeFormatter.ofPattern("HH:mm"));
		builder.appendOptional(DateTimeFormatter.ofPattern("ha"));
		builder.appendOptional(DateTimeFormatter.ofPattern("h.ma"));
		DateTimeFormatter dtf = builder.toFormatter()
				.withLocale(Locale.ENGLISH);
		LocalTime time = LocalTime.parse(str, dtf);
		return time;
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
			builder.appendOptional(DateTimeFormatter.ofPattern("M d"));
			builder.appendOptional(DateTimeFormatter.ofPattern("M.d"));
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
			builder.appendOptional(DateTimeFormatter.ofPattern("dd MM uuuu"));
			builder.appendOptional(DateTimeFormatter.ofPattern("dd-MM-uuuu"));
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
			builder.appendOptional(DateTimeFormatter.ofPattern("eeee"));
			builder.appendOptional(DateTimeFormatter.ofPattern("E"));

			DateTimeFormatter dtf = builder.toFormatter().withLocale(
					Locale.ENGLISH);
			LocalDate date = LocalDate.parse(str, dtf);
			int d = date.getDayOfYear() - LocalDate.now().getDayOfYear();
			if (d < 0) {
				date = date.withDayOfYear(((-d) / 7 + 1) * 7
						+ date.getDayOfYear());
			}
			return date;
		} catch (Exception e) {
			return null;
		}
	}

	public static void main(String[] args) {
		String str = "monday";
		operator.showToUser(extractDate(str).toString());

	}
}
