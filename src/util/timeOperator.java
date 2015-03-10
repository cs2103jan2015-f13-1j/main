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
		builder.appendOptional(DateTimeFormatter.ofPattern("MM d"));
		//builder.optionalStart().parseDefaulting(ChronoField.ALIGNED_WEEK_OF_YEAR,10);
		//builder.appendOptional(DateTimeFormatter.ofPattern("EEE"));
		builder.optionalStart().appendValue(ChronoField.HOUR_OF_DAY,1).optionalEnd();
		builder.optionalStart().appendValue(ChronoField.MINUTE_OF_HOUR,1).optionalEnd();
		//builder.optionalStart().appendValue(ChronoField.SECOND_OF_MINUTE,1).optionalEnd();
		builder.optionalStart().parseDefaulting(ChronoField.HOUR_OF_DAY,0);
		DateTimeFormatter dtf = builder.toFormatter()
				.withLocale(Locale.ENGLISH);
		LocalDateTime date = LocalDateTime.parse(str, dtf);
		return date;
	}

	public static void main(String[] args) {
		String str = "mar 4";
		operator.showToUser(getTime(str).toString());
	}
}
