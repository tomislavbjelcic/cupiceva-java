package hr.fer.oprpp1.hw04.db;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Razred sadrži main metodu koja pokreće program koji iz standardnog ulaza prima upite 
 * i rezultate vraća na standardni izlaz.
 * 
 * @author Tomislav Bjelčić
 *
 */
public class StudentDatabaseEmulator {
	
	/**
	 * Komanda za upit.
	 */
	private static final String QUERY_COMMAND = "query";
	/**
	 * Komanda za izlazak iz programa.
	 */
	private static final String EXIT_COMMAND = "exit";
	/**
	 * Niz koji se ispisuje prije unosa svakog upita.
	 */
	private static final String COMMAND_START = "> ";
	/**
	 * Poruka koja se ispisuje prilikom izlaska iz programa.
	 */
	private static final String EXIT_MSG = "Goodbye!";
	/**
	 * Baza podataka nad kojom se obavljaju upiti.
	 */
	private static StudentDatabase db;
	
	/**
	 * Program koji sa standardnog ulaza prima upite i rezultate ispisuje na 
	 * standardni izlaz.
	 */
	public static void main(String[] args) throws IOException {
		db = StudentDatabaseLoader.load();
		
		try(BufferedReader br = new BufferedReader(
				new InputStreamReader(
						new BufferedInputStream(
								System.in)))) {
			
			while(true) {
				System.out.print(COMMAND_START);
				String line = br.readLine();
				if (line == null || line.strip().equals(EXIT_COMMAND)) {
					System.out.println(EXIT_MSG);
					break;
				}
				
				line = line.strip();
				if (!line.startsWith(QUERY_COMMAND)) {
					System.out.println("Unknown command.");
					continue;
				}
				
				int off = QUERY_COMMAND.length();
				String query = line.substring(off);
				if (query.isBlank()) {
					System.out.println("Empty query command!");
					continue;
				}
				char ch = query.charAt(0);
				if (!Character.isWhitespace(ch)) {
					System.out.println("After query command there must be a whitespace!");
					continue;
				}
				
				try {
					executeQueryAndShowResults(query);
				} catch (QueryException ex) {
					String msg = ex.getMessage();
					System.out.println("Query command error: " + msg);
					continue;
				}
				
				System.out.println();
			}
		}
	}
	
	private static void executeQueryAndShowResults(String query) {
		
		QueryParser qp = new QueryParser(query);
		List<StudentRecord> result = null;
		
		if(qp.isDirectQuery()) {
			System.out.println("Using index for record retrieval.");
			StudentRecord r = db.forJMBAG(qp.getQueriedJMBAG());
			result = r == null ? List.of() : List.of(r);
		} else {
			result = db.filter(new QueryFilter(qp.getQuery()));
		}
		
		int count = result.size();
		if (count > 0) {
			List<String> formattedRecords = RecordFormatter.format(result);
			formattedRecords.forEach(System.out::println);
		}
		
		System.out.println("Records selected: " + count);
	}
	
	
}
