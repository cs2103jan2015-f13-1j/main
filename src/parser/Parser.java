package parser;

import java.util.Scanner;
import java.util.StringTokenizer;

import util.*;
import util.OperationType.COMMAND_TYPE;

public class Parser {

	// @author A0105952H
	public Command parseInputString(String input) {
		String commandTypeString = input.trim().split("\\s+")[0];
		COMMAND_TYPE commandType = OperationType
				.determineCommandType(commandTypeString);

		switch (commandType) {
		case ADD_TASK:
			return addTask(input);
		case BACK:
			return createBackCommand();
		case CHANGEDIR:
			return changeDir(input);
		case CLEAR:
			return clearTask(input);
		case DONE:
			return markTask(input);
		case UNDONE:
			return unmarkTask(input);
		case TOGGLEDONE:
			return toggledone(input);
		case DELETE_TASK:
			return deleteTask(input);
		case EDIT_TASK:
			return editTask(input);
		case FLAG:
			return flag(input);
		case UNFLAG:
			return unflag(input);
		case TOGGLEFLAG:
			return toggleflag(input);
		case MAN:
			return man(input);
		case UNDO:
			return undo(input);
		case SEARCH_TASK:
			return searchTask(input);
		case REDO:
			return redo(input);
		default:
			return null;
		}
	}

	private Command addTask(String input) {
		if (input.contains(" ")) {
			input = input.substring(input.indexOf(" ") + 1).trim();
			TaskBuilder extractor = new TaskBuilder(input);
			Task t = extractor.extractAddCommand();
			CreateCmd createCmd = new CreateCmd();
			return createCmd.createAddCommand(t);
		} else {
			return null;
		}
	}

	private Command changeDir(String input) {
		if (input.contains(" ")) {
			CreateCmd clearcmd = new CreateCmd();
			input = input.substring(input.indexOf(" ") + 1).trim();
			return clearcmd.createDirCommand(input);
		} else {
			return null;
		}
	}

	private Command clearTask(String input) {
		if (input.trim().toLowerCase().equals("clear")) {
			CreateCmd clearcmd = new CreateCmd();
			return clearcmd.createNewCommand("clear");
		} else {
			return null;
		}
	}

	private Command createBackCommand() {
		CreateCmd backcmd = new CreateCmd();
		return backcmd.createNewCommand("");
	}

	private Command deleteTask(String input) {
		try {
			if (input.contains(" ")) {
				input = input.substring(input.indexOf(" ") + 1).trim();
				int index = Integer.parseInt(input);
				CreateCmd createCmd = new CreateCmd();
				return createCmd.createDeleteCommand(index);
			} else {
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}

	private Command editTask(String input) {
		if (!input.contains(" ")) {
			return null;
		}

		input = input.substring(input.indexOf(" ") + 1).trim();
		String editType, modifiedContent = "";

		CreateCmd createCmd = new CreateCmd();

		try {
			StringTokenizer st = new StringTokenizer(input);
			editType = st.nextToken();
			int index = Integer.parseInt(st.nextToken());
			while (st.hasMoreTokens()) {
				modifiedContent = modifiedContent.concat(" " + st.nextToken());
			}
			return createCmd.createEditCommand(editType,
					modifiedContent.trim(), index);
		} catch (Exception e) {
			return null;
		}
	}

	private Command flag(String input) {
		try {
			if (!input.contains(" ")) {
				return null;
			}
			input = input.substring(input.indexOf(" ") + 1).trim();
			int i = Integer.parseInt(input);

			CreateCmd flagcmd = new CreateCmd();
			return flagcmd.createFlagCommand(i);
		} catch (Exception e) {
			return null;
		}
	}

	private Command unflag(String input) {
		try {
			if (!input.contains(" ")) {
				return null;
			}
			input = input.substring(input.indexOf(" ") + 1).trim();
			int i = Integer.parseInt(input);

			CreateCmd flagcmd = new CreateCmd();
			return flagcmd.createUnflagCommand(i);
		} catch (Exception e) {
			return null;
		}
	}

	private Command toggleflag(String input) {
		try {
			if (!input.contains(" ")) {
				return null;
			}
			input = input.substring(input.indexOf(" ") + 1).trim();
			int i = Integer.parseInt(input);

			CreateCmd flagcmd = new CreateCmd();
			return flagcmd.createToggleFlagCommand(i);
		} catch (Exception e) {
			return null;
		}
	}

	private Command markTask(String input) {
		try {
			if (!input.contains(" ")) {
				return null;
			}
			input = input.substring(input.indexOf(" ") + 1).trim();
			int i = Integer.parseInt(input);

			CreateCmd markcmd = new CreateCmd();
			return markcmd.createMarkCommand(i);
		} catch (Exception e) {
			return null;
		}

	}

	private Command unmarkTask(String input) {
		try {
			if (!input.contains(" ")) {
				return null;
			}
			input = input.substring(input.indexOf(" ") + 1).trim();
			int i = Integer.parseInt(input);

			CreateCmd markcmd = new CreateCmd();
			return markcmd.createUnmarkCommand(i);
		} catch (Exception e) {
			return null;
		}
	}

	private Command toggledone(String input) {
		try {
			if (!input.contains(" ")) {
				return null;
			}
			input = input.substring(input.indexOf(" ") + 1).trim();
			int i = Integer.parseInt(input);

			CreateCmd markcmd = new CreateCmd();
			return markcmd.createToggleMarkCommand(i);
		} catch (Exception e) {
			return null;
		}
	}

	private Command man(String input) {
		if (input.trim().toLowerCase().equals("man")) {
			CreateCmd mancmd = new CreateCmd();
			return mancmd.createNewCommand("man");
		} else {
			return null;
		}
	}

	private Command searchTask(String input) {
		try {
			if (!input.contains(" ")) {
				return null;
			}
			input = input.substring(input.indexOf(" ") + 1).trim();
			CreateCmd createCmd = new CreateCmd();
			return createCmd.createSearchCommand(input);
		} catch (Exception e) {
			return null;
		}
	}

	private Command undo(String input) {
		if (input.trim().toLowerCase().equals("undo")) {
			CreateCmd createCmd = new CreateCmd();
			return createCmd.createNewCommand("undo");
		} else {
			return null;
		}
	}

	private Command redo(String input) {
		if (input.trim().toLowerCase().equals("redo")) {
			CreateCmd redocmd = new CreateCmd();
			return redocmd.createNewCommand("redo");
		} else {
			return null;
		}
	}

	public static void main(String[] args) {
		Parser pr = new Parser();
		Scanner sc = new Scanner(System.in);
		String str;
		str = sc.next();
		while (!str.contains(new String("exit"))) {

			pr.parseInputString(str);
			str = sc.next();
		}
		sc.close();
	}
}
