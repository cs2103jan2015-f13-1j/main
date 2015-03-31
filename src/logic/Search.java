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

	private static final String MSG_COMMAND_FAILURE = "Search format incorrect!\n";

	public Search(Vector<Task> TaskList) {
		this.TaskList = TaskList;
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
		Output.showToUser("Search task type!");

		Vector<Task> resultTaskList = new Vector<Task>();
		Task t = null;
		LocalDateTime dl = LocalDateTime.of(date, LocalTime.of(23, 59));

		if (dl != null) {
			for (int index = 1; index <= TaskList.size(); index++) {
				t = TaskList.get(index - 1);
				if (t.getTaskType() != Task.TASK_TYPE.FLOATING_TASK
						&& t.getEndTime().isBefore(dl)) {
					resultTaskList.add(TaskList.get(index - 1));
				}
			}
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
			return resultTaskList;
		} else {
			Output.showToUser(MSG_COMMAND_FAILURE);
			return TaskList;
		}
	}

	public Vector<Task> searchTask(String str) {
		LocalDate dt;
		String searchtype = str.substring(0, str.indexOf(" "));
		switch (searchtype.toLowerCase()) {
		case "desc":
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

	}

	public Task.TASK_TYPE determineTaskType(String taskTypeString) {
		if (taskTypeString == null) {
			Output.showToUser("command type string cannot be null!");
			return null;
		}
		// TODO throw new Error("command type string cannot be null!");
		Output.showToUser("Search task type!");
		if (taskTypeString.equalsIgnoreCase("floating")
				|| taskTypeString.equalsIgnoreCase("create")) {
			return TASK_TYPE.FLOATING_TASK;
		} else if (taskTypeString.equalsIgnoreCase("deadline")
				|| taskTypeString.equalsIgnoreCase("due")) {
			return TASK_TYPE.DEADLINE;
		} else if (taskTypeString.equalsIgnoreCase("timedtask")
				|| taskTypeString.equalsIgnoreCase("event")) {
			return TASK_TYPE.TIMED_TASK;
		} else {
			Output.showToUser("command type string cannot be null!");
			return null;
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
		Output.showToUser("keyword " + str + " searched");
		return resultTaskList;
	}

	private boolean containsText(String taskcontent, String keyword) {
		String[] keywords = keyword.split(" ");
		for (String s : keywords) {
			if (!taskcontent.contains(s)) {
				return false;
			}
		}
		return true;
	}
}
