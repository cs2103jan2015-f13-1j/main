package logic;

import java.util.*;

import FileStream.FileStream;
import parser.*;
import util.*;
import util.operator.COMMAND_TYPE;

public class Logic {
	private Vector<Task> TaskList = new Vector<Task>();
	private Vector<Task> BackUpList = new Vector<Task>();

	private static final String MESSAGE_ADD = "task %s added ";
	private static final String MESSAGE_DELETE = "task %s deleted\n";
	private static final String MESSAGE_FAILURE = "%s does not exist\n";

	private void backup() {
		BackUpList = TaskList;
	}

	public void addTask(Task t) {
		backup();
		TaskList.add(t);
		operator.showToUser(String.format(MESSAGE_ADD, t.getTaskDesc()));
		if (t.getStartTime() != null)
			operator.showToUser("from " + t.getStartTime().toString());
		if (t.getEndTime() != null)
			operator.showToUser("to " + t.getEndTime().toString());
	}

	void editTask(int index, String originalContent, String modifiedContent) {
		try {
			Task editTask = TaskList.get(index - 1);
			String desc = editTask.getTaskDesc();
			desc.replace(originalContent, modifiedContent);
			operator.showToUser("task " + index + " edited");
		} catch (IndexOutOfBoundsException e) {
			operator.showToUser(String.format(MESSAGE_FAILURE, index));
		}
	}

	void deleteTask(int index) {
		try {
			if (index > 0 && index <= TaskList.size()) {
				TaskList.remove(index - 1);
				operator.showToUser(String.format(MESSAGE_DELETE, index));
			}
			else{
				operator.showToUser(String.format(MESSAGE_FAILURE, index));
			}
		} catch (IndexOutOfBoundsException e) {

		}
	}

	void undo() {
		TaskList = BackUpList;
		operator.showToUser("task undo");
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
		operator.showToUser("keyword" + str + "searched");
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

	public void run(String input) {
		Parser pr = new Parser();
		Command cmd = pr.parseInputString(input);
		executeCommand(cmd);
		
		FileStream.writeTasksToXML(TaskList);
	}

	private void executeCommand(Command cmd) {
		String cmdDesc = cmd.getCommandType();
		COMMAND_TYPE commandType = operator.determineCommandType(cmdDesc);

		switch (commandType) {
		case ADD_TASK:
			addTask(cmd.getTask());
			return;
		case DELETE_TASK:
			deleteTask(cmd.getIndex());
			return;
		case EDIT_TASK:
			editTask(cmd.getIndex(), cmd.getContent(), cmd.getModifiedString());
			return;
		case UNDO:
			undo();
			return;
		case SEARCH_TASK:
			searchTask(cmd.getContent());
			return;

		default:
			// throw an error if the command is not recognized
			throw new Error("Unrecognized command type");
		}

	}

	public static void main(String[] args) {
		Logic lgc = new Logic();
		initializeList();
		Scanner sc = new Scanner(System.in);
		String str;
		str = sc.nextLine();
		while (!str.contains(new String("exit"))) {
			lgc.run(str);
			str = sc.nextLine();
		}
		sc.close();
	}

	private static void initializeList() {
		// update the list from memory

	}

}
