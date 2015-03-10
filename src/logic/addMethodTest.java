package logic;

import FileStream.FileStream;
import util.Task;
import FileStream.TaskListWrapper;

import java.time.LocalDateTime;

import util.timeOperator;

import java.util.Vector;

public class addMethodTest {
	
	public static void main(String[] args) {
		Task t = new Task();
		t.setTaskDesc("meeting");
		t.setStartTime(timeOperator.getTime("mar 02"));
		t.setEndTime(timeOperator.getTime("mar 04"));
		
		Vector<Task> list = new Vector<Task>();
		list.add(t);
		
		FileStream.writeTasksToXML(list);
	}

}
