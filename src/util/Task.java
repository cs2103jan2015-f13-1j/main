package util;
import java.time.LocalDateTime;
import java.util.Date;

public class Task {

	private String taskDesc;
	private LocalDateTime startTime;
	private LocalDateTime endTime;

	// private String _taskType

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

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

}
