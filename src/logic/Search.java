package logic;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Vector;

import util.Task;
import util.Output;
import util.Task.TASK_TYPE;
import util.TimeExtractor;

public class Search {
	private Vector<Task> TaskList = new Vector<Task>();

	private static final String MSG_SEARCH_COMMAND = "%s searched!\n";
	private static final String MSG_COMMAND_FAILURE = "Search format incorrect!\n";
	private static final String MSG_COMMAND_NULL = "command string cannot be null!";
	private static final String MSG_INCORRECT_FORMAT = "search command with incorrect format!";

	public Search(Vector<Task> TaskList) {
		this.TaskList = TaskList;
	}

	// @author A0105952H
	public Vector<Task> searchTask(String str) {
		try {
			LocalDate dt;
			String searchtype = str.substring(0, str.indexOf(" "));
			switch (searchtype.toLowerCase()) {
			case "desc":
				return searchDesc(str.substring(str.indexOf(" ")));
			case "task":
				return searchDesc(str.substring(str.indexOf(" ")));
			case "before":
				dt = extractDate(str.substring(str.indexOf(" ") + 1).trim());
				return searchBeforeDate(dt);
			case "date":
				dt = extractDate(str.substring(str.indexOf(" ") + 1).trim());
				return searchOnDate(dt);
			case "type":
				Task.TASK_TYPE t = determineTaskType(str.substring(
						str.indexOf(" ") + 1).trim());
				return searchTaskType(t);
			default:
				Output.showToUser(MSG_COMMAND_FAILURE);
				return TaskList;
			}
		} catch (Exception e) {
			Output.showToUser(MSG_COMMAND_FAILURE);
			return TaskList;
		}

	}

	private Task.TASK_TYPE determineTaskType(String taskTypeString) {
		if (taskTypeString == null) {
			Output.showToUser(MSG_COMMAND_NULL);
			return null;
		}

		if (taskTypeString.equalsIgnoreCase("floating")) {
			return TASK_TYPE.FLOATING_TASK;
		} else if (taskTypeString.equalsIgnoreCase("deadline")
				|| taskTypeString.equalsIgnoreCase("due")) {
			return TASK_TYPE.DEADLINE;
		} else if (taskTypeString.equalsIgnoreCase("timedtask")
				|| taskTypeString.equalsIgnoreCase("event")) {
			return TASK_TYPE.TIMED_TASK;
		} else {
			Output.showToUser(MSG_INCORRECT_FORMAT);
			return null;
		}
	}

	private LocalDate extractDate(String str) {
		LocalDate date;
		try {
			date = TimeExtractor.extractDate(str);
		} catch (Exception e) {
			date = null;
		}
		return date;
	}

	private Vector<Task> searchBeforeDate(LocalDate date) {

		Vector<Task> resultTaskList = new Vector<Task>();
		Task t = null;
		LocalDateTime dl = LocalDateTime.of(date, LocalTime.of(23, 59));

		if (date != null) {
			for (int index = 1; index <= TaskList.size(); index++) {
				t = TaskList.get(index - 1);
				if (t.getTaskType() != Task.TASK_TYPE.FLOATING_TASK
						&& t.getEndTime().isBefore(dl)) {
					resultTaskList.add(TaskList.get(index - 1));
				}
			}
			Output.showToUser(String.format(MSG_SEARCH_COMMAND, "Task before "
					+ TimeExtractor.formatDate(date)));
			return resultTaskList;
		} else {
			Output.showToUser(MSG_COMMAND_FAILURE);
			return TaskList;
		}
	}

	private Vector<Task> searchOnDate(LocalDate date) {
		Vector<Task> resultTaskList = new Vector<Task>();
		Task t = null;
		LocalDate dl;

		if (date != null) {
			for (int index = 1; index <= TaskList.size(); index++) {
				t = TaskList.get(index - 1);

				if (t.getTaskType() != Task.TASK_TYPE.FLOATING_TASK) {
					dl = t.getEndTime().toLocalDate();
					if (date.isEqual(dl)) {
						resultTaskList.add(TaskList.get(index - 1));
					}
				}
			}
			Output.showToUser(String.format(MSG_SEARCH_COMMAND, "Task on "
					+ TimeExtractor.formatDate(date)));
			return resultTaskList;
		} else {
			Output.showToUser(MSG_COMMAND_FAILURE);
			return TaskList;
		}
	}

	private Vector<Task> searchTaskType(Task.TASK_TYPE type) {
		Vector<Task> resultTaskList = new Vector<Task>();
		Task t = null;
		if (type != null) {
			for (int index = 1; index <= TaskList.size(); index++) {
				t = TaskList.get(index - 1);

				if (t.getTaskType() == type) {

					resultTaskList.add(TaskList.get(index - 1));

				}
			}
			Output.showToUser(String.format(MSG_SEARCH_COMMAND, "Task type "
					+ type));
			return resultTaskList;
		} else {
			Output.showToUser(MSG_COMMAND_FAILURE);
			return TaskList;
		}
	}

	private Vector<Task> searchDesc(String str) {
		Vector<Task> resultTaskList = new Vector<Task>();
		String textContent = null;

		for (int index = 1; index <= TaskList.size(); index++) {
			textContent = TaskList.get(index - 1).getTaskDesc();
			if (containsText(textContent, str)) {
				resultTaskList.add(TaskList.get(index - 1));
			}
		}
		Output.showToUser(String.format(MSG_SEARCH_COMMAND, "keyword " + str));
		return resultTaskList;
	}

	private boolean containsText(String taskcontent, String keyword) {
		String[] keywords = keyword.split(" ");
		for (String s : keywords) {
			// search key case insensitive
			if (!taskcontent.toLowerCase().contains(s.toLowerCase())) {
				return false;
			}
		}
		return true;
	}
}
