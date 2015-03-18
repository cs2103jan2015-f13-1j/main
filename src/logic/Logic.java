package logic;

import java.time.*;
import java.util.*;

import fileIO.FileStream;
import parser.*;
import util.*;
import util.operator.COMMAND_TYPE;

public class Logic {
	private Vector<Task> TaskList = new Vector<Task>();
	public Stack<Undo> UndoList = new Stack<Undo>();
	private Vector<Task> OutputList = new Vector<Task>();
	private int isFirstTime = 0;
	private boolean isSuccessful = false;
	private String resultToUser = "";
	
	//private static final String MESSAGE_ADD = "task %s added ";
	//private static final String MESSAGE_DELETE = "task %s deleted\n";
	//private static final String MESSAGE_TASK_FAILURE = "%s does not exist\n";
	//private static final String MESSAGE_COMMAND_FAILURE = "Operation %s failed\n";

	private static final String MSG_ADD = "Task added successfully!";
	private static final String MSG_DELETE = "Task deleted successfully!";
	private static final String MSG_EDIT = "Task edited successfully";
	private static final String MSG_TASK_FAILURE = "Edit %s does not exist!\n";
	private static final String MSG_COMMAND_FAILURE = "Command: %s failed!\n";
	private static final String MSG_UNDO = "Undo successful!";
	
	public String getText() {
		return resultToUser;
	}
	
	public void addTask(Task t) {
		isSuccessful = true;
		undoAdd();
		TaskList.add(t);
		resultToUser = MSG_ADD;
		if (t.getStartTime() != null)
			operator.showToUser("from " + t.getStartTime().toString());
		if (t.getEndTime() != null)
			operator.showToUser("to " + t.getEndTime().toString());

	}
	
	private void undoAdd() {
		Undo u = new Undo();
		u.setCommand("delete");
		u.setIndex(TaskList.size());
		UndoList.push(u);
	}

	void editTask(int index, String editType, String modifiedContent) {
		if (index > 0 && index <= TaskList.size()) {
			switch (editType.toLowerCase()) {
			case "task":
				isSuccessful = editTaskDesc(index, modifiedContent);
				break;
			case "starttime":
				isSuccessful = editTaskStartTime(index, modifiedContent);
				break;
			case "endtime":
				isSuccessful = editTaskEndTime(index, modifiedContent);
				break;
			case "startdate":
				isSuccessful = editTaskStartDate(index, modifiedContent);
				break;
			case "enddate":
				isSuccessful = editTaskEndDate(index, modifiedContent);
				break;
			}
			if (isSuccessful) {
				resultToUser = MSG_EDIT;
			}
		} else {
			resultToUser = String.format(MSG_TASK_FAILURE, index);
		}
	}

	private boolean editTaskEndDate(int index, String modifiedContent) {
		try {
			Task editTask = TaskList.get(index - 1);
			LocalDateTime endTime = editTask.getEndTime();

			if (endTime != null) {
				
				undoEditEndDate(index, endTime);
				
				LocalDate t = timeOperator.extractDate(modifiedContent);
				editTask.setEndTime(endTime.withYear(t.getYear())
						.withDayOfYear(t.getDayOfYear()));
				return true;
			} else {
				return false;
			}
		} catch (NullPointerException e) {
			resultToUser = String.format(MSG_TASK_FAILURE, "enddate for " + index);
			return false;
		}
	}

	private void undoEditEndDate(int index, LocalDateTime endTime) {
		Undo u = new Undo();
		u.setCommand("editEndDate");
		u.setIndex(index);
		u.setEndTime(endTime);
		UndoList.push(u);
	}
	
	private boolean editTaskStartDate(int index, String modifiedContent) {
		try {
			Task editTask = TaskList.get(index - 1);
			LocalDateTime startTime = editTask.getStartTime();

			if (startTime != null) {
				LocalDate t = timeOperator.extractDate(modifiedContent);
				
				undoEditStartDate(index, startTime);
				
				editTask.setStartTime(startTime.withYear(t.getYear())
						.withDayOfYear(t.getDayOfYear()));
				return true;
			} else {
				return false;
			}
		} catch (NullPointerException e) {
			resultToUser = String.format(MSG_TASK_FAILURE, "startdate for " + index);
			return false;
		}
	}
	
	private void undoEditStartDate(int index, LocalDateTime startTime) {
		Undo u = new Undo();
		u.setCommand("editStartDate");
		u.setIndex(index);
		u.setStartTime(startTime);
		UndoList.push(u);
	}

	private boolean editTaskEndTime(int index, String modifiedContent) {
		try {
			Task editTask = TaskList.get(index - 1);
			LocalDateTime endTime = editTask.getEndTime();

			if (endTime != null) {
				LocalTime t = timeOperator.extractTime(modifiedContent);
				
				undoEditEndTime(index, endTime);
				
				editTask.setEndTime(endTime.withHour(t.getHour()).withMinute(
						t.getMinute()));
				return true;
			} else {
				return false;
			}
		} catch (NullPointerException e) {
			resultToUser = String.format(MSG_TASK_FAILURE, "endtime for " + index);
			return false;
		}
	}
	
