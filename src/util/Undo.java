package util;
import java.time.LocalDateTime;

import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

public class Undo {
	private String command;
	private int index;
	private String taskDesc;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	
	public void setCommand(String cmd) {
		command = cmd;
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
	
	public void setIndex(int i) {
		index = i;
	}
	
	public String getCommand() {
		return command;
	}
	public String getTaskDesc() {
		return taskDesc;
	}
	
	public int getIndex() {
		return index;
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
