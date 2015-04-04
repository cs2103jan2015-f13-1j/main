package logic;

import java.time.LocalDateTime;
import java.util.Stack;
import java.util.Vector;

import fileIO.FileStream;
import util.Output;
import util.Task;

public class UndoOps {

	private Stack<Task> UndoList = new Stack<Task>();
	private Stack<Task> RedoList = new Stack<Task>();
	private Stack<Task> RedoListAfterUndo = new Stack<Task>();
	private Stack<Task> UndoListAfterUndo = new Stack<Task>();
	private Stack<String> CommandList = new Stack<String>();
	private Stack<String> RedoCommandList = new Stack<String>();
	private Stack<String> CommandListAfterUndo = new Stack<String>();
	private Stack<String> RedoCommandListAfterUndo = new Stack<String>();
	private Stack<Vector<Task>> backUpList = new Stack<Vector<Task>>();
	private static final String MSG_COMMAND_FAILURE = "Command: %s failed!\n";
	private static final String MSG_UNDO = "Undo successful!";
	private static final String MSG_REDO = "Redo successful!";

	public boolean undoOperation(Vector<Task> TaskList) {
		if (!UndoList.isEmpty()) {
			Task u = UndoList.peek();
			String cmd = CommandList.peek();
			executeCmd(TaskList, u, cmd);
			Output.showToUser(MSG_UNDO);

			RedoListAfterUndo.add(RedoList.pop());
			UndoListAfterUndo.add(UndoList.pop());
			RedoCommandListAfterUndo.add(RedoCommandList.pop());
			CommandListAfterUndo.add(CommandList.pop());

			return true;
		} else {
			Output.showToUser(String.format(MSG_COMMAND_FAILURE, "undo"));
			return false;
		}
	}

	public boolean redoOperation(Vector<Task> TaskList) {
		if (!RedoListAfterUndo.isEmpty()) {
			Task u = RedoListAfterUndo.peek();
			String cmd = RedoCommandListAfterUndo.peek();

			executeCmd(TaskList, u, cmd);
			Output.showToUser(MSG_REDO);

			RedoList.add(RedoListAfterUndo.pop());
			UndoList.add(UndoListAfterUndo.pop());
			RedoCommandList.add(RedoCommandListAfterUndo.pop());
			CommandList.add(CommandListAfterUndo.pop());
			return true;
		} else {
			Output.showToUser(String.format(MSG_COMMAND_FAILURE, "redo"));
			return false;
		}
	}

	private void executeCmd(Vector<Task> TaskList, Task u, String cmd) {
		switch (cmd) {
		case "add":
			TaskList.add(u);
			break;
		case "delete":
			Output.showToUser(TaskList.size() + "");
			TaskList.remove(u.getIndex() - 1);
			break;
		case "editTaskDesc":
			Task editTaskDesc = TaskList.get(u.getIndex() - 1);
			editTaskDesc.setTaskDesc(u.getTaskDesc());
			break;
		case "editStartTime":
			Task editTaskStartTime = TaskList.get(u.getIndex() - 1);
			editTaskStartTime.setStartTime(u.getStartTime());
			break;
		case "editEndTime":
			Task editTaskEndTime = TaskList.get(u.getIndex() - 1);
			editTaskEndTime.setEndTime(u.getEndTime());
			break;
		case "editStartDate":
			Task editTaskStartDate = TaskList.get(u.getIndex() - 1);
			editTaskStartDate.setStartTime(u.getStartTime());
			break;
		case "editEndDate":
			Task editTaskEndDate = TaskList.get(u.getIndex() - 1);
			editTaskEndDate.setEndTime(u.getEndTime());
			break;
		case "mark":
			Task markTask = TaskList.get(u.getIndex() - 1);
			markTask.markTaskAsDone();
			break;
		case "unmark":
			Task unmarkTask = TaskList.get(u.getIndex() - 1);
			unmarkTask.markTaskAsUndone();
			break;
		case "flag":
			Task flagTask = TaskList.get(u.getIndex() - 1);
			flagTask.markFlag();
			break;
		case "unflag":
			Task unflagTask = TaskList.get(u.getIndex() - 1);
			unflagTask.unmarkFlag();
			break;
		case "changedir":
			String pathName = u.getTaskDesc();
			FileStream.changeDirWithString(pathName);
			break;
		case "recover":
			recoverList(TaskList);
			break;
		case "clear":
			clearList(TaskList);
			break;
		}
	}

	private void clearList(Vector<Task> TaskList) {
		Vector<Task> copyOfTaskList = new Vector<Task>();
		for (Task t : TaskList) {
			copyOfTaskList.add(t);
		}
		TaskList.clear();
		backUpList.push(copyOfTaskList);
	}

	private void recoverList(Vector<Task> TaskList) {
		Vector<Task> copyOfTaskList = backUpList.pop();
		for (Task t : copyOfTaskList) {
			TaskList.add(t);
		}
	}

	public void clearHistoryList() {
		RedoListAfterUndo.clear();
		UndoListAfterUndo.clear();
		RedoCommandListAfterUndo.clear();
		CommandListAfterUndo.clear();
	}

