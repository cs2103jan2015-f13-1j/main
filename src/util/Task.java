package util;

import java.time.LocalDateTime;

import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import util.OperationType.COMMAND_TYPE;

public class Task {

	private String taskDesc;

	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private TASK_TYPE type;
	private boolean isDone = false;
	private boolean isDue = false;
	private boolean flag = false;
	private int index;

	public static enum TASK_TYPE {
		TIMED_TASK, FLOATING_TASK, DEADLINE, RECURRING_TASK, NULL;
	}

	private void setTimeFormat() {
		// TODO Auto-generated method stub

	}


	public Task() {
		this.type = TASK_TYPE.NULL;
	}

	public Task(String desc) {

		setTimeFormat();
		this.taskDesc = desc;
		this.type = TASK_TYPE.FLOATING_TASK;
	}

	public Task(String desc, LocalDateTime et) {
		this.taskDesc = desc;
		this.endTime = et;
		this.type = TASK_TYPE.DEADLINE;
	}

	public Task(String desc, LocalDateTime st, LocalDateTime et)
			throws Exception {
		this.taskDesc = desc;
		this.startTime = st;
		this.endTime = et;
		this.type = TASK_TYPE.TIMED_TASK;
		if (st.isAfter(et)) {
			throw new Exception("End time cannot be earlier than Start time");
		}
	}
	
	public boolean flag(){
		return flag;
	}
	
	public boolean isDone(){
		return isDone;
	}
	
	public boolean isDue(){
		return isDue;
	}
	
	public void markTaskAsDue() {
		isDue = true;
	}
	
	public void markTaskAsUndue() {
		isDue = false;
	}
	
	public void markTaskAsDone() {
		isDone = true;
	}
	
	public void markTaskAsUndone() {
		isDone = false;
	}
	
	public void markFlag() {
		flag = true;
	}
	
	public void unmarkFlag() {
		flag = false;
	}

	public void setTaskDesc(String desc) {
		taskDesc = desc;
	}

	public void setStartTime(LocalDateTime st) {
		startTime = st;
	}

	public void setEndTime(LocalDateTime et) {
		endTime = et;
	}

	public String getTaskDesc() {
		return taskDesc;
	}

	public TASK_TYPE getTaskType() {
		return type;
	}
	
	public void setTaskType(TASK_TYPE t) {
		type = t;
	}
	
	public void setFinish(){
		isDone = true;
	}

	@XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
	@XmlSchemaType(name = "startTime")
	public LocalDateTime getStartTime() {
		return startTime;
	}

	@XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
	@XmlSchemaType(name = "endTime")
	public LocalDateTime getEndTime() {
		return endTime;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;		
	}

}
