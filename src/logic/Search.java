package logic;

import java.util.Vector;

import util.Task;
import util.Output;

public class Search {
	private Vector<Task> TaskList = new Vector<Task>();
	//private boolean isSuccessful;

	public Search(Vector<Task> TaskList) {
		this.TaskList = TaskList;
	}

	Vector<Task> searchTask(String str) {
		Vector<Task> resultTaskList = new Vector<Task>();
		String textContent = null;

		for (int index = 1; index <= TaskList.size(); index++) {
			textContent = TaskList.get(index - 1).getTaskDesc();
			if (containsText(textContent, str)) {
				resultTaskList.add(TaskList.get(index - 1));
			}
		}
		Output.showToUser("keyword" + str + "searched");
		//isSuccessful = true;
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
