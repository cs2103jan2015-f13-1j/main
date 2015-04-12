package parser;

import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Locale;
import java.util.Scanner;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import logic.Logic;
import util.Output;
import util.Task;
import util.TimeExtractor;
import util.Task.TASK_TYPE;

public class TaskBuilder {
	private String _input, desc;
	Task t;
	private int startOfTimeString;
	private Task.TASK_TYPE inferredType = Task.TASK_TYPE.NULL;
	private Vector<LocalTime> timeList = new Vector<LocalTime>();
	private Vector<LocalDate> dateList = new Vector<LocalDate>();
	private Vector<Integer> timeIndex = new Vector<Integer>();
	private Vector<Integer> dateIndex = new Vector<Integer>();
	private static final String MSG_FORMAT = "Incorrect Format!\n";
	private static final String MSG_DESC = "Task Description cannot be empty!\n";
	private final static Logger log = Logger.getLogger(Logic.class.getName());

	// @author A0105952H
	public TaskBuilder(String s) {
		_input = s;
		startOfTimeString = s.length();
	}

	public Task extractAddCommand() {
		if (!_input.isEmpty()) {
			Task.TASK_TYPE type = checkTaskType();
			extractTaskInfo(type);
			return t;
		} else {
			return null;
		}
	}

	/** switch to respective task building methods */
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
		_input = _input.replaceAll("\"", "").trim();
		if (!_input.isEmpty()) {
			t = new Task(_input);
		} else {
			Output.showToUser(MSG_DESC);
		}
		// TODO
		log.setLevel(Level.OFF);
	}

	private void buildDeadline() {

		if (inferredType == Task.TASK_TYPE.TIMED_TASK) {
			Output.showToUser(MSG_FORMAT);
			return;
		} else {
			LocalDateTime EndTime;

			if (dateList.size() == 0) {
				EndTime = LocalDateTime.of(LocalDate.now(), timeList.get(0));
			} else if (dateList.size() == 1 && timeList.size() == 1) {
				EndTime = LocalDateTime.of(dateList.get(0), timeList.get(0));
			} else if (dateList.size() == 1 && timeList.size() == 0) {
				EndTime = LocalDateTime.of(dateList.get(0),
						LocalTime.of(23, 59));
			} else {
				Output.showToUser(MSG_FORMAT);
				return;
			}
			if (!desc.isEmpty()) {
				t = new Task(desc, EndTime);
			} else {
				Output.showToUser(MSG_DESC);
			}
		}

	}

	private void buildTimedTask() {
		LocalDateTime StartTime, EndTime;

		int tm = timeList.size();
		int d = dateList.size();
		if (d == 2 && tm == 0) {
			log.info(dateList.get(0).toString());
			StartTime = LocalDateTime.of(dateList.get(0), LocalTime.of(0, 0));
			EndTime = LocalDateTime.of(dateList.get(1), LocalTime.of(23, 59));
		} else if (d == 1 && tm == 2) {
			StartTime = LocalDateTime.of(dateList.get(0), timeList.get(0));
			EndTime = LocalDateTime.of(dateList.get(0), timeList.get(1));
		} else if (d == 1 && tm == 0) {
			StartTime = LocalDateTime.of(dateList.get(0), LocalTime.of(0, 0));
			EndTime = LocalDateTime.of(dateList.get(0), LocalTime.of(23, 59));
		} else if (d == 0 && tm == 2) {
			StartTime = LocalDateTime.of(LocalDate.now(), timeList.get(0));
			EndTime = LocalDateTime.of(LocalDate.now(), timeList.get(1));
		} else if (d == 2 && tm == 2) {
			StartTime = LocalDateTime.of(dateList.get(0), timeList.get(0));
			EndTime = LocalDateTime.of(dateList.get(1), timeList.get(1));
		} else {
			Output.showToUser(MSG_FORMAT);
			return;
		}

		try {
			if (!desc.isEmpty()) {
				t = new Task(desc, StartTime, EndTime);
			} else {
				Output.showToUser(MSG_DESC);
			}
		} catch (Exception e) {
			// TODO
			Output.showToUser(MSG_FORMAT);
			log.info(MSG_FORMAT);
		}
	}

	private Task.TASK_TYPE checkTaskType() {

		int startIndexOfDate = -1, startIndexOfTime = -1;
		checkTimePattern();
		checkDatePattern();
		if (dateIndex.size() != 0) {
			startIndexOfDate = dateIndex.get(0);
		}
		if (timeIndex.size() != 0) {
			startIndexOfTime = timeIndex.get(0);
		}

		extractDesc(startIndexOfDate, startIndexOfTime);
		log.info(startIndexOfDate + " " + startIndexOfTime);

		int tm = timeList.size();
		int d = dateList.size();
		log.info(d + " " + tm);

		if (tm == 0 && d == 0) {
			return Task.TASK_TYPE.FLOATING_TASK;
		} else if (tm != 1 && inferredType != Task.TASK_TYPE.DEADLINE) {
			return Task.TASK_TYPE.TIMED_TASK;
		} else {
			return Task.TASK_TYPE.DEADLINE;
		}

	}

	/** extract description of task from input string */
	private void extractDesc(int startIndexOfDate, int startIndexOfTime) {

		int startIndex;

		if (startIndexOfDate < 0 && startIndexOfTime < 0) {
			desc = _input;
		} else if (startIndexOfDate < 0) {
			startIndex = Math.min(startOfTimeString, startIndexOfTime);
			desc = _input.substring(0, startIndex).trim();
		} else if (startIndexOfTime < 0) {
			startIndex = Math.min(startOfTimeString, startIndexOfDate);
			desc = _input.substring(0, startIndex).trim();
		} else {
			startIndex = Math.min(startOfTimeString,
					Math.min(startIndexOfDate, startIndexOfTime));
			desc = _input.substring(0, startIndex).trim();
		}

		desc = desc.replaceAll("\"", "");
	}

	private void checkDatePattern() {

		dateFormat1();
		dateFormat2();
		dateFormat3();
		dateFormat4();
		dateFormat5();

	}

	/** check whether string starting from index i has been parsed */
	private boolean checkDateIndex(int i) {
		if (dateIndex.size() != 0)
			for (int index : dateIndex) {
				if (i == index) {
					return false;
				}
			}
		return true;
	}

	private void extractDate(Matcher m) {
		boolean isNew;
		LocalDate date;
		int i = 0, index = -1, k = 0;
		try {
			while (m.find(k)) {

				if (dateIndex.size() != 0) {
					index = dateIndex.get(0);
				}

				String dt = m.group();
				dt = dt.trim().replaceAll("[\\s/\\-\\.]", " ");
				log.info(dt);
				i = m.start();
				isNew = checkDateIndex(i);

				if (isNew) {
					if (i == 0 || _input.charAt(i - 1) != '\"') {
						date = TimeExtractor.extractDate(dt);
					} else {
						date = null;
					}

					if (date != null) {
						if (index < 0 || i < index) {
							dateList.add(0, date);
							dateIndex.add(0, i);
						} else {
							dateList.add(date);
							dateIndex.add(i);
						}
						if (i > 0) {
							checkTimeIndicationWord(i, false);
						}
						i = m.end() - 1;
						log.info(date.toString());
					}
				}
				k = _input.indexOf(" ", i + 1);
			}
		} catch (Exception e) {
			return;
		}
	}

	/** extract the date format 13 mar 2015, 13-mar-2015, 3/mar/2015, 3 march */
	private void dateFormat1() {

		Matcher m = Pattern.compile(
				"\\b\\d{1,2}[\\s/\\-]\\w+(?![:\\.])([\\s/\\-]\\d{4})?\\b",
				Pattern.CASE_INSENSITIVE).matcher(_input);
		log.info("1:");
		extractDate(m);

	}

	/** extract the date format mar 13 2015, mar-13-2015, mar/3/2015, March.2 */
	private void dateFormat2() {

		Matcher m = Pattern.compile(
				"\\b\\w+[\\s/\\-\\.]\\d{1,2}(?![:\\.])([\\s/\\-]\\d{4})?\\b",
				Pattern.CASE_INSENSITIVE).matcher(_input);
		log.info("2:");
		extractDate(m);
	}

	/** extract date format 3-3-2015,3 3 2015,3/3/2015, 3.3.2015 */
	private void dateFormat3() {

		Matcher m;
		m = Pattern
				.compile(
						"\\b\\d{1,2}+[\\s/\\-.]\\d{1,2}(?![:\\.])([\\s/\\-.]\\d{4})?\\b",
						Pattern.CASE_INSENSITIVE).matcher(_input);
		log.info("3:");
		extractDate(m);
	}

	/** extract date format Monday/Mon */
	private void dateFormat4() {

		String[] dayOfWeek = DateFormatSymbols.getInstance(Locale.ENGLISH)
				.getWeekdays();
		String[] dayOfWeekShort = DateFormatSymbols.getInstance(Locale.ENGLISH)
				.getShortWeekdays();

		extractDayOfWeek(dayOfWeek);
		extractDayOfWeek(dayOfWeekShort);

	}

	/** extract date format tmr, tomorrow, today, tdy */
	private void dateFormat5() {
		int i = -1, index = -1;

		String s;
		LocalDate date = null;
		String[] spcdt = { "tomorrow", "tmr", "today", "tdy" };

		String[] info = _input.split(" ");

		for (int j = 0; j < info.length; j++) {
			s = info[j];
			if (s.equalsIgnoreCase(spcdt[2]) || s.equalsIgnoreCase(spcdt[3])) {
				i = _input.indexOf(s);
				date = LocalDate.now();
			} else if (s.equalsIgnoreCase(spcdt[1])
					|| s.equalsIgnoreCase(spcdt[0])) {
				i = _input.indexOf(" " + s);
				if (i == -1) {
					i = 0;
				}
				date = LocalDate.now().plusDays(1);
			} else {
				i = -1;
			}
			if (dateIndex.size() != 0) {
				index = dateIndex.get(0);
			}

			if (i != -1) {
				if (index < 0 || i < index) {
					dateList.add(0, date);
					dateIndex.add(0, i);
				} else {
					dateList.add(date);
					dateIndex.add(i);
				}
				log.info(date.toString());
				if (i > 0) {
					checkTimeIndicationWord(i, false);
				}
			}
		}
	}

	private void extractDayOfWeek(String[] dayOfWeek) {
		String s;
		String[] info = _input.split(" ");
		int i, index = -1;
		for (String d : dayOfWeek) {

			for (int j = 0; j < info.length; j++) {
				s = info[j];
				if (s.equalsIgnoreCase(d)) {
					if (j > 1 && info[j - 1].equalsIgnoreCase("next")) {
						i = _input.indexOf(info[j - 1]);
					} else {
						i = _input.indexOf(" " + s);
					}
					if (dateIndex.size() != 0) {
						index = dateIndex.get(0);
					}

					LocalDate date = TimeExtractor.extractDate(s);
					// TODO
					if (i < 0) {
						i = 0;
					}

					if (index < 0 || i < index) {
						dateList.add(0, date);
						dateIndex.add(0, i);
					} else {
						dateList.add(date);
						dateIndex.add(i);
					}
					if (i > 0) {
						checkTimeIndicationWord(i, false);
					}
					log.info(s + " " + date.toString());
				}
			}
		}
	}

	private void checkTimePattern() {

		timePattern1();
		timePattern2();

	}

	/** extract time format hh:mm, eg.12:00 */
	private void timePattern1() {

		Matcher m = Pattern.compile("\\b\\d{1,2}:\\d{2}\\b").matcher(_input);
		log.info("tm 1:");
		extractTime(m);
	}

	/** extract time pattern h.m 3pm/2.15pm/3 pm */
	private void timePattern2() {

		Matcher m;
		m = Pattern.compile("\\b\\d{1,2}([.:]\\d{1,2})?\\s*[ap]m\\s*",
				Pattern.CASE_INSENSITIVE).matcher(_input);
		log.info("tm 2:");
		extractTime(m);
	}

	private void extractTime(Matcher m) {
		try {
			int i, index = -1;
			LocalTime time;
			while (m.find()) {
				i = m.start();
				if (i == 0 || _input.charAt(i - 1) != '\"') {
					time = TimeExtractor
							.extractTime(m.group().replace(" ", ""));
				} else {
					time = null;
				}
				log.info(m.group());

				if (time != null) {
					if (timeIndex.size() != 0) {
						index = timeIndex.get(0);
					}
					if (index < 0 || i < index) {
						timeList.add(0, time);
						timeIndex.add(0, i);
					} else {
						timeList.add(time);
						timeIndex.add(i);
					}
					log.info(time.toString());
					if (i > 0) {
						checkTimeIndicationWord(i, true);
					}
				}

			}
		} catch (Exception e) {
			Output.showToUser(MSG_DESC);
		}
	}

	/** check for existence of time indication word */
	private void checkTimeIndicationWord(int i, boolean isTime) {
		int startIndex = _input.length();

		if (isTime && _input.substring(0, i).trim().endsWith(" at")) {
			startIndex = _input.substring(0, i).lastIndexOf("at");
		} else if (!isTime && _input.substring(0, i).trim().endsWith(" on")) {
			startIndex = _input.substring(0, i).lastIndexOf("on");
		} else if (_input.substring(0, i).trim().endsWith(" until")) {
			startIndex = _input.substring(0, i).lastIndexOf("until");
		} else if (_input.substring(0, i).trim().endsWith(" till")) {
			startIndex = _input.substring(0, i).lastIndexOf("till");
		} else if (_input.substring(0, i).trim().endsWith(" from")) {
			startIndex = _input.substring(0, i).lastIndexOf("from");
			inferredType = Task.TASK_TYPE.TIMED_TASK;
		} else if (_input.substring(0, i).trim().endsWith(" to")) {
			startIndex = _input.substring(0, i).lastIndexOf("to");
			inferredType = Task.TASK_TYPE.TIMED_TASK;
		} else if (_input.substring(0, i).trim().endsWith(" by")) {
			startIndex = _input.substring(0, i).lastIndexOf("by");
			inferredType = Task.TASK_TYPE.DEADLINE;
		}

		if (startIndex < startOfTimeString) {
			startOfTimeString = startIndex;
		}
	}

	public static void main(String[] args) {
		TaskBuilder tb = new TaskBuilder(args.toString());
		tb.run();

	}

	public void run() {

		//_input = "mar 3";
		Task t = extractAddCommand();
		// clear list
		// Scanner sc = new Scanner(System.in);
		// _input = sc.nextLine();
		// while (!_input.contains(new String("exit"))) {
		// Task t = extractAddCommand();
		//displayTask(t);
		// _input = sc.nextLine();
		// }
		// sc.close();

	}

	private void displayTask(Task t) {
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
