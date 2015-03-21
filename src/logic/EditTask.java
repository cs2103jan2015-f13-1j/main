package logic;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Vector;

import parser.Command;
import util.Task;
import util.Output;
import util.TimeExtractor;

public class EditTask {
	boolean isSuccessful;
	private Vector<Task> TaskList = new Vector<Task>();
	private static final String MSG_EDIT = "Task edited successfully";
	private static final String MSG_TASK_FAILURE = "Edit %s does not exist!\n";
	
	public EditTask(Vector<Task> TaskList){
		this.TaskList = TaskList;
	}

	void editTask(Command cmd) {
		
		int index = cmd.getIndex();
		String editType = cmd.getContent();
		String modifiedContent = cmd.getModifiedString();
		
		if (index > 0 && index <= TaskList.size()) {
			switch (editType.toLowerCase()) {
			case "task":
				isSuccessful = editTaskDesc(index, modifiedContent);
				break;
			case "starttime":
				isSuccessful = editTaskStartTime(index, modifiedContent);
				break;
			case "endtime":
				isSuccessful = editTaskEndTime(index, modifiedContent);
				break;
			case "startdate":
				isSuccessful = editTaskStartDate(index, modifiedContent);
				break;
			case "enddate":
				isSuccessful = editTaskEndDate(index, modifiedContent);
				break;
			}
			if (isSuccessful) {
				Output.showToUser (MSG_EDIT);
			}
		} else {
			Output.showToUser (String.format(MSG_TASK_FAILURE, index));
		}
	}

	private boolean editTaskEndDate(int index, String modifiedContent) {
		try {
			Task editTask = TaskList.get(index - 1);
			LocalDateTime endTime = editTask.getEndTime();

			if (endTime != null) {
				
				Logic.u.undoEditEndDate(index, endTime);
				
				LocalDate t = TimeExtractor.extractDate(modifiedContent);
				editTask.setEndTime(endTime.withYear(t.getYear())
						.withDayOfYear(t.getDayOfYear()));
				return true;
			} else {
				return false;
			}
		} catch (NullPointerException e) {
			Output.showToUser (String.format(MSG_TASK_FAILURE, "enddate for " + index));
			return false;
		}
	}

	
	
	private boolean editTaskStartDate(int index, String modifiedContent) {
		try {
			Task editTask = TaskList.get(index - 1);
			LocalDateTime startTime = editTask.getStartTime();

			if (startTime != null) {
				LocalDate t = TimeExtractor.extractDate(modifiedContent);
				
				Logic.u.undoEditStartDate(index, startTime);
				
				editTask.setStartTime(startTime.withYear(t.getYear())
						.withDayOfYear(t.getDayOfYear()));
				return true;
			} else {
				return false;
			}
		} catch (NullPointerException e) {
			Output.showToUser (String.format(MSG_TASK_FAILURE, "startdate for " + index));
			return false;
		}
	}
	

	private boolean editTaskEndTime(int index, String modifiedContent) {
		try {
			Task editTask = TaskList.get(index - 1);
			LocalDateTime endTime = editTask.getEndTime();

			if (endTime != null) {
				LocalTime t = TimeExtractor.extractTime(modifiedContent);
				
				Logic.u.undoEditEndTime(index, endTime);
				
				editTask.setEndTime(endTime.withHour(t.getHour()).withMinute(
						t.getMinute()));
				return true;
			} else {
				return false;
			}
		} catch (NullPointerException e) {
			Output.showToUser (String.format(MSG_TASK_FAILURE, "endtime for " + index));
			return false;
		}
	}
	

	private boolean editTaskStartTime(int index, String modifiedContent) {
		try {
			Task editTask = TaskList.get(index - 1);
			LocalDateTime startTime = editTask.getStartTime();

			if (startTime != null) {
				LocalTime t = TimeExtractor.extractTime(modifiedContent);
				
				Logic.u.undoEditStartTime(index, startTime);
				
				editTask.setStartTime(startTime.withHour(t.getHour())
						.withMinute(t.getMinute()));
				return true;
			} else {
				return false;
			}
		} catch (NullPointerException e) {
			Output.showToUser (String.format(MSG_TASK_FAILURE, "startdate for " + index));
			return false;
		}
	}
	

	private boolean editTaskDesc(int index, String modifiedContent) {
		try {
			Task editTask = TaskList.get(index - 1);
			
			Logic.u.undoEditTaskDesc(index, editTask);
			
			editTask.setTaskDesc(modifiedContent);
			return true;
		} catch (IndexOutOfBoundsException e) {
			Output.showToUser (String.format(MSG_TASK_FAILURE, index));
			return false;
		}
	}
}
