package hr.fer.oprpp1.hw04.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Razred koji nudi metode za inicijalizaciju baze podataka zapisa studenata 
 * iz datoteke koja ima određen format.
 * 
 * @author Tomislav Bjelčić
 *
 */
public class StudentDatabaseLoader {
	
	private static final String DEFAULT_DATABASE_FILENAME = "database.txt";
	
	private StudentDatabaseLoader() {}
	
	public static StudentDatabase loadFromFile(String fileName) throws IOException {
		Objects.requireNonNull(fileName, "Predano ime tekstualne datoteke je null.");
		
		/*
		List<String> lines = Files.readAllLines(
				Paths.get(fileName),
				StandardCharsets.UTF_8
				);
		*/
		List<String> lines = null;
		try (BufferedReader br = new BufferedReader(new InputStreamReader(StudentDatabaseLoader.class.getClassLoader()
					.getResourceAsStream(fileName)))) {
			
			lines = br.lines().collect(Collectors.toList());
		}
		return new StudentDatabase(lines);
	}
	
	public static StudentDatabase load() throws IOException {
		return loadFromFile(DEFAULT_DATABASE_FILENAME);
	}
	
	
}
