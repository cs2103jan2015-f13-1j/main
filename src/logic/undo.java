package logic;

import java.time.LocalDateTime;
import java.util.Stack;
import java.util.Vector;

import util.Task;
import util.Undo;

public class undo {

	private Stack<Undo> UndoList;
	private Vector<Task> TaskList;
	
	public undo(Stack<Undo> UndoList, Vector<Task> TaskList){
		this.TaskList = TaskList;
		this.UndoList = UndoList;
	}
	
	public String undoOperation() {
		if (UndoList != null) {
			Undo u = UndoList.pop();
			switch (u.getCommand()) {
			case "add":
				Task add = new Task();
				add.setTaskDesc(u.getTaskDesc());
				add.setStartTime(u.getStartTime());
				add.setEndTime(u.getEndTime());
				//add.setTaskType(u.g);
				TaskList.add((u.getIndex() - 1), add);
				break;
			case "delete":
				TaskList.remove(u.getIndex());
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
			return String.format(Logic.MSG_COMMAND_FAILURE, "undo");
		}
	}
	
	
	private void undoAdd() {
		Undo u = new Undo();
		u.setCommand("delete");
		u.setIndex(TaskList.size());
		UndoList.push(u);
	}

	private void undoEditEndDate(int index, LocalDateTime endTime) {
		Undo u = new Undo();
		u.setCommand("editEndDate");
		u.setIndex(index);
		u.setEndTime(endTime);
		UndoList.push(u);
	}

	private void undoEditStartDate(int index, LocalDateTime startTime) {
		Undo u = new Undo();
		u.setCommand("editStartDate");
		u.setIndex(index);
		u.setStartTime(startTime);
		UndoList.push(u);
	}
	
	private void undoEditEndTime(int index, LocalDateTime endTime) {
		Undo u = new Undo();
		u.setCommand("editEndTime");
		u.setIndex(index);
		u.setEndTime(endTime);
		UndoList.push(u);
	}
	
	private void undoEditStartTime(int index, LocalDateTime startTime) {
		Undo u = new Undo();
		u.setCommand("editStartTime");
		u.setIndex(index);
		u.setStartTime(startTime);
		UndoList.push(u);
	}
	
	private void undoEditTaskDesc(int index, Task editTask) {
		Undo u = new Undo();
		u.setCommand("editTaskDesc");
		u.setIndex(index);
		u.setTaskDesc(editTask.getTaskDesc());
		UndoList.push(u);
	}

	private void undoDelete(int index) {
		Undo u = new Undo();
		Task t = TaskList.get(index - 1);
		u.setCommand("add");
		u.setIndex(index);
		u.setTaskDesc(t.getTaskDesc());
		u.setStartTime(t.getStartTime());
		u.setEndTime(t.getEndTime());
		UndoList.push(u);
	}
}
