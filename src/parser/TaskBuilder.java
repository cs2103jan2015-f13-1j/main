package parser;

import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Locale;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import logic.Logic;
import util.Task;
import util.TimeExtractor;
import util.Task.TASK_TYPE;

public class TaskBuilder {
	private String _input, desc;
	Task t;
	private Vector<LocalTime> timeList = new Vector<LocalTime>();
	private Vector<LocalDate> dateList = new Vector<LocalDate>();
	private Vector<Integer> timeIndex = new Vector<Integer>();
	private Vector<Integer> dateIndex = new Vector<Integer>();
	private static final String MSG_FORMAT = "Incorrect Format!\n";
	private final static Logger log = Logger.getLogger(Logic.class.getName());

	public TaskBuilder(String s) {
		_input = s;
	}

	public Task extractAddCommand() {
		Task.TASK_TYPE type = checkTaskType();
		extractTaskInfo(type);
		return t;
	}

	private void extractTaskInfo(Task.TASK_TYPE type) {
		switch (type) {
		case TIMED_TASK:
			buildTimedTask();
			return;
		case DEADLINE:
			buildDeadline();
			return;
		case FLOATING_TASK:
			buildFloatingTask();
			return;
		default:
			break;
		}
	}

	private void buildFloatingTask() {
		t = new Task(_input.trim());
		log.setLevel(Level.INFO);
	}

	private void buildDeadline() {
		LocalDateTime EndTime;

		if (dateList.size() == 0) {
			EndTime = LocalDateTime.of(LocalDate.now(), timeList.get(0));
		} else if (dateList.size() == 1) {
			EndTime = LocalDateTime.of(dateList.get(0), timeList.get(0));
		} else {
			throw new Error(MSG_FORMAT);
		}

		t = new Task(desc, EndTime);

	}

	private void buildTimedTask() {
		LocalDateTime StartTime, EndTime;

		int tm = timeList.size();
		int d = dateList.size();
		if (d == 2 && tm == 0) {
			StartTime = LocalDateTime.of(dateList.get(0), LocalTime.of(0, 0));
			EndTime = LocalDateTime.of(dateList.get(1), LocalTime.of(23, 59));
		} else if (d == 1 && tm == 2) {
			StartTime = LocalDateTime.of(dateList.get(0), timeList.get(0));
			EndTime = LocalDateTime.of(dateList.get(0), timeList.get(1));
		} else if (d == 1 && tm == 0) {
			StartTime = LocalDateTime.of(dateList.get(0), LocalTime.of(0, 0));
			EndTime = LocalDateTime.of(dateList.get(0), LocalTime.of(23, 59));
		} else if (d == 2 && tm == 2) {
			StartTime = LocalDateTime.of(dateList.get(0), timeList.get(0));
			EndTime = LocalDateTime.of(dateList.get(1), timeList.get(1));
		} else {
			throw new Error(MSG_FORMAT);
		}

		try {
			t = new Task(desc, StartTime, EndTime);
		} catch (Exception e) {
			log.info(e.getMessage());
		}
	}

	private Task.TASK_TYPE checkTaskType() {

		int i = -1, j = -1;
		checkTimePattern();
		checkDatePattern();
		if (dateIndex.size() != 0) {
			i = dateIndex.get(0);
		}
		if (timeIndex.size() != 0) {
			j = timeIndex.get(0);
		}

		extractDesc(i, j);
		log.info(i + " " + j);

		int tm = timeList.size();
		int d = dateList.size();
		log.info(d + " " + tm);

		if (tm == 0 && d == 0) {
			return Task.TASK_TYPE.FLOATING_TASK;
		} else if (tm != 1) {
			return Task.TASK_TYPE.TIMED_TASK;
		} else {
			return Task.TASK_TYPE.DEADLINE;
		}

	}

	private void extractDesc(int i, int j) {

		if (i < 0 && j < 0) {
			buildFloatingTask();
			return;
		} else if (i < 0) {
			desc = _input.substring(0, j).trim();
		} else if (j < 0) {
			desc = _input.substring(0, i).trim();
		} else {
			desc = _input.substring(0, Math.min(i, j)).trim();
		}

		desc = desc.replaceAll("\"", "");
		desc = removeIndicationWords();
	}

	private String removeIndicationWords() {

		if (desc.toLowerCase().endsWith(" from\\b")) {
			desc = desc.substring(0, desc.length() - 4).trim();
		}
		if (desc.toLowerCase().endsWith(" by")) {
			desc = desc.substring(0, desc.length() - 2).trim();
		}
		if (desc.endsWith(" at")) {
			desc = desc.substring(0, desc.length() - 2).trim();
		}
		return desc;
	}

	private void checkDatePattern() {

		dateFormat1();
		dateFormat2();
		dateFormat3();
		dateFormat4();
		dateFormat5();
		dateFormat6();

	}

