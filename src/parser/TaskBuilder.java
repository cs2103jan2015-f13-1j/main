package parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import util.Output;
import util.Task;
import util.TimeExtractor;

public class TaskBuilder {
	private String _input;
	private Task t;
	private Vector<LocalTime> timeList = new Vector<LocalTime>();
	private Vector<LocalDate> dateList = new Vector<LocalDate>();

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
		String[] info = _input.split("by");
		String desc = info[0].trim();
		LocalDateTime EndTime = extractTime(info[1].trim());
		t = new Task(desc, EndTime);

	}

	private void buildTimedTask() {
		try {
			String[] info = _input.split("from |to ");
			String desc = info[0].trim();
			LocalDateTime StartTime = extractTime(info[1].trim());
			LocalDateTime EndTime = extractTime(info[2].trim());
			t = new Task(desc, StartTime, EndTime);
		} catch (Exception e) {
		}
	}

	private LocalDateTime extractTime(String str) {
		try {
			return TimeExtractor.getTime(str);
		} catch (Exception e) {
			// return timeOperator.extractDate(str);
			return null;
		}
	}

	private Task.TASK_TYPE checkTaskType() {

		int i = checkTimePattern();
		int j = checkDatePattern();

		extractDesc(i, j);
		Output.showToUser(i+" "+j);

		if (_input.toLowerCase().contains(new String("by"))) {
			return Task.TASK_TYPE.DEADLINE;
		} else if (_input.toLowerCase().contains(new String("from"))) {
			return Task.TASK_TYPE.TIMED_TASK;
		} else {
			return Task.TASK_TYPE.FLOATING_TASK;
		}

	}

	private void extractDesc(int i, int j) {
		String desc;
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

		desc = removeIndicationWords(desc);
	}

	private String removeIndicationWords(String desc) {

		if (desc.endsWith("from")) {
			desc.toLowerCase().replaceAll("from$", "");
		}
		if (desc.endsWith("by")) {
			desc.toLowerCase().replaceAll("by$", "");
		}
		if (desc.endsWith("at")) {
			desc.toLowerCase().replaceAll("at$", "");
		}
		return desc;
	}

	private int checkDatePattern() {
		int index = -1, i;
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
					dateList.add(date);
					index = i;
				} else {
					dateList.add(1, date);
				}
				Output.showToUser(date.toString());
			}
		}

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
					dateList.add(date);
					index = i;
				} else {
					dateList.add(1, date);
				}
				Output.showToUser(date.toString());
			}

		}
		return index;
	}

	private int checkTimePattern() {
		int index = -1, i;
		Matcher m = Pattern.compile("[^\"]\\b\\d{1,2}:\\d{2}\\b").matcher(
				_input);
		while (m.find()) {
			LocalTime time = TimeExtractor.extractTime(m.group().trim());
			i = m.start();
			if (index < 0 || i < index) {
				timeList.add(time);
				index = i;
			} else {
				timeList.add(1, time);
			}
			Output.showToUser(time.toString());
		}

		m = Pattern.compile("[^\"]\\b\\d{1,2}\\s*[ap]m\\b",
				Pattern.CASE_INSENSITIVE).matcher(_input);
		while (m.find()) {
			LocalTime time = TimeExtractor.extractTime(m.group().replace(" ",
					""));
			i = m.start();
			if (index < 0 || i < index) {
				timeList.add(time);
				index = i;
			} else {
				timeList.add(1, time);
			}
			Output.showToUser(time.toString());
		}
		return index;
	}

	public static void main(String[] args) {
		TaskBuilder tb = new TaskBuilder("school holiday 8/14");
		tb.run();

	}

	public void run() {
		checkDatePattern();
	}

}
