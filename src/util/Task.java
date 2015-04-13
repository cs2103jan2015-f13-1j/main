package util;

import java.time.LocalDateTime;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

public class Task {

	private String taskDesc;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private TASK_TYPE type;
	private boolean done;
	private boolean flag;
	private boolean isDue;
	private int index;

	public static enum TASK_TYPE {
		TIMED_TASK, FLOATING_TASK, DEADLINE, RECURRING_TASK, NULL;
	}

	public Task() {
		this.type = TASK_TYPE.NULL;
	}

	public Task(String desc) {
		this.taskDesc = desc;
		this.type = TASK_TYPE.FLOATING_TASK;
		this.done = false;
		this.flag = false;
	}

	public Task(String desc, LocalDateTime et) {
		this.taskDesc = desc;
		this.endTime = et;
		this.type = TASK_TYPE.DEADLINE;
		this.done = false;
		this.flag = false;
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
		this.done = false;
		this.flag = false;
	}

	//@author A0111855J
	@XmlSchemaType(name = "flag")
	public boolean getFlag() {
		return flag;
	}
	
	public void setFlag(boolean b) {
		flag = b;
	}

	@XmlSchemaType(name = "done")
	public boolean getDone() {
		return done;
	}
	
	public void setDone(boolean b) {
		done = b;
	}
	//@author A0105952H
	public void setFinish(){
		done = true;
	}
	
	public boolean isDue() {
		return isDue;
	}
	
	public void markTaskAsDue() {
		isDue = true;
	}
	
	public void markTaskAsUndue() {
		isDue = false;
	}
	
	public void markTaskAsDone() {
		done = true;
	}
	
	public void markTaskAsUndone() {
		done = false;
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
	
	//@author A0111855J
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
