package parser;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TaskBuilderTest {

	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

	@Before
	public void setUpStreams() {
		System.setOut(new PrintStream(outContent));
	}

	@After
	public void cleanUpStreams() {
		System.setOut(null);
	}

	@Test
	public void test() {

		// case 1
		TaskBuilder tb = new TaskBuilder("Bank holiday 8/14");
		LocalDateTime tm = LocalDateTime.of(LocalDate.of(2015, 8, 14),
				LocalTime.of(23, 59));
		tb.run();
		// assertEquals("2. b for boy", outContent.toString().trim());
		assertEquals("Bank holiday", tb.t.getTaskDesc());
		assertEquals(tm.toString(), tb.t.getEndTime().toString());

		// case 2
		tb = new TaskBuilder("Volleyball at 5pm");
		tm = LocalDateTime.of(LocalDate.now(), LocalTime.of(17, 00));
		tb.run();
		assertEquals("Volleyball", tb.t.getTaskDesc());
		assertEquals(tm.toString(), tb.t.getEndTime().toString());

		// case 3
		tb = new TaskBuilder(
				"lunch with John at \"Taco Tuesdays\" Friday 12 pm");
		tm = LocalDateTime.of(
				LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.FRIDAY)),
				LocalTime.of(12, 00));
		tb.run();
		assertEquals("lunch with John at Taco Tuesdays", tb.t.getTaskDesc());
		assertEquals(tm.toString(), tb.t.getEndTime().toString());

		// case 4
		tb = new TaskBuilder("Running w/ Pat 14:15 - 3 pm tomorrow");
		tm = LocalDateTime
				.of(LocalDate.now().plusDays(1), LocalTime.of(14, 15));

		tb.run();
		assertEquals("Running w/ Pat", tb.t.getTaskDesc());
		assertEquals(tm.toString(), tb.t.getStartTime().toString());
		tm = LocalDateTime
				.of(LocalDate.now().plusDays(1), LocalTime.of(15, 00));
		assertEquals(tm.toString(), tb.t.getEndTime().toString());

		// case 5
		tb = new TaskBuilder(
				"National Conference in Atlanta 9/23 - 9/26");
		tm = LocalDateTime.of(
				LocalDate.of(LocalDate.now().getYear(), 9, 23),
				LocalTime.of(0, 0));
		tb.run();
		assertEquals("National Conference in Atlanta", tb.t.getTaskDesc());
		assertEquals(tm.toString(), tb.t.getStartTime().toString());
		tm = LocalDateTime.of(
				LocalDate.of(LocalDate.now().getYear(), 9, 26),
				LocalTime.of(23, 59));
		assertEquals(tm.toString(), tb.t.getEndTime().toString());
	}

}
