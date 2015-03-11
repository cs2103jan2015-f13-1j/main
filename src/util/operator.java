package util;

public class operator {

	public static enum COMMAND_TYPE {
		ADD_TASK, EDIT_TASK, INVALID, SEARCH_TASK, DELETE_TASK, UNDO, BACK
	};

	public static COMMAND_TYPE determineCommandType(String commandTypeString) {
		if (commandTypeString == null)
			throw new Error("command type string cannot be null!");

		if (commandTypeString.equalsIgnoreCase("add")) {
			return COMMAND_TYPE.ADD_TASK;
		} else if (commandTypeString.equalsIgnoreCase("delete")) {
			return COMMAND_TYPE.DELETE_TASK;
		} else if (commandTypeString.equalsIgnoreCase("edit")) {
			return COMMAND_TYPE.EDIT_TASK;
		} else if (commandTypeString.equalsIgnoreCase("search")) {
			return COMMAND_TYPE.SEARCH_TASK;
		} else if (commandTypeString.equalsIgnoreCase("undo")) {
			return COMMAND_TYPE.UNDO;
		} else if (commandTypeString.isEmpty()) {
			return COMMAND_TYPE.BACK;
		} else {
			return COMMAND_TYPE.INVALID;
		}
	}

	public static void showToUser(String text) {
		System.out.println(text);
	}

}
