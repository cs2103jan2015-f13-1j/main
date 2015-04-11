package util;

public class OperationType {

	public static enum COMMAND_TYPE {
		ADD_TASK, EDIT_TASK, INVALID, SEARCH_TASK, DELETE_TASK, UNDO, BACK, CHANGEDIR, CLEAR, DONE, UNDONE, REDO, FLAG, UNFLAG, TOGGLEDONE, TOGGLEFLAG, MAN
	};

	// @author A0105952H
	public static COMMAND_TYPE determineCommandType(String commandTypeString) {
		if (commandTypeString == null)
			throw new Error("command type string cannot be null!");

		if (commandTypeString.equalsIgnoreCase("add")
				|| commandTypeString.equalsIgnoreCase("create")) {
			return COMMAND_TYPE.ADD_TASK;
		} else if (commandTypeString.equalsIgnoreCase("delete")) {
			return COMMAND_TYPE.DELETE_TASK;
		} else if (commandTypeString.equalsIgnoreCase("edit")
				|| commandTypeString.equalsIgnoreCase("update")) {
			return COMMAND_TYPE.EDIT_TASK;
		} else if (commandTypeString.equalsIgnoreCase("search")) {
			return COMMAND_TYPE.SEARCH_TASK;
		} else if (commandTypeString.equalsIgnoreCase("undo")) {
			return COMMAND_TYPE.UNDO;
		} else if (commandTypeString.equalsIgnoreCase("clear")) {
			return COMMAND_TYPE.CLEAR;
		} else if (commandTypeString.equalsIgnoreCase("changedir")) {
			return COMMAND_TYPE.CHANGEDIR;
		} else if (commandTypeString.equalsIgnoreCase("done")
				|| commandTypeString.equalsIgnoreCase("mark")) {
			return COMMAND_TYPE.DONE;
		} else if (commandTypeString.equalsIgnoreCase("undone")
				|| commandTypeString.equalsIgnoreCase("unmark")) {
			return COMMAND_TYPE.UNDONE;
		} else if (commandTypeString.equalsIgnoreCase("redo")) {
			return COMMAND_TYPE.REDO;
		} else if (commandTypeString.equalsIgnoreCase("flag")
				|| commandTypeString.equalsIgnoreCase("prioritise")) {
			return COMMAND_TYPE.FLAG;
		} else if (commandTypeString.equalsIgnoreCase("unflag")) {
			return COMMAND_TYPE.UNFLAG;
		} else if (commandTypeString.equalsIgnoreCase("togglemark")
				|| commandTypeString.equalsIgnoreCase("toggledone")) {
			return COMMAND_TYPE.TOGGLEDONE;
		} else if (commandTypeString.equalsIgnoreCase("toggleflag")) {
			return COMMAND_TYPE.TOGGLEFLAG;
		} else if (commandTypeString.equalsIgnoreCase("help")
				|| commandTypeString.equalsIgnoreCase("?")
				|| commandTypeString.equalsIgnoreCase("man")) {
			return COMMAND_TYPE.MAN;
		} else if (commandTypeString.isEmpty()) {
			return COMMAND_TYPE.BACK;
		} else {
			return COMMAND_TYPE.INVALID;
		}
	}

}
