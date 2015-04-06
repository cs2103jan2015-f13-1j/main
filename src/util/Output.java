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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		//System.out.println(log.getHandlers().length);

		log.log(Level.INFO, text);

		count++;
		// TaskDisplayController.updateLabel(text);
	}

	private static class outputFormatter extends Formatter {

		@Override
		public String format(LogRecord record) {
			// StringBuffer sb = new StringBuffer();

			// sb.append(record.getMessage());
			return record.getMessage();
		}
	}
}
