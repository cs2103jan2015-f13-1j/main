package logic;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.*;

import fileIO.FileStream;
import parser.*;
import util.*;
import util.OperationType.COMMAND_TYPE;

public class Logic {
	private static Vector<Task> TaskList = new Vector<Task>();
	private Vector<Task> OutputList = new Vector<Task>();
	private int isFirstTime = 0;
	private boolean isSuccessful = false;
	private boolean inSearchState = false;
	private String keyword = "";
	private static String resultToUser = "";
	public static UndoOps u = new UndoOps();

	private final static Logger log = Logger.getLogger(Logic.class.getName());

	private static final String MSG_ADD = "Task added successfully!";
	private static final String MSG_DELETE = "Task deleted successfully!";
	private static final String MSG_DELETE_FAILURE = "%s does not exist!\n";
	private static final String MSG_COMMAND_FAILURE = "Command: %s failed!\n";
	private static final String MSG_CHGDIR = "Directory changed!\n";

	public String getText() {
		return resultToUser;
	}

	public static void setText(String s) {
		resultToUser = s;
	}

	public void addTask(Task t) {
		isSuccessful = true;

		TaskList.add(t);
		resultToUser = MSG_ADD;
		// log(LEVEL_INFO, MSG_ADD);
		if (t.getStartTime() != null)
			Output.showToUser("from " + t.getStartTime().toString());
		if (t.getEndTime() != null)
			Output.showToUser("to " + t.getEndTime().toString());
		Sort s = new Sort(TaskList);
		s.sortList();
		u.undoAdd(t.getIndex());

	}

	void deleteTask(int index) {
		try {
			if (index > 0 && index <= TaskList.size()) {
				u.undoDelete(TaskList.get(index - 1));
				TaskList.remove(index - 1);
				resultToUser = MSG_DELETE;
				isSuccessful = true;
			} else {
			}
		} catch (IndexOutOfBoundsException e) {
			resultToUser = String.format(MSG_COMMAND_FAILURE, "delete");
			Output.showToUser(String.format(MSG_DELETE_FAILURE, index));
		}
	}

	private void changeDir(String str) {
		u.undoChgdir();
		Output.showToUser(String.format(MSG_CHGDIR));
	}

	private void clearTask() {
		u.undoClear();
		TaskList.clear();
	}

	private void unmarkTask(int i) {
		
		if (i >= 0 && i < TaskList.size()) {
			TaskList.get(i).markTaskAsUndone();;
		}
	}

	private void markTask(int i) {
		
		if (i >= 0 && i < TaskList.size()) {
			TaskList.get(i).markTaskAsDone();
		}
	}

	private void executeCommand(Command cmd) {
		String cmdDesc = cmd.getCommandType();
		COMMAND_TYPE commandType = OperationType.determineCommandType(cmdDesc);
		isSuccessful = false;

		switch (commandType) {
		case ADD_TASK:
			addTask(cmd.getTask());
			break;
		case DELETE_TASK:
			deleteTask(cmd.getIndex());
			break;
		case EDIT_TASK:
			EditTask edit = new EditTask(TaskList);
			edit.editTask(cmd);
			break;
		case UNDO:
			u.undoOperation(TaskList);
			break;
		case SEARCH_TASK:
			Search s = new Search(TaskList);
			keyword = cmd.getContent();
			OutputList = s.searchTask(keyword);
			inSearchState = true;
			return;
		case BACK:
			inSearchState = false;
			break;
		case CHANGEDIR:
			changeDir(cmd.getContent());
			break;
		case CLEAR:
			clearTask();
			break;
		case DONE:
			markTask(cmd.getIndex());
			break;
		case UNDONE:
			unmarkTask(cmd.getIndex());
			break;

		default:
			// throw an error if the command is not recognized
			resultToUser = "Unrecognized command type";
			throw new Error("Unrecognized command type");
		}
		sortOutput();
	}

	private void sortOutput() {
		Sort s = new Sort(TaskList);
		s.sortList();
		if (inSearchState) {
			Search st = new Search(TaskList);
			assert !keyword.equals("");
			OutputList = st.searchTask(keyword);
		} else {
			OutputList = TaskList;
		}
	}

	public Vector<Task> run(String input) {
		log.setLevel(Level.INFO);

		isFirstTime++;
		if (isFirstTime == 1) {
			TaskList = initializeList();
		}

		Parser pr = new Parser();
		Command cmd = pr.parseInputString(input);
		executeCommand(cmd);
		log.info(resultToUser);

		if (isSuccessful) {
			FileStream.writeTasksToXML(TaskList);
		}

		return OutputList;
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
