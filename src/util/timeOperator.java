package util;

import java.time.*;
import java.time.format.*;
import java.time.temporal.*;
import java.util.Locale;

public class timeOperator {
	static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d")
			.withLocale(Locale.ENGLISH);

	public static LocalDateTime getTime(String str) {
		DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
		builder.parseDefaulting(ChronoField.YEAR, 2015);
		builder.parseCaseInsensitive();
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
		int d;
		DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
		builder.parseCaseInsensitive();
		builder.parseDefaulting(ChronoField.YEAR, LocalDate.now().getYear());
		builder.appendOptional(DateTimeFormatter.ofPattern("MMM d"));
		builder.appendOptional(DateTimeFormatter.ofPattern("M d"));
		builder.appendOptional(DateTimeFormatter.ofPattern("M.d"));
		builder.optionalStart().parseDefaulting(ChronoField.MONTH_OF_YEAR,
				LocalDate.now().getMonthValue());
		builder.optionalStart().parseDefaulting(
				ChronoField.ALIGNED_WEEK_OF_MONTH, 1);
		builder.appendOptional(DateTimeFormatter.ofPattern("E"));
		builder.appendOptional(DateTimeFormatter.ofPattern("eeee"));
		DateTimeFormatter dtf = builder.toFormatter()
				.withLocale(Locale.ENGLISH);
		LocalDate date = LocalDate.parse(str, dtf);
		d = date.getDayOfYear() - LocalDate.now().getDayOfYear();
		if (d < 0) {
			date = date.withDayOfYear(((-d) / 7 + 1) * 7 + date.getDayOfYear());
		} else if (date.getDayOfYear() < LocalDate.now().getDayOfYear()) {
			date = date.withYear(2016);
		}// if(){}
		return date;
	}

	public static void main(String[] args) {
		String str = "440";
		operator.showToUser(extractTime(str).toString());
	}
}
