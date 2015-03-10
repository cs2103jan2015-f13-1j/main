package parser;

import util.Task;

public class createCommand {
	

	public Command createAddCommand(Task t) {
		Command addCommand = new Command("add");
		addCommand.setTask(t);
		return addCommand;
	}

	public Command createEditCommand(String f, String t, int i) {
		Command editCommand = new Command("edit");
		editCommand.setIndex(i);
		editCommand.setContent(f);
		editCommand.setModifiedString(t);

		return editCommand;
	}

	public Command createDeleteCommand(int i) {
		Command deleteCommand = new Command("delete");
		deleteCommand.setIndex(i);

		return deleteCommand;
	}

	public Command createSearchCommand(String s) {
		Command searchCommand = new Command("search");
		searchCommand.setContent(s);

		return searchCommand;
	}

	public Command createUndoCommand() {
		Command undoCommand = new Command("undo");

		return undoCommand;
	}
}
