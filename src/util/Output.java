package util;

import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

public class Output {

	private static int count = 0;
	private static Logger log;

	// @author A0105952H
	public static void showToUser(String text) {

		if (count == 0) {
			log = Logger.getLogger("");
			StreamHandler handler;
			try {
				handler = new ConsoleHandler();
				handler.setFormatter(new outputFormatter());
				log.addHandler(handler);
				log.setUseParentHandlers(false);
				log.setLevel(Level.INFO);
				log.removeHandler(log.getHandlers()[0]);
			} catch (SecurityException e) {
				e.printStackTrace();
			}
		}

		log.log(Level.INFO, text);

		count++;
	}

	private static class outputFormatter extends Formatter {

		@Override
		public String format(LogRecord record) {
			return record.getMessage();
		}
	}
}
