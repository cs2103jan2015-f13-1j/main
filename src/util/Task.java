package util;
import java.time.LocalDateTime;

import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

public class Task {

	private String taskDesc;
	private LocalDateTime startTime;
	private LocalDateTime endTime;

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

	@XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
	@XmlSchemaType(name="startTime")
	public LocalDateTime getStartTime() {
		return startTime;
	}


	@XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
	@XmlSchemaType(name="endTime")
	public LocalDateTime getEndTime() {
		return endTime;
	}

}
