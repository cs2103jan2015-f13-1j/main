package logic;

import java.time.LocalDateTime;
import java.util.Stack;
import java.util.Vector;

import util.Output;
import util.Task;

public class UndoOps {

	private Stack<Task> UndoList = new Stack<Task>();
	private Stack<String> CommandList = new Stack<String>();
	private static final String MSG_COMMAND_FAILURE = "Command: %s failed!\n";
	
	
	public String undoOperation(Vector<Task> TaskList) {
		if (UndoList != null) {
			Task u = UndoList.pop();
			String cmd = CommandList.pop();
			switch (cmd) {
			case "add":
				TaskList.add(u);
				break;
			case "delete":
				Output.showToUser(TaskList.size()+"");
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
			}
			return Logic.MSG_UNDO;
			} 
		else {
			return String.format(MSG_COMMAND_FAILURE, "undo");
		}
	}
	
	public void undoAdd(int index) {
		Task u = new Task();
		u.setIndex(index);
		CommandList.push(new String("delete"));
		UndoList.push(u);
	}

	public void undoEditEndDate(int index, LocalDateTime endTime) {
		Task u = new Task();
		CommandList.push(new String("editEndDate"));
		u.setIndex(index);
		u.setEndTime(endTime);
		UndoList.push(u);
	}

	public void undoEditStartDate(int index, LocalDateTime startTime) {
		Task u = new Task();

		CommandList.push(new String("editStartDate"));
		u.setIndex(index);
		u.setStartTime(startTime);
		UndoList.push(u);
	}
	
	public void undoEditEndTime(int index, LocalDateTime endTime) {
		Task u = new Task();
		CommandList.push(new String("editEndTime"));
		u.setIndex(index);
		u.setEndTime(endTime);
		UndoList.push(u);
	}
	
	public void undoEditStartTime(int index, LocalDateTime startTime) {
		Task u = new Task();
		CommandList.push(new String("editStartTime"));
		u.setIndex(index);
		u.setStartTime(startTime);
		UndoList.push(u);
	}
	
	public void undoEditTaskDesc(int index, Task editTask) {
		Task u = new Task();
		CommandList.push(new String("editTaskDesc"));
		u.setIndex(index);
		u.setTaskDesc(editTask.getTaskDesc());
		UndoList.push(u);
	}

	public void undoDelete(Task u) {
		CommandList.push(new String("add"));
		UndoList.push(u);
	}

	public void undoClear() {
		// TODO Auto-generated method stub
		
	}

	public void undoChgdir() {
		// TODO Auto-generated method stub
		
	}
	
	
}
