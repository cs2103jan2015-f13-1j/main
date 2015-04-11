package parser;

import util.Task;

public class CreateCmd {

	// @author A0105952H
	public Command createNewCommand(String str) {
		assert str.equals(new String("clear")) || str.equals(new String(""))
				|| str.equals(new String("undo"))
				|| str.equals(new String("redo"))
				|| str.equals(new String("man"));
		Command command = new Command(str);
		return command;
	}

	public Command createAddCommand(Task t) {
		Command addCommand = new Command("add");
		addCommand.setTask(t);
		return addCommand;
	}

	public Command createDeleteCommand(int i) {
		Command deleteCommand = new Command("delete");
		deleteCommand.setIndex(i);

		return deleteCommand;
	}

	public Command createEditCommand(String f, String t, int i) {
		Command editCommand = new Command("edit");
		editCommand.setIndex(i);
		editCommand.setContent(f);
		editCommand.setModifiedString(t);

		return editCommand;
	}

	public Command createDirCommand(String str) {
		Command dirCommand = new Command("changedir");
		dirCommand.setContent(str);
		return dirCommand;
	}

	public Command createFlagCommand(int i) {
		Command flagCommand = new Command("flag");
		flagCommand.setIndex(i);
		return flagCommand;
	}

	public Command createUnflagCommand(int i) {
		Command unflagCommand = new Command("unflag");
		unflagCommand.setIndex(i);
		return unflagCommand;
	}

	public Command createToggleFlagCommand(int i) {
		Command flagCommand = new Command("toggleflag");
		flagCommand.setIndex(i);
		return flagCommand;
	}

	public Command createMarkCommand(int i) {
		Command markCommand = new Command("mark");
		markCommand.setIndex(i);
		return markCommand;
	}

	public Command createUnmarkCommand(int i) {
		Command markCommand = new Command("unmark");
		markCommand.setIndex(i);
		return markCommand;
	}

	public Command createToggleMarkCommand(int i) {
		Command markCommand = new Command("togglemark");
		markCommand.setIndex(i);
		return markCommand;
	}

	public Command createSearchCommand(String s) {
		Command searchCommand = new Command("search");
		searchCommand.setContent(s);

		return searchCommand;
	}
}
