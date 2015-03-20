package parser;

import java.util.Scanner;
import java.util.StringTokenizer;

import util.*;
import util.operator.COMMAND_TYPE;

public class Parser {

	public Command parseInputString(String input) {
		String commandTypeString = input.trim().split("\\s+")[0];
		COMMAND_TYPE commandType = operator
				.determineCommandType(commandTypeString);
		input = input.toLowerCase();

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
		case CHANGEDIR:
			return changeDir(input);
		case CLEAR:
			return clearTask();
		case BACK:
			return createBackCommand();
		default:
			// throw an error if the command is not recognized
			throw new Error("Unrecognized command type");
		}
	}

	private Command changeDir(String input) {
		createCommand clearcmd = new createCommand();
		input = input.replaceFirst("changedir", "").trim();
		return clearcmd.createDirCommand(input);
	}

	private Command clearTask() {
		createCommand clearcmd = new createCommand();
		return clearcmd.createNewCommand("clear");
	}

	private Command createBackCommand() {
		createCommand backcmd = new createCommand();
		return backcmd.createBackCommand();
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
		String editType, modifiedContent = "";
		
		createCommand createCmd = new createCommand();

		StringTokenizer st = new StringTokenizer(input);
		int index = Integer.parseInt(st.nextToken());
		editType = st.nextToken();
		while(st.hasMoreTokens()){
			modifiedContent = modifiedContent.concat(" "+st.nextToken());
		}
		return createCmd.createEditCommand(editType, modifiedContent.trim(), index);
	}

	private Command deleteTask(String input) {
		input = input.replaceFirst("delete", "").trim();
		int index = Integer.parseInt(input);
		createCommand createCmd = new createCommand();
		return createCmd.createDeleteCommand(index);
	}

	private Command addTask(String input) {
		input = input.replaceFirst("add", "").trim();
		addCommandExtractor extractor = new addCommandExtractor(input);
		Task t = extractor.extractAddCommand();
		createCommand createCmd = new createCommand();
		return createCmd.createAddCommand(t);
	}

	public void main() {
		Parser pr = new Parser();
		Scanner sc = new Scanner(System.in);
		String str;
		str = sc.next();
		while (str.contains(new String("exit"))) {

			pr.parseInputString(str);
			str = sc.next();
		}
	}

}
