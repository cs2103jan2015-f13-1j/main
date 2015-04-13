//@author A0111855J
package util;

import gui.TaskDisplayController;
import junitx.framework.*;
import java.io.File;
import javafx.application.Application;
import javafx.stage.Stage;
import org.junit.BeforeClass;
import org.junit.Test;

public class SystemTest {

	int i=0;

	public static class ListViewGUI extends Application {

		@Override
		public void start(Stage primaryStage) throws Exception {
			// TODO Auto-generated method stub
		}
	}

	@BeforeClass
	public static void initJFX() throws InterruptedException {
		Thread t = new Thread("JavaFX Init Thread") {
			public void run() {
				Application.launch(ListViewGUI.class, new String[0]);
			}
		};
		t.setDaemon(true);
		t.start();
		Thread.sleep(500);
	}


	@Test
	public void test() throws Exception {
		TaskDisplayController tdc = new TaskDisplayController();
		//Testing command: clear, at the same time, initializes a new clean task for the following test
		tdc.processUserInput("add Start test");
		tdc.processUserInput("clear");
		checkCurrentAgainstExpectedXML("Ontask.xml", "Empty.xml");
		
		//Testing time formats: TT:TT (24 hour clock)
		tdc.processUserInput("add timed task 30 APR 3:25 1 MAY 17:25");
		
		//Testing time formats: T.TT(with suffix, 12 hour clock)
		tdc.processUserInput("add timed task from 30 apr 9pm to 1 may 4.13pm");
		
		//Testing date formatings DD MMM T(with suffix)
		tdc.processUserInput("add timed task from 30 may 3pm to 1 jun 5pm");

		//Testing date formatings MM DD with both time formats, represents June 15 3pm to June 16 5pm
		tdc.processUserInput("add timed task from 6 15 15:00 to 6 16 5pm");
		
		//Testing a past date, should reflect 2016 automatically
		tdc.processUserInput("add timed task from 1 Jan 15:00 to 2 Feb 5pm");

		//Testing format for due task with keyword 'by'
		tdc.processUserInput("add due TASK by Friday 14:00");
		
		//Testing format for due task without keyword 'by'
		tdc.processUserInput("add due task Fri 5pm");
		
		//Testing special words: today, tomorrow
		tdc.processUserInput("add timed task today 3.25pm tomorrow 5.00pm");
		
		//Testing special words: tdy , tmr 
		tdc.processUserInput("add timed task tdy 3.25pm tmr 5.00pm");
		
		//Check command: delete, should delete 1, update indexes, and delete task 1 again
		tdc.processUserInput("delete 1");
		tdc.processUserInput("delete 1");
		
		//Testing a date input in task description with " ", upper and lower case form
		tdc.processUserInput("add FloAtinG task by \"3 aPr\"");
		
		//Testing a date input in task description for due task
		tdc.processUserInput("add WATCH-movie, 'Day after \"tmr\"' by 20 MAY");
		
		//Testing edit for task description
		tdc.processUserInput("edit task 1 new task description");
		
		//Testing edit for endtime
		tdc.processUserInput("edit endtime 1 5.00pm");
		
		//Testing edit for enddate 
		tdc.processUserInput("edit enddate 1 9 apr");
		
		//Testing edit for starttime, should not display error because due task do not have a start time
		tdc.processUserInput("edit starttime 1");
		
		//Test edit starttime and startdate
		tdc.processUserInput("edit starttime 3 1.25am");
		tdc.processUserInput("edit startdate 3 apr 25");
		
		//Test marking task as done
		tdc.processUserInput("mark 2");
		
		//Test flagging task to prioritize
		tdc.processUserInput("flag 3");
		
		//Test undo function
		tdc.processUserInput("add task to be undo-ed, should not appear");
		tdc.processUserInput("undo");
		
		//Test final command exit
		tdc.processUserInput("exit");
		
		//Weird cases which fail: 
		//add friday, add 3pm
		//add i want to do this TODAY!
		//add
		//auto complete for edit bugs
		
		checkCurrentAgainstExpectedXML("Ontask.xml", "Expected.xml");
	}

	public void checkCurrentAgainstExpectedXML(String currentS, String expectedS) {
		File current = new File(currentS);
		File expected = new File(expectedS);
		
		//Import JUnit-addons version 1.4 in order to use FileAssert under junitx.framework.*;
		FileAssert.assertEquals(expected, current);
	}
}
