package parser;

import java.util.Scanner;
import java.util.StringTokenizer;

import util.*;
import util.OperationType.COMMAND_TYPE;

public class Parser {

	public Command parseInputString(String input) {
		String commandTypeString = input.trim().split("\\s+")[0];
		COMMAND_TYPE commandType = OperationType
				.determineCommandType(commandTypeString);

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
		case DONE:			
			return markTask(input);
		case UNDONE:
			return unmarkTask(input);
		case REDO:
			return redoTask();
		case FLAG:
			return flag(input);
		case UNFLAG:
			return unflag(input);
		case TOGGLEFLAG:
			return toggleflag(input);
		case TOGGLEDONE:
			return toggledone(input);
		default:
			//TODO throw an error if the command is not recognized
			return null;
			//throw new Error("Unrecognized command type");
		}
	}

	private Command addTask(String input) {
		input = input.substring(input.indexOf(" ")+1).trim();
		TaskBuilder extractor = new TaskBuilder(input);
		Task t = extractor.extractAddCommand();
		CreateCmd createCmd = new CreateCmd();
		return createCmd.createAddCommand(t);
	}

	private Command changeDir(String input) {
		CreateCmd clearcmd = new CreateCmd();
		input = input.substring(input.indexOf(" ")+1).trim();
		return clearcmd.createDirCommand(input);
	}

	private Command clearTask() {
		CreateCmd clearcmd = new CreateCmd();
		return clearcmd.createNewCommand("clear");
	}

	private Command createBackCommand() {
		CreateCmd backcmd = new CreateCmd();
		return backcmd.createBackCommand();
	}

	private Command deleteTask(String input) {
		input = input.substring(input.indexOf(" ")+1).trim();
		int index = Integer.parseInt(input);
		CreateCmd createCmd = new CreateCmd();
		return createCmd.createDeleteCommand(index);
	}

	private Command editTask(String input) {
		input = input.substring(input.indexOf(" ")+1).trim();
		String editType, modifiedContent = "";
		
		CreateCmd createCmd = new CreateCmd();
	
		StringTokenizer st = new StringTokenizer(input);
		int index = Integer.parseInt(st.nextToken());
		editType = st.nextToken();
		while(st.hasMoreTokens()){
			modifiedContent = modifiedContent.concat(" "+st.nextToken());
		}
		return createCmd.createEditCommand(editType, modifiedContent.trim(), index);
	}

	private Command flag(String input) {
		input = input.substring(input.indexOf(" ") + 1).trim();
		int i = Integer.parseInt(input);
	
		CreateCmd flagcmd = new CreateCmd();
		return flagcmd.createFlagCommand(i);
	}

	private Command unflag(String input) {
		input = input.substring(input.indexOf(" ") + 1).trim();
		int i = Integer.parseInt(input);
	
		CreateCmd flagcmd = new CreateCmd();
		return flagcmd.createUnflagCommand(i);
	}

	private Command toggleflag(String input) {
		input = input.substring(input.indexOf(" ") + 1).trim();
		int i = Integer.parseInt(input);
		
		CreateCmd flagcmd = new CreateCmd();
		return flagcmd.createToggleFlagCommand(i);
	}

	private Command markTask(String input) {
		input = input.substring(input.indexOf(" ") + 1).trim();
		int i = Integer.parseInt(input);
	
		CreateCmd markcmd = new CreateCmd();
		return markcmd.createMarkCommand(i);
		
	}

	private Command unmarkTask(String input) {
		input = input.substring(input.indexOf(" ") + 1).trim();
		int i = Integer.parseInt(input);
	
		CreateCmd markcmd = new CreateCmd();
		return markcmd.createUnmarkCommand(i);
	}

	private Command toggledone(String input) {
		input = input.substring(input.indexOf(" ") + 1).trim();
		int i = Integer.parseInt(input);
		
		CreateCmd markcmd = new CreateCmd();
		return markcmd.createToggleMarkCommand(i);
	}

	private Command searchTask(String input) {
		input = input.substring(input.indexOf(" ")+1).trim();
		CreateCmd createCmd = new CreateCmd();
		return createCmd.createSearchCommand(input);
	}

	private Command undo() {
		CreateCmd createCmd = new CreateCmd();
		return createCmd.createUndoCommand();
	}

	private Command redoTask() {
		CreateCmd redocmd = new CreateCmd();
		return redocmd.createNewCommand("redo");
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
