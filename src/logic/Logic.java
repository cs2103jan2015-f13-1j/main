package logic;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

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
	private boolean inUndoState = false;
	private String keyword = "";
	public static UndoOps u = new UndoOps();
	// TODO
	private final static Logger log = Logger.getLogger(Logic.class.getName());

	private static final String MSG_ADD = "Task added successfully!";
	private static final String MSG_DELETE = "Task deleted successfully!";
	private static final String MSG_DELETE_FAILURE = "%s does not exist!\n";
	private static final String MSG_COMMAND_FAILURE = "Command: %s failed!\n";
	private static final String MSG_CHGDIR = "Directory changed!\n";
	private static final String MSG_CLEAR = "List cleared!\n";
	private static final String MSG_MARK = "Task %d marked as done!\n";
	private static final String MSG_UNMARK = "Task %d marked as undone!\n";
	private static final String MSG_FLAG = "Task %d prioritised!\n";
	private static final String MSG_UNFLAG = "Task %d unprioritised!\n";
	private static final String MSG_BACK = "Back to main list.\n";
//	private static final String MSG_FORMAT = "Incorrect Format!\n";
	private static final String MSG_WRONG_FILE = "Manual cannot be open!\n";
	private static final String MSG_CMD_INCORRECT = "Unrecognized command type";

	private void addTask(Task t) {
		if (t != null) {
			isSuccessful = true;

			TaskList.add(t);

			Sort s = new Sort(TaskList);
			s.sortList();
			u.undoAdd(t.getIndex());
			u.redoAdd(t);

			Output.showToUser(MSG_ADD);
		} else {
			//Output.showToUser(MSG_FORMAT);
		}
	}

	private void deleteTask(int index) {
		try {
			if (index > 0 && index <= TaskList.size()) {
				u.undoDelete(TaskList.get(index - 1));
				u.redoDelete(index);
				TaskList.remove(index - 1);
				Output.showToUser(MSG_DELETE);
				isSuccessful = true;
			} else {
				Output.showToUser(String.format(MSG_COMMAND_FAILURE, "delete"));
			}
		} catch (IndexOutOfBoundsException e) {
			Output.showToUser(String.format(MSG_DELETE_FAILURE, index));
		}
	}

	private void changeDir() {
		FileStream.changeDir();
		if (!FileStream.getOldPath().equals(FileStream.getNewPath())) {
			u.undoChgdir();
			u.redoChgdir();
		}
		Output.showToUser(String.format(MSG_CHGDIR));
	}

	private void clearTask() {
		u.undoClear(TaskList);
		u.redoClear();
		TaskList.clear();
		isSuccessful = true;
		Output.showToUser(String.format(MSG_CLEAR));
	}

	private void markTask(int i) {

		if (i > 0 && i <= TaskList.size()) {
			Task t = TaskList.get(i - 1);
			t.markTaskAsDone();
			isSuccessful = true;

			Sort s = new Sort(TaskList);
			s.sortList();
			u.undoMark(t.getIndex());
			u.redoMark(i);

			Output.showToUser(String.format(MSG_MARK, i));
		} else {
			Output.showToUser(String.format(MSG_COMMAND_FAILURE, "mark"));
		}
	}

	private void unmarkTask(int i) {

		if (i > 0 && i <= TaskList.size()) {
			Task t = TaskList.get(i - 1);
			t.markTaskAsUndone();
			isSuccessful = true;

			Sort s = new Sort(TaskList);
			s.sortList();
			u.undoMark(t.getIndex());
			u.redoUnmark(i);

			Output.showToUser(String.format(MSG_UNMARK, i));
		} else {
			Output.showToUser(String.format(MSG_COMMAND_FAILURE, "unmark"));
		}
	}

	private void toggleMarkTask(int index) {
		if (index > 0 && index <= TaskList.size()) {
			Task t = TaskList.get(index - 1);
			if (t.getDone()) {
				unmarkTask(index);
			} else {
				markTask(index);
			}
		}
	}

	private void flagTask(int i) {

		if (i > 0 && i <= TaskList.size()) {
			Task t = TaskList.get(i - 1);
			t.markFlag();
			isSuccessful = true;

			Sort s = new Sort(TaskList);
			s.sortList();
			u.undoFlag(t.getIndex());
			u.redoFlag(i);

			Output.showToUser(String.format(MSG_FLAG, i));
		} else {
			Output.showToUser(String.format(MSG_COMMAND_FAILURE, "flag"));
		}
	}

	private void unflagTask(int i) {

		if (i > 0 && i <= TaskList.size()) {
			Task t = TaskList.get(i - 1);
			t.unmarkFlag();
			isSuccessful = true;

			Sort s = new Sort(TaskList);
			s.sortList();
			u.undoUnflag(t.getIndex());
			u.redoUnflag(i);

			Output.showToUser(String.format(MSG_UNFLAG, i));
		} else {
			Output.showToUser(String.format(MSG_COMMAND_FAILURE, "unflag"));
		}
	}

	private void toggleFlagTask(int index) {
		if (index > 0 && index <= TaskList.size()) {
			Task t = TaskList.get(index - 1);
			if (t.getFlag()) {
				unflagTask(index);
			} else {
				flagTask(index);
			}
		}
	}

	private void man(String content) {
		// TODO Auto-generated method stub
		File helpFile = new File("HelpCommands.html");
		try {
			Desktop.getDesktop().browse(helpFile.toURI());
			
		} catch (IOException e) {
			Output.showToUser(MSG_WRONG_FILE);
			//e.printStackTrace();
		}
	}

	private void undoTask() {
		isSuccessful = u.undoOperation(TaskList);
		inUndoState = true;
	}

	private void redoTask() {
		inUndoState = true;
		isSuccessful = u.redoOperation(TaskList);
	}

	private void backToMain() {
		inSearchState = false;
		Output.showToUser(String.format(MSG_BACK));
	}

	private void searchKey(Command cmd) {
		Search s = new Search(TaskList);
		keyword = cmd.getContent();
		OutputList = s.searchTask(keyword);
		inSearchState = true;
	}

	/** clean unnecessary undo history */
	private void clearUndoHistory(COMMAND_TYPE commandType) {
		if (inUndoState) {
			if (commandType != COMMAND_TYPE.UNDO
					&& commandType != COMMAND_TYPE.REDO) {
				inUndoState = false;
				u.clearHistoryList();
			}
		}
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

	private void executeCommand(Command cmd) {

		if (cmd == null) {
			Output.showToUser(MSG_CMD_INCORRECT);

		} else {
			String cmdDesc = cmd.getCommandType();
			COMMAND_TYPE commandType = OperationType
					.determineCommandType(cmdDesc);
			isSuccessful = false;

			switch (commandType) {
			case ADD_TASK:
				addTask(cmd.getTask());
				break;
			case BACK:
				backToMain();
				break;
			case CHANGEDIR:
				changeDir();
				break;
			case CLEAR:
				clearTask();
				break;
			case DELETE_TASK:
				deleteTask(cmd.getIndex());
				break;
			case DONE:
				markTask(cmd.getIndex());
				break;
			case UNDONE:
				unmarkTask(cmd.getIndex());
				break;
			case EDIT_TASK:
				EditTask edit = new EditTask(TaskList);
				isSuccessful = edit.editTask(cmd);
				break;
			case FLAG:
				flagTask(cmd.getIndex());
				break;
			case UNFLAG:
				unflagTask(cmd.getIndex());
				break;
			case MAN:
				man(cmd.getContent());
				break;
			case SEARCH_TASK:
				searchKey(cmd);
				return;
			case UNDO:
				undoTask();
				break;
			case REDO:
				redoTask();
				break;
			case TOGGLEFLAG:
				toggleFlagTask(cmd.getIndex());
				break;
			case TOGGLEDONE:
				toggleMarkTask(cmd.getIndex());
				break;
			default:
				Output.showToUser(MSG_CMD_INCORRECT);
			}
			clearUndoHistory(commandType);
		}
		sortOutput();
	}

	public Vector<Task> run(String input) {

		isFirstTime++;
		if (isFirstTime == 1) {
			TaskList = initializeList();
		}

		Parser pr = new Parser();
		Command cmd = pr.parseInputString(input);
		executeCommand(cmd);

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

	public Vector<Task> initializeList() {
		FileStream.initializeDir();
		Vector<Task> list = FileStream.loadTasksFromXML();
		Sort s = new Sort(list);
		s.sortList();
		return list;
	}

}
