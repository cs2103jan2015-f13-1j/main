package util;

import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

public class Output {

	public static void showToUser(String text) {
		final Logger log = Logger.getLogger("");
		StreamHandler handler;
		try {
			handler = new ConsoleHandler();
			handler.setFormatter(new outputFormatter());
			log.addHandler(handler);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		log.setLevel(Level.INFO);

		// System.out.println(text);
		log.log(Level.INFO, text);

		// TaskDisplayController.updateLabel(text);
	}

	private static class outputFormatter extends Formatter {

		@Override
		public String format(LogRecord record) {
			StringBuffer sb = new StringBuffer();
			sb.append(record.getMessage());
			return sb.toString();
		}
	}
}
