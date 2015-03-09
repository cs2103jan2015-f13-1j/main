package FileStream;

public class Task {
	
	private String taskDesc;
	private String startTime;
	private String endTime;
	
	//default constructor
	public Task() {
		this(null, null, null);
	}

	public Task(String taskDesc, String startTime, String endTime) {
		this.taskDesc = taskDesc;
		this.startTime = startTime;
		this.endTime = endTime;
	}
	
	public String getTaskDesc() {
		return taskDesc;
	}
	
	public void setTaskDesc(String taskDesc) {
		this.taskDesc = taskDesc;
	}
	
	public String getStartTime() {
		return startTime;
	}
	
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	
	public String getEndTime() {
		return endTime;
	}
	
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String toString() {
		return "Task: " + this.taskDesc + ", " + this.startTime + ", " + this.endTime;
	}
}
