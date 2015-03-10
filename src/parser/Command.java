package parser;

import util.Task;

public class Command {

	private String _commmandtype;
	private Task _task;
	private int _index;
	private String _content, _modifiedString;

	public Command(String s) {
		_commmandtype = s;
	}

	public String getCommandType() {
		return _commmandtype;
	}

	public Task getTask() {
		return _task;
	}

	public int getIndex() {
		return _index;
	}

	public String getContent() {
		return _content;
	}

	public String getModifiedString() {
		return _modifiedString;
	}

	public void setCommandType(String str) {
		this._commmandtype = str;
	}

	public void setTask(Task t) {
		this._task = t;
	}

	public void setIndex(int i) {
		this._index = i;
	}

	public void setContent(String str) {
		this._content = str;
	}

	public void setModifiedString(String str) {
		this._modifiedString = str;
	}
}
