package parser;

import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Locale;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import util.Output;
import util.Task;
import util.TimeExtractor;
import util.Task.TASK_TYPE;

public class TaskBuilder {
	private String _input, desc;
	private Task t;
	private Vector<LocalTime> timeList = new Vector<LocalTime>();
	private Vector<LocalDate> dateList = new Vector<LocalDate>();
	private static final String MSG_FORMAT = "Incorrect Format!\n";

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
			Output.showToUser(e.getMessage());
		}
	}
	

	private Task.TASK_TYPE checkTaskType() {

		int i = checkTimePattern();
		int j = checkDatePattern();

		extractDesc(i, j);
		Output.showToUser(i + " " + j);

		int tm = timeList.size();
		int d = dateList.size();
		Output.showToUser(tm + " " + d);

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
		desc = removeIndicationWords(desc);
	}

	private String removeIndicationWords(String desc) {

		if (desc.toLowerCase().endsWith("\\bfrom")) {
			desc = desc.substring(0, desc.length() - 4);
		}
		if (desc.toLowerCase().endsWith("\\bby")) {
			desc = desc.substring(0, desc.length() - 2);
		}
		if (desc.endsWith("\\bat")) {
			desc = desc.substring(0, desc.length() - 2);
		}
		return desc;
	}

	private int checkDatePattern() {
		int index = -1;
		index = dateFormat1(index);
		index = dateFormat2(index);
		index = dateFormat3(index);
		index = dateFormat4(index);
		index = dateFormat5(index);
		index = dateFormat6(index);
		
		return index;
	}

	//extract the date format mar 13 2015, mar-13-2015, mar/3/2015
	private int dateFormat1(int index) {
		int i;
		Matcher m = Pattern.compile(
				"[^\"]\\w+[\\s/\\-]\\d{1,2}([\\s/\\-]\\d{4})?\\b",
				Pattern.CASE_INSENSITIVE).matcher(_input);
		while (m.find()) {

			String dt = m.group();
			dt = dt.trim().replaceAll("[\\s/\\-]", " ");
			Output.showToUser(dt);
			LocalDate date = TimeExtractor.extractDate(dt);
			i = m.start();
			if (date != null) {
				if (index < 0 || i < index) {
					dateList.add(0, date);
					index = i;
				} else {
					dateList.add(date);
				}
				Output.showToUser(date.toString());
			}
		}
		return index;
	}

	//extract date format 3-3-2015,3 3 2015,3/3/2015
	private int dateFormat2(int index) {
		int i;
		Matcher m;
		m = Pattern.compile(
				"[^\"]\\d{1,2}+[\\s/\\-]\\d{1,2}([\\s/\\-]\\d{4})?\\b",
				Pattern.CASE_INSENSITIVE).matcher(_input);
		while (m.find()) {

			String dt = m.group();
			dt = dt.trim().replaceAll("[\\s/\\-]", " ");
			Output.showToUser(dt);
			LocalDate date = TimeExtractor.extractDate(dt);
			i = m.start();
			if (date != null) {
				if (index < 0 || i < index) {
					dateList.add(0, date);
					index = i;
				} else {
					dateList.add(date);
				}
				Output.showToUser(date.toString());
			}

		}
		return index;
	}

	//extract date format monday
	private int dateFormat3(int index) {
		int i;
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
						index = i;
					} else {
						dateList.add(date);
					}

					Output.showToUser(date.toString());
				}
			}
		}

		return index;
	}

	//extract date format mon
	private int dateFormat4(int index) {
		int i;
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
						index = i;
					} else {
						dateList.add(date);
					}

					Output.showToUser(date.toString());
				}
			}
		}

		return index;
	}

	//extract tmr, tomorrow, today
	private int dateFormat5(int index) {
		int i;
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
					index = i;
				} else {
					dateList.add(date);
				}
				Output.showToUser(date.toString());
			}
		}

		return index;
	}

	//extract the date format mar 13 2015, mar-13-2015, mar/3/2015
		private int dateFormat6(int index) {
			int i;
			Matcher m = Pattern.compile(
					"[^\"]\\d{1,2}[\\s/\\-]\\w+([\\s/\\-]\\d{4})?\\b",
					Pattern.CASE_INSENSITIVE).matcher(_input);
			while (m.find()) {

				String dt = m.group();
				dt = dt.trim().replaceAll("[\\s/\\-]", " ");
				Output.showToUser(dt);
				LocalDate date = TimeExtractor.extractDate(dt);
				i = m.start();
				if (date != null) {
					if (index < 0 || i < index) {
						dateList.add(0, date);
						index = i;
					} else {
						dateList.add(date);
					}
					Output.showToUser(date.toString());
				}
			}
			return index;
		}
	
	private int checkTimePattern() {
		int index = -1;

		index = timePattern1(index);
		index = timePattern2(index);
		index = timePattern3(index);

		return index;
	}

	private int timePattern1(int index) {
		int i;
		Matcher m = Pattern.compile("[^\"]\\b\\d{1,2}:\\d{2}\\b").matcher(
				_input);
		while (m.find()) {
			LocalTime time = TimeExtractor.extractTime(m.group().trim());
			i = m.start();
			if (index < 0 || i < index) {
				timeList.add(0, time);
				index = i;
			} else {
				timeList.add(time);
			}
			Output.showToUser(time.toString());
		}
		return index;
	}

	// extract time pattern 3pm/2.15pm
	private int timePattern2(int index) {
		int i;
		Matcher m;
		m = Pattern.compile("[^\"]\\b\\d{1,2}\\s*[ap]m\\b",
				Pattern.CASE_INSENSITIVE).matcher(_input);
		while (m.find()) {
			LocalTime time = TimeExtractor.extractTime(m.group().replace(" ",
					""));
			i = m.start();
			if (index < 0 || i < index) {
				timeList.add(0, time);
				index = i;
			} else {
				timeList.add(time);
			}
			Output.showToUser(time.toString());
		}
		return index;
	}

	private int timePattern3(int index) {
		int i;
		Matcher m;
		m = Pattern.compile("[^\"]\\d{3,4}\\b", Pattern.CASE_INSENSITIVE)
				.matcher(_input);
		while (m.find()) {
			LocalTime time = TimeExtractor.extractTime(m.group().replace(" ",
					""));
			i = m.start();
			if (index < 0 || i < index) {
				timeList.add(0, time);
				index = i;
			} else {
				timeList.add(time);
			}
			Output.showToUser(time.toString());
		}
		return index;
	}

	public static void main(String[] args) {
		TaskBuilder tb = new TaskBuilder("Running w/ Pat 1 apr");
		tb.run();

	}

	public void run() {
		Task t = extractAddCommand();
		Output.showToUser(t.getTaskType().toString());
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
		Output.showToUser(text);
	}

}