	private boolean checkDateIndex(int i) {
		if (dateIndex.size() != 0)
			for (int index : dateIndex) {
				if (i == index) {
					return false;
				}
			}
		return true;
	}

	// extract the date format mar 13 2015, mar-13-2015, mar/3/2015
	private int dateFormat1() {
		int i, index = -1;
		boolean isNew;
		if (dateIndex.size() != 0) {
			index = dateIndex.get(0);
		}
		Matcher m = Pattern.compile(
				"[^\"]\\w+[\\s/\\-]\\d{1,2}([\\s/\\-]\\d{4})?\\b",
				Pattern.CASE_INSENSITIVE).matcher(_input);
		while (m.find()) {

			String dt = m.group();
			dt = dt.trim().replaceAll("[\\s/\\-]", " ");
			log.info(dt);
			i = m.start();
			isNew = checkDateIndex(i);

			if (isNew) {
				LocalDate date = TimeExtractor.extractDate(dt);

				if (date != null) {
					if (index < 0 || i < index) {
						dateList.add(0, date);
						dateIndex.add(0, i);
					} else {
						dateList.add(date);
					}
					log.info("1:" + date.toString());
				}
			}
		}
		return index;
	}

	// extract date format 3-3-2015,3 3 2015,3/3/2015
	private int dateFormat2() {
		int i, index = -1;
		boolean isNew;
		if (dateIndex.size() != 0) {
			index = dateIndex.get(0);
		}

		Matcher m;
		m = Pattern.compile(
				"[^\"]\\d{1,2}+[\\s/\\-]\\d{1,2}([\\s/\\-]\\d{4})?\\b",
				Pattern.CASE_INSENSITIVE).matcher(_input);
		while (m.find()) {

			String dt = m.group();
			dt = dt.trim().replaceAll("[\\s/\\-]", " ");
			log.info(dt);
			i = m.start();
			isNew = checkDateIndex(i);

			if (isNew) {
				LocalDate date = TimeExtractor.extractDate(dt);

				if (date != null) {
					if (index < 0 || i < index) {
						dateList.add(0, date);
						dateIndex.add(0, i);
					} else {
						dateList.add(date);
						dateIndex.add(i);
					}
					log.info("2:" + date.toString());
				}
			}
		}
		return index;
	}

	// extract the date format mar 13 2015, mar-13-2015, mar/3/2015
	private int dateFormat3() {
		int i, index = -1;
		boolean isNew;
		if (dateIndex.size() != 0) {
			index = dateIndex.get(0);
		}
		Matcher m = Pattern.compile(
				"[^\"]\\d{1,2}[\\s/\\-]\\w+([\\s/\\-]\\d{4})?\\b",
				Pattern.CASE_INSENSITIVE).matcher(_input);
		while (m.find()) {

			String dt = m.group();
			dt = dt.trim().replaceAll("[\\s/\\-]", " ");
			log.info(dt);
			i = m.start();
			isNew = checkDateIndex(i);

			if (isNew) {
				LocalDate date = TimeExtractor.extractDate(dt);
				if (date != null) {
					if (index < 0 || i < index) {
						dateList.add(0, date);
						dateIndex.add(0, i);
					} else {
						dateList.add(date);
						dateIndex.add(i);
					}
					log.info(date.toString());
				}
			}
		}
		return index;
	}

	// extract date format monday
	private int dateFormat4() {
		int i, index = -1;
		if (dateIndex.size() != 0) {
			index = dateIndex.get(0);
		}

		String s;
		String[] wds = DateFormatSymbols.getInstance(Locale.ENGLISH)
				.getWeekdays();

		String[] info = _input.split(" ");

		for (String d : wds) {
			for (int j = 0; j < info.length; j++) {
				s = info[j];
				if (s.equalsIgnoreCase(d)) {
					if (j > 1 && info[j - 1].equalsIgnoreCase("next")) {
						i = _input.indexOf(info[j - 1]);
					} else {
						i = _input.indexOf(s);
					}

					LocalDate date = TimeExtractor.extractDate(s);
					if (index < 0 || i < index) {
						dateList.add(0, date);
						dateIndex.add(0, i);
					} else {
						dateList.add(date);
						dateIndex.add(i);
					}

					log.info("3:" + date.toString());
				}
			}
		}

		return index;
	}

