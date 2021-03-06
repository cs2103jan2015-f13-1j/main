//@author A0111855J
package fileIO;

import java.io.File;
import java.util.Vector;
import java.util.prefs.Preferences;

import util.Task;
import javafx.stage.DirectoryChooser;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.Marshaller;

public class FileStream {

	private static File file;
	private static String oldPath;
	private static String newPath;

	/**
	 * Retrieves XML from Ontask.xml, converts and returns a Vector<Task>
	 * @return
	 */
	public static Vector<Task> loadTasksFromXML() {
		Vector<Task> tasks = new Vector<Task>();

		try {
			JAXBContext context = JAXBContext.newInstance(TaskListWrapper.class);
			Unmarshaller um = context.createUnmarshaller();

			//Read XML from file and unmarshal.
			TaskListWrapper wrapper = (TaskListWrapper) um.unmarshal(file);	
			if(wrapper.getTasks() != null) {
				tasks.addAll(wrapper.getTasks());
			}
		} catch (JAXBException e) {
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

	public static void initializeDir() {

		/*
		 * If else statement to catch two cases: 
		 * 1) If preference has already been initialized but cannot locate Ontask.xml, treat as first-time user. file = null
		 * 2) If preference is not initialized (real first-time users), file = null
		 */
		if(getFilePath().exists() && getFilePath() != null) {	
			file = getFilePath();
		} else {
			file = null;
		}

		if(file == null) {
			//Ask user for directory to save Ontask.xml
			DirectoryChooser dirChooser = new DirectoryChooser();
			File selectedDirectory = dirChooser.showDialog(null);

			while(selectedDirectory == null) {
				dirChooser.setTitle("Please Select a Folder");
				selectedDirectory = dirChooser.showDialog(null);
			} 

			newPath = selectedDirectory.getAbsolutePath();
			file = new File(newPath + "/Ontask.xml");
			setFilePath(file);
			writeTasksToXML(new Vector<Task>());
			//System.out.println(newPath + "/Ontask.xml");
			//System.out.println("end of initializeDir()");
		}
	}

	/*
	 * Copy the old Ontask.xml to new directory. Delete old directory Ontask.xml
	 */
	public static void changeDir() {
		oldPath = getFilePath().getParentFile().getAbsolutePath();

		DirectoryChooser dirChooser = new DirectoryChooser();
		File selectedDirectory = dirChooser.showDialog(null);

		//If user did not select a directory, this method will not make any changes.
		if(selectedDirectory == null) {
			return;
		} else {
			newPath = selectedDirectory.getAbsolutePath();

			//Copy old Ontask.xml to new
			file.renameTo(new File(newPath + "/Ontask.xml"));

			//Update file references
			file = new File(newPath + "/Ontask.xml");
			setFilePath(file);

		}

	}

	public static void changeDirWithString(String s) {
		
		assert s != null : "String is null!";

		//Copy old Ontask.xml to new
		file.renameTo(new File(s + "/Ontask.xml"));

		//Update file references
		file = new File(s + "/Ontask.xml");
		setFilePath(file);
	}

	public static String getOldPath() {
		return oldPath;
	}

	public static String getNewPath() {
		return newPath;
	}

	/*
	 * Preference API stores the value of File using a string key filePath
	 */
	public static File getFilePath() {
		Preferences prefs = Preferences.userNodeForPackage(FileStream.class);
		String filePath = prefs.get("filePath", null);
		if(filePath != null) {
			return new File(filePath);
		} else {
			return null;
		}
	}

	/*
	 * Updates preference to store new value for File with the same string key filePath
	 */
	private static void setFilePath(File file) {

		assert file != null : "File is null!";
		
		Preferences prefs = Preferences.userNodeForPackage(FileStream.class);
		if(file != null) {
			prefs.put("filePath", file.getPath());
		} else {
			prefs.remove("filePath");
		}
	}
}
