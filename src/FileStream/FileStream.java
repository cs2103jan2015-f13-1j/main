package FileStream;

import java.io.File;
import java.util.Vector;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.Marshaller;

public class FileStream {

	
	public static void main(String[] args) {

		
		//---------- Create a dummy list of Task --------
		Vector<Task> dummyList = new Vector<Task>();
		dummyList.add(new Task("taskDesc", "30-08-1991", "09-03-2015"));
		dummyList.add(new Task("taskDesc2", "30-08-1991", "09-03-2015"));
		dummyList.add(new Task("taskDesc3", "30-08-1991", "09-03-2015"));
		dummyList.add(new Task("taskDesc4", "30-08-1991", "09-03-2015"));
		dummyList.add(new Task("taskDesc5", "30-08-1991", "09-03-2015"));
		dummyList.add(new Task("taskDesc6", "30-08-1991", "09-03-2015"));
		dummyList.add(new Task("taskDesc7", "30-08-1991", "09-03-2015"));
		//-----------------------------------------------
		
		//writeTasksToXML(dummyList);
		//System.out.println("Saved");	
		
		Vector<Task> newList = new Vector<Task>();
		newList = loadTasksFromXML();		
		System.out.println("Retrieved");
		
		//--------- Print newList ----------------------
		for(Task t: newList) {
			System.out.println(t);
		}
		//-----------------------------------------------
		
	}
	
	/**
	 * Retrieves XML from Ontask.xml, converts and returns a Vector<Task>
	 * @return
	 */
	public static Vector<Task> loadTasksFromXML() {
		Vector<Task> tasks = new Vector<Task>();
		
		try {
			File file = new File("Ontask.xml");
			JAXBContext context = JAXBContext.newInstance(TaskListWrapper.class);
			Unmarshaller um = context.createUnmarshaller();
			
			//Read XML from file and unmarshal.
			TaskListWrapper wrapper = (TaskListWrapper) um.unmarshal(file);
			
			tasks.addAll(wrapper.getTasks());
			
		} catch (JAXBException e) {
			//System.out.println("Unable to load, file does not exist");
			e.printStackTrace();
		}
		
		return tasks;
	}
	
	/**
	 * Save specified Vector<Task> to Ontask.xml 
	 * @param tasks
	 */
	public static void writeTasksToXML(Vector<Task> tasks) {
		try {
			
			File file = new File("Ontask.xml");
			JAXBContext context = JAXBContext.newInstance(TaskListWrapper.class);
			Marshaller m = context.createMarshaller();
			
			//Formatted with lines and indentation
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			
			//Wrap vector of tasks
			TaskListWrapper wrapper = new TaskListWrapper();
			wrapper.setTasks(tasks);
			
			
			//Marshals and saves XML to the file
			m.marshal(wrapper, file);
			
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
}
