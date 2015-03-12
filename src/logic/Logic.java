package logic;

import java.time.*;
import java.util.*;

import fileIO.FileStream;
import parser.*;
import util.*;
import util.operator.COMMAND_TYPE;

public class Logic {
	private Vector<Task> TaskList = new Vector<Task>();
	private Vector<Task> BackUpList = new Vector<Task>();

	private static final String MESSAGE_ADD = "task %s added ";
	private static final String MESSAGE_DELETE = "task %s deleted\n";
	private static final String MESSAGE_TASK_FAILURE = "%s does not exist\n";
	private static final String MESSAGE_COMMAND_FAILURE = "Operation %s failed\n";

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

	void editTask(int index, String editType, String modifiedContent) {
		if (index > 0 && index <= TaskList.size()) {
			boolean isSuccessful = false;
			switch (editType) {
			case "task":
				isSuccessful = editTaskDesc(index, modifiedContent);
				break;
			case "starttime":
				editTaskStartTime(index, modifiedContent);
				break;
			case "endtime":
				editTaskEndTime(index, modifiedContent);
				break;
			case "startdate":
				editTaskStartDate(index, modifiedContent);
				break;
			case "enddate":
				editTaskEndDate(index, modifiedContent);
				break;
			}
			if (isSuccessful) {
				operator.showToUser("task " + index + editType + " edited: "
						+ modifiedContent);
				backup();
			}
		} else {
			operator.showToUser(String.format(MESSAGE_TASK_FAILURE, index));
		}
	}

	private boolean editTaskEndDate(int index, String modifiedContent) {
		try {
			Task editTask = TaskList.get(index - 1);
			LocalDateTime endtTime = editTask.getEndTime();

			if (endtTime != null) {
				LocalDate t = timeOperator.extractDate(modifiedContent);
				editTask.setStartTime(endtTime.withYear(t.getYear())
						.withDayOfYear(t.getDayOfYear()));
				return true;
			} else {
				return false;
			}
		} catch (NullPointerException e) {
			operator.showToUser(String.format(MESSAGE_TASK_FAILURE,
					"enddate for " + index));
			return false;
		}
	}

	private boolean editTaskStartDate(int index, String modifiedContent) {
		try {
			Task editTask = TaskList.get(index - 1);
			LocalDateTime startTime = editTask.getStartTime();

			if (editTask.getEndTime() != null) {
				LocalDate t = timeOperator.extractDate(modifiedContent);
				editTask.setStartTime(startTime.withYear(t.getYear())
						.withDayOfYear(t.getDayOfYear()));
				return true;
			} else {
				return false;
			}
		} catch (NullPointerException e) {
			operator.showToUser(String.format(MESSAGE_TASK_FAILURE,
					"starttime for " + index));
			return false;
		}
	}

	private boolean editTaskEndTime(int index, String modifiedContent) {
		try {
			Task editTask = TaskList.get(index - 1);
			LocalDateTime endtTime = editTask.getEndTime();

			if (endtTime != null) {
				LocalTime t = timeOperator.extractTime(modifiedContent);
				editTask.setEndTime(endtTime.withHour(t.getHour()).withMinute(
						t.getMinute()));
				return true;
			} else {
				return false;
			}
		} catch (NullPointerException e) {
			operator.showToUser(String.format(MESSAGE_TASK_FAILURE,
					"endtime for " + index));
			return false;
		}
	}

	private boolean editTaskStartTime(int index, String modifiedContent) {
		try {
			Task editTask = TaskList.get(index - 1);
			LocalDateTime startTime = editTask.getStartTime();

			if (startTime != null) {
				LocalTime t = timeOperator.extractTime(modifiedContent);
				editTask.setStartTime(startTime.withHour(t.getHour())
						.withMinute(t.getMinute()));
				return true;
			} else {
				return false;
			}
		} catch (NullPointerException e) {
			operator.showToUser(String.format(MESSAGE_TASK_FAILURE,
					"startdate for " + index));
			return false;
		}
	}

	private boolean editTaskDesc(int index, String modifiedContent) {
		try {
			Task editTask = TaskList.get(index - 1);
			editTask.setTaskDesc(modifiedContent);
			return true;
		} catch (IndexOutOfBoundsException e) {
			operator.showToUser(String.format(MESSAGE_TASK_FAILURE, index));
			return false;
		}
	}

	void deleteTask(int index) {
		try {
			if (index > 0 && index <= TaskList.size()) {
				TaskList.remove(index - 1);
				operator.showToUser(String.format(MESSAGE_DELETE, index));
				backup();
			} else {
				operator.showToUser(String.format(MESSAGE_TASK_FAILURE, index));
			}
		} catch (IndexOutOfBoundsException e) {
			operator.showToUser(String
					.format(MESSAGE_COMMAND_FAILURE, "delete"));
		}
	}

	void undo() {
		if (BackUpList != null) {
			TaskList = BackUpList;
			BackUpList = null;
			operator.showToUser("undo completed");
		} else {
			operator.showToUser(String.format(MESSAGE_COMMAND_FAILURE, "undo"));
		}
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
		
		TaskList = FileStream.loadTasksFromXML();
		
		Parser pr = new Parser();
		Command cmd = pr.parseInputString(input);
		executeCommand(cmd);

		FileStream.writeTasksToXML(TaskList);
	}

	private Vector<Task> executeCommand(Command cmd) {
		String cmdDesc = cmd.getCommandType();
		COMMAND_TYPE commandType = operator.determineCommandType(cmdDesc);

		switch (commandType) {
		case ADD_TASK:
			addTask(cmd.getTask());
			break;
		case DELETE_TASK:
			deleteTask(cmd.getIndex());
			break;
		case EDIT_TASK:
			editTask(cmd.getIndex(), cmd.getContent(), cmd.getModifiedString());
			break;
		case UNDO:
			undo();
			break;
		case SEARCH_TASK:
			return searchTask(cmd.getContent());
		case BACK:
			break;
		default:
			// throw an error if the command is not recognized
			throw new Error("Unrecognized command type");
		}
		return TaskList;
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