	private void undoEditEndTime(int index, LocalDateTime endTime) {
		Undo u = new Undo();
		u.setCommand("editEndTime");
		u.setIndex(index);
		u.setEndTime(endTime);
		UndoList.push(u);
	}

	private boolean editTaskStartTime(int index, String modifiedContent) {
		try {
			Task editTask = TaskList.get(index - 1);
			LocalDateTime startTime = editTask.getStartTime();

			if (startTime != null) {
				LocalTime t = timeOperator.extractTime(modifiedContent);
				
				undoEditStartTime(index, startTime);
				
				editTask.setStartTime(startTime.withHour(t.getHour())
						.withMinute(t.getMinute()));
				return true;
			} else {
				return false;
			}
		} catch (NullPointerException e) {
			resultToUser = String.format(MSG_TASK_FAILURE, "startdate for " + index);
			return false;
		}
	}
	
	private void undoEditStartTime(int index, LocalDateTime startTime) {
		Undo u = new Undo();
		u.setCommand("editStartTime");
		u.setIndex(index);
		u.setStartTime(startTime);
		UndoList.push(u);
	}

	private boolean editTaskDesc(int index, String modifiedContent) {
		try {
			Task editTask = TaskList.get(index - 1);
			
			undoEditTaskDesc(index, editTask);
			
			editTask.setTaskDesc(modifiedContent);
			return true;
		} catch (IndexOutOfBoundsException e) {
			resultToUser = String.format(MSG_TASK_FAILURE, index);
			return false;
		}
	}
	
	private void undoEditTaskDesc(int index, Task editTask) {
		Undo u = new Undo();
		u.setCommand("editTaskDesc");
		u.setIndex(index);
		u.setTaskDesc(editTask.getTaskDesc());
		UndoList.push(u);
	}

	void deleteTask(int index) {
		try {
			if (index > 0 && index <= TaskList.size()) {
				undoDelete(index);
				TaskList.remove(index - 1);
				resultToUser = MSG_DELETE;
				isSuccessful = true;
			} else {
				resultToUser = String.format(MSG_TASK_FAILURE, index);
			}
		} catch (IndexOutOfBoundsException e) {
			resultToUser = String.format(MSG_COMMAND_FAILURE, "delete");
		}
	}

	private void undoDelete(int index) {
		Undo u = new Undo();
		Task t = TaskList.get(index - 1);
		u.setCommand("add");
		u.setIndex(index);
		u.setTaskDesc(t.getTaskDesc());
		u.setStartTime(t.getStartTime());
		u.setEndTime(t.getEndTime());
		UndoList.push(u);
	}
	
	void undo() {
		if (UndoList != null) {
			Undo u = UndoList.pop();
			switch (u.getCommand()) {
			case "add":
				Task add = new Task();
				add.setTaskDesc(u.getTaskDesc());
				add.setStartTime(u.getStartTime());
				add.setEndTime(u.getEndTime());
				TaskList.add((u.getIndex() - 1), add);
				break;
			case "delete":
				TaskList.remove(u.getIndex());
				break;
			case "editTaskDesc":
				Task editTaskDesc = TaskList.get(u.getIndex() - 1);
				editTaskDesc.setTaskDesc(u.getTaskDesc());
				break;
			case "editStartTime":
				Task editTaskStartTime = TaskList.get(u.getIndex() - 1);
				editTaskStartTime.setStartTime(u.getStartTime());
				break;
			case "editEndTime":
				Task editTaskEndTime = TaskList.get(u.getIndex() - 1);
				editTaskEndTime.setEndTime(u.getEndTime());
				break;
			case "editStartDate":
				Task editTaskStartDate = TaskList.get(u.getIndex() - 1);
				editTaskStartDate.setStartTime(u.getStartTime());
				break;
			case "editEndDate":
				Task editTaskEndDate = TaskList.get(u.getIndex() - 1);
				editTaskEndDate.setEndTime(u.getEndTime());
				break;
			}
			resultToUser = MSG_UNDO;
			} 
		else {
			resultToUser = String.format(MSG_COMMAND_FAILURE, "undo");
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
		isSuccessful = true;
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

	public Vector<Task> run(String input) {

		isFirstTime++;
		if (isFirstTime == 1) {
			TaskList = initializeList();
		}

		Parser pr = new Parser();
		Command cmd = pr.parseInputString(input);
		OutputList = executeCommand(cmd);

		if (isSuccessful) {
			FileStream.writeTasksToXML(TaskList);
		}
		return OutputList;
	}

	private Vector<Task> executeCommand(Command cmd) {
		String cmdDesc = cmd.getCommandType();
		COMMAND_TYPE commandType = operator.determineCommandType(cmdDesc);
		isSuccessful = false;

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
			resultToUser = "Unrecognized command type";
			throw new Error("Unrecognized command type");
		}
		return TaskList;
	}

	public static void main(String[] args) {
		Logic lgc = new Logic();
		
		Scanner sc = new Scanner(System.in);
		String str;
		str = sc.nextLine();
		while (!str.contains(new String("exit"))) {
			lgc.run(str);
			str = sc.nextLine();
		}
		sc.close();
	}

	private Vector<Task> initializeList() {
		return FileStream.loadTasksFromXML();
	}
}
