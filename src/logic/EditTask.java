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
	private static final String MSG_EDIT = "Task edited successfully.\n";
	private static final String MSG_TASK_FAILURE = "Task %s does not exist! Edit Failed.\n";
	private static final String MSG_EDIT_FAILURE = "Edit %d %s failed!\n";
	private static final String MSG_TIME_FAILURE = "End time cannot be earlier than start time!\n";
	private static final String MSG_DESC_FAILURE = "Task Description cannot be empty!\n";

	//@author A0105952H
	public EditTask(Vector<Task> TaskList) {
		this.TaskList = TaskList;
	}

	/**
	 * switch to different types of editing according to command
	 * 
	 * @param cmd
	 * @return
	 */
	public boolean editTask(Command cmd) {

		int index = cmd.getIndex();
		String editType = cmd.getContent();
		String modifiedContent = cmd.getModifiedString();

		if (index > 0 && index <= TaskList.size()) {
			switch (editType.toLowerCase()) {
			case "task":
				isSuccessful = editTaskDesc(index, modifiedContent);
				break;
			case "desc":
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
			default:
				Output.showToUser(String.format(MSG_EDIT_FAILURE, index,
						editType));
				break;
			}
			if (isSuccessful) {
				Output.showToUser(MSG_EDIT);
			} else {
				Output.showToUser(String.format(MSG_EDIT_FAILURE, index,
						editType));
			}
		} else {

			Output.showToUser(String.format(MSG_TASK_FAILURE, index));
		}
		return isSuccessful;
	}

	/**
	 * 
	 Change start date of task if new start time is earlier than end time
	 */
	private boolean editTaskStartDate(int index, String modifiedContent) {
		try {
			Task editTask = TaskList.get(index - 1);
			LocalDateTime startTime = editTask.getStartTime();
			LocalDateTime modifiedTime;

			if (startTime != null) {
				LocalDate t = TimeExtractor.extractDate(modifiedContent);
				modifiedTime = LocalDateTime.of(t, startTime.toLocalTime());

				if (modifiedTime.isBefore(editTask.getEndTime())) {
					editTask.setStartTime(modifiedTime);

					Sort s = new Sort(TaskList);
					s.sortList();
					Logic.u.undoEditStartDate(editTask.getIndex(), startTime);
					Logic.u.redoEditStartDate(index, modifiedTime);
					return true;
				}
				Output.showToUser(MSG_TIME_FAILURE);
			}
			return false;

		} catch (NullPointerException e) {
			Output.showToUser(String.format(MSG_TASK_FAILURE, "start date for "
					+ index));
			return false;
		}
	}

	private boolean editTaskEndDate(int index, String modifiedContent) {
		try {
			Task editTask = TaskList.get(index - 1);
			LocalDateTime endTime = editTask.getEndTime();
			LocalDateTime modifiedTime;

			if (endTime != null) {

				LocalDate t = TimeExtractor.extractDate(modifiedContent);
				modifiedTime = LocalDateTime.of(t, endTime.toLocalTime());

				if (editTask.getStartTime() == null
						|| modifiedTime.isAfter(editTask.getStartTime())) {
					editTask.setEndTime(modifiedTime);

					Sort s = new Sort(TaskList);
					s.sortList();
					Logic.u.undoEditEndDate(editTask.getIndex(), endTime);
					Logic.u.redoEditEndDate(index, modifiedTime);

					return true;
				}
				Output.showToUser(MSG_TIME_FAILURE);
			}
			return false;

		} catch (NullPointerException e) {
			Output.showToUser(String.format(MSG_TASK_FAILURE, "end date for "
					+ index));
			return false;
		}
	}

	private boolean editTaskStartTime(int index, String modifiedContent) {
		try {
			Task editTask = TaskList.get(index - 1);
			LocalDateTime startTime = editTask.getStartTime();
			LocalDateTime modifiedTime;

			if (startTime != null) {
				LocalTime t = TimeExtractor.extractTime(modifiedContent);

				modifiedTime = LocalDateTime.of(startTime.toLocalDate(), t);
				if (modifiedTime.isBefore(editTask.getEndTime())) {
					editTask.setStartTime(modifiedTime);

					Sort s = new Sort(TaskList);
					s.sortList();
					Logic.u.undoEditStartTime(editTask.getIndex(), startTime);
					Logic.u.redoEditStartTime(index, modifiedTime);
					return true;
				}
				Output.showToUser(MSG_TIME_FAILURE);
			}
			return false;
		} catch (NullPointerException e) {
			Output.showToUser(String.format(MSG_TASK_FAILURE, "start time for "
					+ index));
			return false;
		}
	}

	private boolean editTaskEndTime(int index, String modifiedContent) {
		try {
			Task editTask = TaskList.get(index - 1);
			LocalDateTime endTime = editTask.getEndTime();
			LocalDateTime modifiedTime;

			if (endTime != null) {
				LocalTime t = TimeExtractor.extractTime(modifiedContent);

				modifiedTime = LocalDateTime.of(endTime.toLocalDate(), t);
				if (editTask.getStartTime() == null
						|| modifiedTime.isAfter(editTask.getStartTime())) {
					editTask.setEndTime(modifiedTime);

					Sort s = new Sort(TaskList);
					s.sortList();
					Logic.u.undoEditEndTime(editTask.getIndex(), endTime);
					Logic.u.redoEditEndDate(index, modifiedTime);
					return true;
				}
				Output.showToUser(MSG_TIME_FAILURE);
			}
			return false;
		} catch (NullPointerException e) {
			Output.showToUser(String.format(MSG_TASK_FAILURE, "end time for "
					+ index));
			return false;
		}
	}

	private boolean editTaskDesc(int index, String modifiedContent) {
		try {
			Task editTask = TaskList.get(index - 1);
			String originalContent = editTask.getTaskDesc();

			if (!modifiedContent.isEmpty()) {
				editTask.setTaskDesc(modifiedContent);
				Sort s = new Sort(TaskList);
				s.sortList();
				Logic.u.undoEditTaskDesc(editTask.getIndex(), originalContent);
				Logic.u.redoEditTaskDesc(index, modifiedContent);
				return true;
			}
			Output.showToUser(String.format(MSG_DESC_FAILURE, index));
			return false;
		} catch (IndexOutOfBoundsException e) {
			Output.showToUser(String.format(MSG_TASK_FAILURE, index));
			return false;
		}
	}
}
