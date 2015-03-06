package parser;

public class createCommand {

	public class Command {
		private String _commmandtype;
		private String _task;
		private int _index;
		private String _content, _result;

		public Command(String s) {
			_commmandtype = s;
		}

		public String getCommandType() {
			return _commmandtype;
		}

		public String getTask() {
			return _task;
		}

		public int getIndex() {
			return _index;
		}

		public String getContent() {
			return _content;
		}

		public String getResultString() {
			return _result;
		}

		public void setCommandType(String str) {
			this._commmandtype = str;
			System.out.println(str);
		}

		public void setTask(String t) {
			this._task = t;
			System.out.println(t);
		}

		public void setIndex(int i) {
			this._index = i;
			System.out.println(i);
		}

		public void setContent(String str) {
			this._content = str;
			System.out.println(str);
		}

		public void setResultString(String str) {
			this._result = str;
			System.out.println(str);
		}
	}

	public Command createAddCommand(String str) {
		Command addCommand = new Command("add");
		addCommand.setTask(str);
		return addCommand;
	}

	public Command createEditCommand(String f, String t, int i) {
		Command editCommand = new Command("edit");
		editCommand.setIndex(i);
		editCommand.setContent(f);
		editCommand.setResultString(t);

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
