import java.util.*;

public class Logic {
	private Vector<Task> TaskList;
	private Vector<Task> BackUpList;

	private void backup() {
		BackUpList = TaskList;
	}

	public void addTask(Task t) {
		backup();
		TaskList.add(t);
	}

	void editTask(int index) {
		Task editTask = TaskList.get(index);

	}

	void deleteTask(int index) {
		if (index > 0 && index <= TaskList.size())
			TaskList.remove(index);
	}

	void undo() {
		TaskList = BackUpList;
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
