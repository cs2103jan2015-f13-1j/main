package filestream;

import java.util.Vector;
import util.Task;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/*
 * This class wraps a Vector of tasks to save to XML
 */
@XmlRootElement(name = "Tasks")
public class TaskListWrapper {
	
	private Vector<Task> tasks;

	@XmlElement(name = "Task")
	public Vector<Task> getTasks() {
		return tasks;
	}

	public void setTasks(Vector<Task> tasks) {
		this.tasks = tasks;
	}

}
