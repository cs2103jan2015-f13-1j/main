package parser;

import java.time.LocalDateTime;

import util.Task;
import util.TimeExtractor;

public class TaskBuilder {
	private String _input;
	private Task t;

	

	public TaskBuilder(String s) {
		_input = s;
	}

	public Task extractAddCommand() {
		Task.TASK_TYPE type = checkTaskType();
		extractTaskInfo(type);
		return t;
	}

	private void extractTaskInfo(Task.TASK_TYPE type) {
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
		t = new Task(_input.trim());
	}

	private void buildDeadline() {
		String[] info = _input.split("by");
		String desc = info[0].trim();
		LocalDateTime EndTime = extractTime(info[1].trim());
		t = new Task(desc, EndTime);
		
	}

	private void buildTimedTask() {
		try{
		String[] info = _input.split("from |to ");	
		String desc = info[0].trim();
		LocalDateTime StartTime = extractTime(info[1].trim());
		LocalDateTime EndTime =extractTime(info[2].trim());
		t = new Task(desc, StartTime, EndTime);
	}catch(Exception e){
		}
	}

	private LocalDateTime extractTime(String str) {
		try{
			return TimeExtractor.getTime(str);
		}catch(Exception e){
			//return timeOperator.extractDate(str);
			return null;
		}
	}

	private Task.TASK_TYPE checkTaskType() {
		if (_input.toLowerCase().contains(new String("by"))) {
			return Task.TASK_TYPE.DEADLINE;
		} else if (_input.toLowerCase().contains(new String("from"))) {
			return Task.TASK_TYPE.TIMED_TASK;
		} else {
			return Task.TASK_TYPE.FLOATING_TASK;
		}

	}

}
