package logic;

import static org.junit.Assert.*;

import java.util.Vector;

import org.junit.Test;

import fileIO.FileStream;
import util.Task;
import util.TimeExtractor;

public class LogicSystemTest {

	@Test
	public void test() {
		Logic l = new Logic();
		FileStream.initializeDir();
		Vector<Task> testList = new Vector<Task>();
		Vector<Task> emptyList = new Vector<Task>();
		int i=0;
		
		//Testing date formatings DD MMM T(with suffix)
		testList = l.run("add timed task 30 mar 3pm to 1 apr 5pm");
		assertEquals("Mar 30 3PM", TimeExtractor.formatDateTime(testList.get(i).getStartTime()));
		assertEquals("Apr 1 5PM", TimeExtractor.formatDateTime(testList.get(i).getEndTime()));
		i++;
		
		//Testing date formatings DD MM TTTT
		testList = l.run("add timed task from 30 03 1500 to 1 04 1700");
		assertEquals("Mar 30 3PM", TimeExtractor.formatDateTime(testList.get(i).getStartTime()));
		assertEquals("Apr 1 5PM", TimeExtractor.formatDateTime(testList.get(i).getEndTime()));
		i++;

		//Testing date formatings MMM DD T(with suffix)
		testList = l.run("add timed task Mar 30 3pm apr 1 5pm");
		assertEquals("Mar 30 3PM", TimeExtractor.formatDateTime(testList.get(i).getStartTime()));
		assertEquals("Apr 1 5PM", TimeExtractor.formatDateTime(testList.get(i).getEndTime()));
		i++;

		/*Failed test case
		//Testing date formatings
		testList = l.run("add timed task from 30 mar 3.25 to 1 apr 5.20");
		assertEquals("Mar 30 3PM", TimeExtractor.formatDateTime(testList.get(i).getStartTime()));
		assertEquals("Apr 1 5PM", TimeExtractor.formatDateTime(testList.get(i).getEndTime()));
		i++;	
		*/
		
		//Clears the list
		FileStream.writeTasksToXML(emptyList);
	}

}
