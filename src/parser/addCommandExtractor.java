package parser;

import java.time.LocalDateTime;

import util.Task;
import util.operator;
import util.timeOperator;

public class addCommandExtractor {
	private String _input;
	private Task t;

	public static enum TASK_TYPE {
		TIMED_TASK, FLOATING_TASK, DEADLINE;
	}

	public addCommandExtractor(String s) {
		_input = s;
	}

	public Task extractAddCommand() {
		TASK_TYPE type = checkTaskType();
		extractTaskInfo(type);
		return t;
	}

	private void extractTaskInfo(TASK_TYPE type) {
		switch (type) {
		case TIMED_TASK:
			buildTimedTask();
			return;
		case DEADLINE:
			buildDeadline();
			return;
		case FLOATING_TASK:
			buildFloatingTask();
			return;
		default:
			break;
		}
	}

	private void buildFloatingTask() {
		t = new Task();
		t.setTaskDesc(_input.trim());
	}

	private void buildDeadline() {
		String[] info = _input.split("by");
		t = new Task();
		t.setTaskDesc(info[0].trim());
		LocalDateTime endTime = timeOperator.getTime(info[1].trim());
		t.setEndTime(endTime);
	}

	private void buildTimedTask() {
		String[] info = _input.split("from |to ");
		t = new Task();
		t.setTaskDesc(info[0].trim());
		LocalDateTime startTime = timeOperator.getTime(info[1].trim());
		t.setStartTime(startTime);
		LocalDateTime endTime = timeOperator.getTime(info[2].trim());
		t.setEndTime(endTime);
	}

	private TASK_TYPE checkTaskType() {
		if (_input.toLowerCase().contains(new String("by"))) {
			return TASK_TYPE.DEADLINE;
		} else if (_input.toLowerCase().contains(new String("from"))) {
			return TASK_TYPE.TIMED_TASK;
		} else {
			return TASK_TYPE.FLOATING_TASK;
		}

	}

}
