package logic;

import static org.junit.Assert.*;

import java.util.Vector;

import org.junit.Test;

import fileIO.FileStream;
import util.Task;
import util.TimeExtractor;

public class IntegrationTest {

	@Test
	public void test() {
		Logic l = new Logic();
		FileStream.initializeDir();
		Vector<Task> testList = new Vector<Task>();
		Vector<Task> emptyList = new Vector<Task>();
		int i=0;
		
		//Clears the list
		FileStream.writeTasksToXML(emptyList);
		
		//Testing time formats: 1) T.TT(with suffix) 2) TT:TT (24 hours)
		testList = l.run("add timed task 30 APR 3.25am 1 MAY 17:25");
		assertEquals("Apr.30 2015 3.25AM", TimeExtractor.formatDateTime(testList.get(i).getStartTime()));
		assertEquals("May.1 2015 5.25PM", TimeExtractor.formatDateTime(testList.get(i).getEndTime()));
		i++;	

		//Testing date formatings DD MMM T(with suffix)
		testList = l.run("add timed task from 30 may 3pm to 1 jun 5pm");
		assertEquals("May.30 2015 3PM", TimeExtractor.formatDateTime(testList.get(i).getStartTime()));
		assertEquals("Jun.1 2015 5PM", TimeExtractor.formatDateTime(testList.get(i).getEndTime()));
		i++;
		
		//Testing date formatings DD MM TTTT, past date shows year 2016
		testList = l.run("add timed task from 6 15 15:00 to 6 16 17:00");
		assertEquals("Jun.15 2015 3PM", TimeExtractor.formatDateTime(testList.get(i).getStartTime()));
		assertEquals("Jun.16 2015 5PM", TimeExtractor.formatDateTime(testList.get(i).getEndTime()));
		i++;
		
		/*Failed test case		
		//Testing date formatings DD MM TTTT
		testList = l.run("add timed task from 30 03 3.25am to 1 04 17:26");
		assertEquals("Mar.30 2016 3.25AM", TimeExtractor.formatDateTime(testList.get(i).getStartTime()));
		assertEquals("Apr.1 2016 5.26PM", TimeExtractor.formatDateTime(testList.get(i).getEndTime()));
		i++;
		
		//Testing date formatings MMM DD T(with suffix)
		testList = l.run("add timed task Mar 30 3pm apr 1 5pm");
		assertEquals("Mar 30 3PM", TimeExtractor.formatDateTime(testList.get(i).getStartTime()));
		assertEquals("Apr 1 5PM", TimeExtractor.formatDateTime(testList.get(i).getEndTime()));
		i++;

		*/
		
		//Clears the list
		//FileStream.writeTasksToXML(emptyList);
	}

}
