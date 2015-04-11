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

	// @author A0105952H
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

		// case 1 format M/dd
		TaskBuilder tb = new TaskBuilder("Bank holiday 8/14");
		LocalDateTime tm = LocalDateTime.of(LocalDate.of(2015, 8, 14),
				LocalTime.of(23, 59));
		tb.run();
		assertEquals("Bank holiday", tb.t.getTaskDesc());
		assertEquals(tm.toString(), tb.t.getEndTime().toString());

		// case 2 format 3pm/11.14pm
		tb = new TaskBuilder("Volleyball at 11.14pm");
		tm = LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 14));
		tb.run();
		assertEquals("Volleyball", tb.t.getTaskDesc());
		assertEquals(tm.toString(), tb.t.getEndTime().toString());

		// case 3 format with quotes
		tb = new TaskBuilder(
				"lunch with John at \"Taco Tuesdays\" Friday 12 pm");
		tm = LocalDateTime.of(
				LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.FRIDAY)),
				LocalTime.of(12, 00));
		tb.run();
		assertEquals("lunch with John at Taco Tuesdays", tb.t.getTaskDesc());
		assertEquals(tm.toString(), tb.t.getEndTime().toString());

		// case 4 format tomorrow, hh:mm, ha
		tb = new TaskBuilder("Running w/ Pat 14:15 - 3 pm tomorrow");
		tm = LocalDateTime
				.of(LocalDate.now().plusDays(1), LocalTime.of(14, 15));

		tb.run();
		assertEquals("Running w/ Pat", tb.t.getTaskDesc());
		assertEquals(tm.toString(), tb.t.getStartTime().toString());
		tm = LocalDateTime
				.of(LocalDate.now().plusDays(1), LocalTime.of(15, 00));
		assertEquals(tm.toString(), tb.t.getEndTime().toString());

		// case 5 dd/MM/uuuu
		tb = new TaskBuilder(
				"National Conference in Atlanta 23/9/2015 - 26/9/2015");
		tm = LocalDateTime.of(LocalDate.of(LocalDate.now().getYear(), 9, 23),
				LocalTime.of(0, 0));
		tb.run();
		assertEquals("National Conference in Atlanta", tb.t.getTaskDesc());
		assertEquals(tm.toString(), tb.t.getStartTime().toString());
		tm = LocalDateTime.of(LocalDate.of(LocalDate.now().getYear(), 9, 26),
				LocalTime.of(23, 59));
		assertEquals(tm.toString(), tb.t.getEndTime().toString());

		// case 6 dd/MM/uuuu hhmm
		tb = new TaskBuilder("CS2013 from 12/12/2015 12:30 to 12/12/2015 14:00");
		tm = LocalDateTime.of(LocalDate.of(2015, 12, 12), LocalTime.of(12, 30));
		tb.run();
		assertEquals("CS2013", tb.t.getTaskDesc());
		assertEquals(tm.toString(), tb.t.getStartTime().toString());
		tm = LocalDateTime.of(LocalDate.of(2015, 12, 12), LocalTime.of(14, 00));
		assertEquals(tm.toString(), tb.t.getEndTime().toString());

		// case 7 MMMM d hh:mm
		tb = new TaskBuilder("Conference jan 1 13:00");
		tm = LocalDateTime.of(LocalDate.of(LocalDate.now().getYear(), 1, 1),
				LocalTime.of(13, 00));
		if (tm.isBefore(LocalDateTime.now())) {
			tm = tm.plusYears(1);
			System.out.println("hh");
		}
		tb.run();
		assertEquals("Conference", tb.t.getTaskDesc());
		assertEquals(tm.toString(), tb.t.getEndTime().toString());

		// case 8 d MMM hh:mm
		tb = new TaskBuilder("Conference 1 jan 13:00");
		tm = LocalDateTime.of(LocalDate.of(LocalDate.now().getYear(), 1, 1),
				LocalTime.of(13, 00));
		if (tm.isBefore(LocalDateTime.now())) {
			tm = tm.plusYears(1);
		}
		tb.run();
		assertEquals("Conference", tb.t.getTaskDesc());
		assertEquals(tm.toString(), tb.t.getEndTime().toString());

		// case 9 MMMM.d hh:mm
		tb = new TaskBuilder("Conference January.1 13:00");
		tm = LocalDateTime.of(LocalDate.of(LocalDate.now().getYear(), 1, 1),
				LocalTime.of(13, 00));
		if (tm.isBefore(LocalDateTime.now())) {
			tm = tm.plusYears(1);
		}
		tb.run();
		assertEquals("Conference", tb.t.getTaskDesc());
		assertEquals(tm.toString(), tb.t.getEndTime().toString());

		// case 10 dd MM
		tb = new TaskBuilder("t3 3 mar");
		tm = LocalDateTime.of(LocalDate.of(LocalDate.now().getYear(), 3, 3),
				LocalTime.of(0, 0));
		if (tm.isBefore(LocalDateTime.now())) {
			tm = tm.plusYears(1);
		}
		tb.run();
		assertEquals("t3", tb.t.getTaskDesc());
		assertEquals(tm.toString(), tb.t.getStartTime().toString());
		tm = LocalDateTime.of(LocalDate.of(LocalDate.now().getYear(), 3, 3),
				LocalTime.of(23, 59));
		if (tm.isBefore(LocalDateTime.now())) {
			tm = tm.plusYears(1);
		}
		assertEquals(tm.toString(), tb.t.getEndTime().toString());

		// case 11 no date, 2 times, assume today
		tb = new TaskBuilder("Meet professor 3pm 5.25pm");
		tm = LocalDateTime.of(LocalDate.now(), LocalTime.of(15, 0));
		tb.run();
		assertEquals("Meet professor", tb.t.getTaskDesc());
		assertEquals(tm.toString(), tb.t.getStartTime().toString());
		tm = LocalDateTime.of(LocalDate.now(), LocalTime.of(17, 25));
		assertEquals(tm.toString(), tb.t.getEndTime().toString());

		// case 12 failed case: from without to
		tb = new TaskBuilder("add P-P from jan 9 2015 23:20");
		tb.run();
		assertEquals(tb.t, null);

	}

}