	public void undoAdd(int index) {
		Task u = new Task();
		u.setIndex(index);
		CommandList.push(new String("delete"));
		UndoList.push(u);
	}

	public void redoAdd(Task t) {

		RedoCommandList.add(new String("add"));
		RedoList.add(t);
	}

	public void undoClear(Vector<Task> TaskList) {

		Vector<Task> copyOfTaskList = new Vector<Task>();
		for (Task t : TaskList) {
			copyOfTaskList.add(t);
		}

		backUpList.push(copyOfTaskList);
		CommandList.push("recover");
		UndoList.push(new Task());
	}

	public void redoClear() {
		// TODO Auto-generated method stub

		RedoCommandList.push("clear");
		RedoList.push(new Task());
	}

	public void undoChgdir() {
		Task u = new Task();
		String desc = FileStream.getOldPath();
		CommandList.push(new String("changedir"));
		u.setTaskDesc(desc);
		UndoList.push(u);
	}

	public void redoChgdir() {
		Task u = new Task();
		String desc = FileStream.getNewPath();
		RedoCommandList.push(new String("changedir"));
		u.setTaskDesc(desc);
		RedoList.push(u);
	}

	public void undoDelete(Task u) {
		CommandList.push(new String("add"));
		UndoList.push(u);
	}

	public void redoDelete(int index) {
		Task u = new Task();
		u.setIndex(index);
		RedoCommandList.push(new String("delete"));
		RedoList.push(u);
	}

	public void undoEditEndDate(int index, LocalDateTime endTime) {
		Task u = new Task();
		CommandList.push(new String("editEndDate"));
		u.setIndex(index);
		u.setEndTime(endTime);
		UndoList.push(u);
	}

	public void redoEditEndDate(int index, LocalDateTime endTime) {
		Task u = new Task();
		u.setIndex(index);
		RedoCommandList.add(new String("editEndDate"));
		u.setEndTime(endTime);
		RedoList.add(u);
	}

	public void undoEditStartDate(int index, LocalDateTime startTime) {
		Task u = new Task();

		CommandList.push(new String("editStartDate"));
		u.setIndex(index);
		u.setStartTime(startTime);
		UndoList.push(u);
	}

	public void redoEditStartDate(int index, LocalDateTime startTime) {
		Task u = new Task();
		u.setIndex(index);
		RedoCommandList.add(new String("editStartDate"));
		u.setEndTime(startTime);
		RedoList.add(u);
	}

	public void undoEditEndTime(int index, LocalDateTime endTime) {
		Task u = new Task();
		CommandList.push(new String("editEndTime"));
		u.setIndex(index);
		u.setEndTime(endTime);
		UndoList.push(u);
	}

	public void redoEditEndTime(int index, LocalDateTime endTime) {
		Task u = new Task();
		u.setIndex(index);
		RedoCommandList.add(new String("editStartDate"));
		u.setEndTime(endTime);
		RedoList.add(u);
	}

	public void undoEditStartTime(int index, LocalDateTime startTime) {
		Task u = new Task();
		CommandList.push(new String("editStartTime"));
		u.setIndex(index);
		u.setStartTime(startTime);
		UndoList.push(u);
	}

	public void redoEditStartTime(int index, LocalDateTime startTime) {
		Task u = new Task();
		RedoCommandList.add(new String("editStartTime"));
		u.setIndex(index);
		u.setStartTime(startTime);
		RedoList.add(u);
	}

	public void undoEditTaskDesc(int index, String originalContent) {
		Task u = new Task();
		CommandList.push(new String("editTaskDesc"));
		u.setIndex(index);
		u.setTaskDesc(originalContent);
		UndoList.push(u);
	}

	public void redoEditTaskDesc(int index, String ModifiedContent) {
		Task u = new Task();
		RedoCommandList.add(new String("editTaskDesc"));
		u.setIndex(index);
		u.setTaskDesc(ModifiedContent);
		RedoList.add(u);
	}

	public void undoFlag(int index) {
		Task u = new Task();
		CommandList.push(new String("unflag"));
		u.setIndex(index);
		UndoList.push(u);
	}

	public void redoFlag(int index) {
		Task u = new Task();
		RedoCommandList.push(new String("flag"));
		u.setIndex(index);
		RedoList.push(u);
	}

	public void undoMark(int index) {
		Task u = new Task();
		CommandList.push(new String("unmark"));
		u.setIndex(index);
		UndoList.push(u);
	}

	public void redoMark(int index) {
		Task u = new Task();
		RedoCommandList.push(new String("mark"));
		u.setIndex(index);
		RedoList.push(u);
	}

	public void undoUnmark(int index) {
		Task u = new Task();
		CommandList.push(new String("mark"));
		u.setIndex(index);
		UndoList.push(u);
	}

	public void redoUnmark(int index) {
		Task u = new Task();
		RedoCommandList.push(new String("unmark"));
		u.setIndex(index);
		RedoList.push(u);
	}

	public void undoUnflag(int index) {
		Task u = new Task();
		CommandList.push(new String("flag"));
		u.setIndex(index);
		UndoList.push(u);
	}

	public void redoUnflag(int index) {
		Task u = new Task();
		RedoCommandList.push(new String("unflag"));
		u.setIndex(index);
		RedoList.push(u);
	}

}