	// extract date format mon
	private int dateFormat5() {
		int i, index = -1;
		if (dateIndex.size() != 0) {
			index = dateIndex.get(0);
		}
		String s;
		String[] wds = DateFormatSymbols.getInstance(Locale.ENGLISH)
				.getShortWeekdays();

		String[] info = _input.split(" ");

		for (String d : wds) {
			for (int j = 0; j < info.length; j++) {
				s = info[j];
				if (s.equalsIgnoreCase(d)) {
					if (j > 1 && info[j - 1].equalsIgnoreCase("next")) {
						i = _input.indexOf(info[j - 1]);
					} else {
						i = _input.indexOf(s);
					}

					LocalDate date = TimeExtractor.extractDate(s);
					if (index < 0 || i < index) {
						dateList.add(0, date);
						dateIndex.add(0, i);
					} else {
						dateList.add(date);
						dateIndex.add(i);
					}

					log.info("4:" + date.toString());
				}
			}
		}
		return index;
	}

	// extract tmr, tomorrow, today
	private int dateFormat6() {
		int i, index = -1;
		if (dateIndex.size() != 0) {
			index = dateIndex.get(0);
		}
		String s;
		LocalDate date = null;
		String[] spcdt = { "tomorrow", "tmr", "today" };

		String[] info = _input.split(" ");

		for (int j = 0; j < info.length; j++) {
			s = info[j];
			if (s.equalsIgnoreCase(spcdt[2])) {
				i = _input.indexOf(s);
				date = LocalDate.now();
			} else if (s.equalsIgnoreCase(spcdt[1])
					|| s.equalsIgnoreCase(spcdt[0])) {
				i = _input.indexOf(s);
				date = LocalDate.now().plusDays(1);
			} else {
				i = -1;
			}

			if (i != -1) {
				if (index < 0 || i < index) {
					dateList.add(0, date);
					dateIndex.add(0, i);
				} else {
					dateList.add(date);
					dateIndex.add(i);
				}
				log.info("5:" + date.toString());
			}
		}

		return index;
	}

	private void checkTimePattern() {

		timePattern1();
		timePattern2();
		timePattern3();

	}

	private int timePattern1() {
		int i, index = -1;
		if (timeIndex.size() != 0) {
			index = timeIndex.get(0);
		}
		Matcher m = Pattern.compile("[^\"]\\b\\d{1,2}:\\d{2}\\b").matcher(
				_input);
		while (m.find()) {
			LocalTime time = TimeExtractor.extractTime(m.group().trim());
			i = m.start();
			if (time != null) {
				if (index < 0 || i < index) {
					timeList.add(0, time);
					timeIndex.add(0, i);
				} else {
					timeList.add(time);
					timeIndex.add(i);
				}
			}
			log.info(time.toString());
		}
		return index;
	}

	// extract time pattern 3pm/2.15pm
	private int timePattern2() {
		int i, index = -1;
		if (timeIndex.size() != 0) {
			index = timeIndex.get(0);
		}
		Matcher m;
		m = Pattern.compile("[^\"]\\b\\d{1,2}\\s*[ap]m\\b",
				Pattern.CASE_INSENSITIVE).matcher(_input);
		while (m.find()) {
			LocalTime time = TimeExtractor.extractTime(m.group().replace(" ",
					""));
			i = m.start();
			if (time != null) {
				if (index < 0 || i < index) {
					timeList.add(0, time);
					timeIndex.add(0, i);
				} else {
					timeList.add(time);
					timeIndex.add(i);
				}
			}
			log.info(time.toString());
		}
		return index;
	}

	private int timePattern3() {
		int i, index = -1;
		if (timeIndex.size() != 0) {
			index = timeIndex.get(0);
		}
		Matcher m;
		m = Pattern.compile("[^\"]\\d{3,4}\\b", Pattern.CASE_INSENSITIVE)
				.matcher(_input);
		while (m.find()) {
			LocalTime time = TimeExtractor.extractTime(m.group().replace(" ",
					""));
			i = m.start();
			if (index < 0 || i < index) {
				timeList.add(0, time);
				timeIndex.add(0, i);
			} else {
				timeList.add(time);
				timeIndex.add(i);
			}
			log.info(time.toString());
		}
		return index;
	}

	public static void main(String[] args) {
		TaskBuilder tb = new TaskBuilder(args.toString());
		tb.run();

	}

	public void run() {
		// _input = "National Conference in Atlanta 9/23 - 9/26";
		Task t = extractAddCommand();
		log.info(t.getTaskType().toString());
		String text;
		if (t.getTaskType().equals(TASK_TYPE.TIMED_TASK)) {
			text = new String(t.getTaskDesc() + "\nFrom: "
					+ TimeExtractor.formatDateTime(t.getStartTime()) + " To: "
					+ TimeExtractor.formatDateTime(t.getEndTime()));

		} else if (t.getTaskType().equals(TASK_TYPE.DEADLINE)) {
			text = new String(t.getTaskDesc() + "\nBy: "
					+ TimeExtractor.formatDateTime(t.getEndTime()));
		} else {
			text = new String(t.getTaskDesc() + "\n");
		}
		log.info(text);
	}

}
