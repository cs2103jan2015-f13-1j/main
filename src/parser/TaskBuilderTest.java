package parser;

import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

		TaskBuilder tb = new TaskBuilder("Bank holiday 8/14");
		LocalDateTime tm = LocalDateTime.of(LocalDate.of(2015, 8, 14),
				LocalTime.of(23, 59));
		tb.run();
		// assertEquals("2. b for boy", outContent.toString().trim());
		assertEquals("Bank holiday", tb.t.getTaskDesc());
		assertEquals(tm.toString(), tb.t.getEndTime().toString());
	}

}
