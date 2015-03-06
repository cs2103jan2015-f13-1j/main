package parser;

import java.util.Scanner;
import java.util.StringTokenizer;

import parser.createCommand.Command;

public class Parser {

	enum COMMAND_TYPE {
		ADD_TASK, EDIT_TASK, INVALID, SEARCH_TASK, DELETE_TASK, UNDO
	};

	public Command parseInputString(String input) {
		String commandTypeString = input.trim().split("\\s+")[0];
		COMMAND_TYPE commandType = determineCommandType(commandTypeString);

		switch (commandType) {
		case ADD_TASK:
			return addTask(input);
		case DELETE_TASK:
			return deleteTask(input);
		case EDIT_TASK:
			return editTask(input);
		case UNDO:
			return undo();
		case SEARCH_TASK:
			return searchTask(input);

		default:
			// throw an error if the command is not recognized
			throw new Error("Unrecognized command type");
		}
	}

	private Command undo() {
		createCommand createCmd = new createCommand();
		return createCmd.createUndoCommand();
	}

	private Command searchTask(String input) {
		input = input.replaceFirst("search", "").trim();
		createCommand createCmd = new createCommand();
		return createCmd.createSearchCommand(input);
	}

	private Command editTask(String input) {
		input = input.replaceFirst("edit", "").trim();
		String fromText = "" , toText = "";
		int f_flag = 0, t_flag = 0;
		createCommand createCmd = new createCommand();

		StringTokenizer st = new StringTokenizer(input);
		int index = Integer.parseInt(st.nextToken());

		while (st.hasMoreTokens()) {
			String str = st.nextToken();
			if (str.contains(new String("from"))) {
				f_flag = 1;
			} else if (str.contains(new String("to"))) {
				f_flag = 0; t_flag = 1;
			}else if (f_flag == 1) {
				fromText = fromText.concat(str);
			}else if (t_flag == 1) {
				fromText = toText.concat(str);
			}
		}		
		return createCmd.createEditCommand(fromText, toText, index);
	}

	private Command deleteTask(String input) {
		input = input.replaceFirst("delete", "").trim();
		int index = Integer.parseInt(input);
		createCommand createCmd = new createCommand();
		return createCmd.createDeleteCommand(index);
	}

	private Command addTask(String input) {
		input = input.replaceFirst("add", "").trim();
		createCommand createCmd = new createCommand();
		return createCmd.createAddCommand(input);
	}

	private static COMMAND_TYPE determineCommandType(String commandTypeString) {
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
		} else {
			return COMMAND_TYPE.INVALID;
		}
	}
	
	public void main(){
		Parser pr = new Parser();
		Scanner sc = new Scanner(System.in);
		String str;
		str = sc.next();
		while(str.contains(new String("exit"))){
			
			pr.parseInputString(str);
			str = sc.next();
		}
	}

}
