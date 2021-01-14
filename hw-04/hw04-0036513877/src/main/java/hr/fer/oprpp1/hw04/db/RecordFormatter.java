package hr.fer.oprpp1.hw04.db;

import java.util.LinkedList;
import java.util.List;

/**
 * Razred koji sadrži metodu za formatiranje liste studenata dobivenih iz baze podataka.
 * 
 * @author Tomislav Bjelčić
 *
 */
public class RecordFormatter {
	
	private RecordFormatter() {}
	
	public static List<String> format(List<StudentRecord> records) {
		
		List<String> formatted = new LinkedList<>();
		
		int maxFirstNameLetterCount = 0;
		int maxLastNameLetterCount = 0;
		
		for (StudentRecord r : records) {
			int firstNameLetterCount = r.getFirstName().length();
			int lastNameLetterCount = r.getLastName().length();
			
			if (firstNameLetterCount > maxFirstNameLetterCount)
				maxFirstNameLetterCount = firstNameLetterCount;
			if (lastNameLetterCount > maxLastNameLetterCount)
				maxLastNameLetterCount = lastNameLetterCount;
		}
		
		String plus = "+";
		String eq = "=";
		String vert = "|";
		String sp = " ";
		String twelveEq = eq.repeat(12);
		String threeEq = eq.repeat(3);
		
		StringBuilder sb = new StringBuilder();
		
		String firstLine = sb.append(plus)
								.append(twelveEq)
								.append(plus)
								.append(eq.repeat(maxLastNameLetterCount + 2))
								.append(plus)
								.append(eq.repeat(maxFirstNameLetterCount + 2))
								.append(plus)
								.append(threeEq)
								.append(plus)
								.toString();
		sb.delete(0, sb.length());
		formatted.add(firstLine);
		for (StudentRecord r : records) {
			String jmbag = r.getJmbag();
			String firstName = r.getFirstName();
			String lastName = r.getLastName();
			String finalGrade = String.valueOf(r.getFinalGrade());
			
			String spaceExtraLast = sp.repeat(maxLastNameLetterCount - lastName.length());
			String spaceExtraFirst = sp.repeat(maxFirstNameLetterCount - firstName.length());
			String formattedLine = sb.append(vert)
										.append(sp).append(jmbag).append(sp)
										.append(vert)
										.append(sp).append(lastName).append(spaceExtraLast).append(sp)
										.append(vert)
										.append(sp).append(firstName).append(spaceExtraFirst).append(sp)
										.append(vert)
										.append(sp).append(finalGrade).append(sp)
										.append(vert)
										.toString();
			
			sb.delete(0, sb.length());
			formatted.add(formattedLine);
		}
		formatted.add(firstLine);
		
		return formatted;
	}
	
	
}
