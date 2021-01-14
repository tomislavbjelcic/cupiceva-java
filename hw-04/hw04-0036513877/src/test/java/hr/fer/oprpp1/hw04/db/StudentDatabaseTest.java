package hr.fer.oprpp1.hw04.db;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;

public class StudentDatabaseTest {
	
	private static final IFilter ALWAYS_TRUE_FILTER = record -> true;
	private static final IFilter ALWAYS_FALSE_FILTER = record -> false;
	
	private final StudentDatabase db;
	
	public StudentDatabaseTest() throws IOException {
		db = StudentDatabaseLoader.load();
	}
	
	@Test
	public void testFilterAllTrue() {
		List<StudentRecord> records = db.filter(ALWAYS_TRUE_FILTER);
		
		int expected = 63;
		int actual = records.size();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testFilterAllFalse() {
		List<StudentRecord> records = db.filter(ALWAYS_FALSE_FILTER);
		
		int expected = 0;
		int actual = records.size();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testForJmbagReturnsNull() {
		String jmbag = "this doesn't exist";
		
		StudentRecord expected = null;
		StudentRecord actual = db.forJMBAG(jmbag);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testForJmbag() {
		String jmbag = "0000000004";
		
		StudentRecord expected = new StudentRecord(jmbag, "Božić", "Marin", 5);
		StudentRecord actual = db.forJMBAG(jmbag);
		
		assertEquals(expected.getJmbag(), actual.getJmbag());
		assertEquals(expected.getLastName(), actual.getLastName());
		assertEquals(expected.getFirstName(), actual.getFirstName());
		assertEquals(expected.getFinalGrade(), actual.getFinalGrade());
	}
	
}
