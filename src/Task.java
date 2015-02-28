import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


//Object-oriented class for Task
public class Task {

	private final StringProperty taskDesc;
	private final StringProperty date;
	private final StringProperty startTime;
	private final StringProperty endTime;
	
	//default constructor
	public Task() {
		this(null, null);
	}
	
	//constructor with data
	public Task(String taskDesc, String date) {
		this.taskDesc = new SimpleStringProperty(taskDesc);
		this.date = new SimpleStringProperty(date);
		
		//dummy Data, for testing
		this.startTime = new SimpleStringProperty("1200");
		this.endTime = new SimpleStringProperty("2359");
	}
	
	public String getTaskDesc() {
		return taskDesc.get();
	}
	
	public void setTaskDesc(String taskDesc) {
		this.taskDesc.set(taskDesc);
	}
	
	public StringProperty taskDescProperty() {
		return taskDesc;
	}
	
	public String getDate() {
		return date.get();
	}
	
	public void setDate(String date) {
		this.date.set(date);
	}
	
	public StringProperty dateProperty() {
		return date;
	}
	
	public String getStartTime() {
		return startTime.get();
	}
	
	public void setStartTime(String startTime) {
		this.startTime.set(startTime);
	}

	public StringProperty startTimeProperty() {
		return startTime;
	}
	
	public String getEndTime() {
		return endTime.get();
	}
	
	public void setEndTime(String endTime) {
		this.endTime.set(endTime);
	}
	
	public StringProperty endTimeProperty() {
		return endTime;
	}
}
