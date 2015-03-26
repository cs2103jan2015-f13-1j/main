package logic;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Vector;

import util.Task;
import util.Output;
import util.TimeExtractor;

public class Search {
	private Vector<Task> TaskList = new Vector<Task>();

	// private boolean isSuccessful;

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
		Vector<Task> resultTaskList = new Vector<Task>();
		Task t = null;
		LocalDateTime dl = LocalDateTime.of(date, LocalTime.of(23, 59));

		for (int index = 1; index <= TaskList.size(); index++) {
			t = TaskList.get(index - 1);
			if (t.getTaskType() != Task.TASK_TYPE.FLOATING_TASK
					&& t.getEndTime().isBefore(dl)) {
				resultTaskList.add(TaskList.get(index - 1));
			}
		}
		// isSuccessful = true;
		return resultTaskList;
	}

	private Vector<Task> searchOnDate(LocalDate date) {
		Vector<Task> resultTaskList = new Vector<Task>();
		Task t = null;
		LocalDate dl;

		for (int index = 1; index <= TaskList.size(); index++) {
			t = TaskList.get(index - 1);

			if (t.getTaskType() != Task.TASK_TYPE.FLOATING_TASK) {
				dl = t.getEndTime().toLocalDate();
				if (dl.isEqual(dl)) {
					resultTaskList.add(TaskList.get(index - 1));
				}
			}
		}
		// isSuccessful = true;
		return resultTaskList;
	}

	public Vector<Task> searchTask(String str) {
		Vector<Task> resultTaskList = new Vector<Task>();
		String textContent = null;

		for (int index = 1; index <= TaskList.size(); index++) {
			textContent = TaskList.get(index - 1).getTaskDesc();
			if (containsText(textContent, str)) {
				resultTaskList.add(TaskList.get(index - 1));
			}
		}
		Output.showToUser("keyword" + str + "searched");
		// isSuccessful = true;
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
