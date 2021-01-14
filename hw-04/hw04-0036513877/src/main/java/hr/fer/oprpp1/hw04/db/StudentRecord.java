package hr.fer.oprpp1.hw04.db;

import java.util.Objects;

/**
 * Predstavlja zapis jednog studenta (redak tablice) u bazi podataka.
 * 
 * @author Tomislav Bjelčić
 *
 */
public class StudentRecord {
	
	/**
	 * Broj znamenaka ispravnog JMBAG-a.
	 */
	private static final int JMBAG_DIGIT_COUNT = 10;
	/**
	 * Maksimalna ocjena.
	 */
	private static final int MAX_FINAL_GRADE = 5;
	/**
	 * Minimalna ocjena.
	 */
	private static final int MIN_FINAL_GRADE = 1;
	
	/**
	 * JMBAG (jedinstveni matični broj akademskog građana) studenta, 
	 * primarni ključ po kojem je jedinstveno identificiran svaki student.
	 */
	private String jmbag;
	/**
	 * Prezime studenta.
	 */
	private String lastName;
	/**
	 * Ime studenta.
	 */
	private String firstName;
	/**
	 * Konačna ocjena studenta.
	 */
	private int finalGrade;
	
	/**
	 * Stvara novi zapis studenta sa predanim atributima.
	 * 
	 * @param jmbag JMBAG studenta.
	 * @param lastName prezime studenta.
	 * @param firstName ime studenta.
	 * @param finalGrade konačna ocjena studenta.
	 * 
	 * @throws NullPointerException ako je bilo koji atribut (osim konačne ocjene) {@code null}.
	 * @throws IllegalArgumentException ako je bilo koji atribut neispravan u semantičkom smislu.
	 * Primjerice, ako JMBAG nema 10 znamenaka, ako je konačna ocjena negativna, itd.
	 */
	public StudentRecord(String jmbag, String lastName, String firstName, int finalGrade) {
		setJmbag(jmbag);
		setLastName(lastName);
		setFirstName(firstName);
		setFinalGrade(finalGrade);
	}
	
	/**
	 * Dohvaća JMBAG studenta ovog zapisa.
	 * 
	 * @return JMBAG studenta.
	 */
	public String getJmbag() {
		return jmbag;
	}

	private void setJmbag(String jmbag) {
		Objects.requireNonNull(jmbag, "Predani JMBAG je null.");
		checkJmbag(jmbag);
		this.jmbag = jmbag;
	}
	
	private static void checkJmbag(String jmbag) {
		int len = jmbag.length();
		if (len != JMBAG_DIGIT_COUNT)
			throw new IllegalArgumentException("Predani JMBAG nije ispravan: JMBAG mora imati točno 10 znamenaka.");
		
		for (int i=0; i<len; i++) {
			char ch = jmbag.charAt(i);
			if (!Character.isDigit(ch))
				throw new IllegalArgumentException("Predani JMBAG nije ispravan: znak \"" + ch + "\" nije znamenka.");
		}
	}

	/**
	 * Dohvaća prezime studenta ovog zapisa.
	 * 
	 * @return prezime studenta.
	 */
	public String getLastName() {
		return lastName;
	}

	private void setLastName(String lastName) {
		Objects.requireNonNull(lastName, "Predano prezime je null.");
		checkName(lastName);
		this.lastName = lastName;
	}
	
	private static void checkName(String name) {
		for (int i=0, len=name.length(); i<len; i++) {
			char ch = name.charAt(i);
			if (!Character.isLetter(ch) && !Character.isWhitespace(ch) && ch != '-')
				throw new IllegalArgumentException("U imenu i prezimenu smiju biti samo slova i praznine.");
		}
	}

	/**
	 * Dohvaća ime studenta ovog zapisa.
	 * 
	 * @return ime studenta.
	 */
	public String getFirstName() {
		return firstName;
	}

	private void setFirstName(String firstName) {
		Objects.requireNonNull(firstName, "Predano prezime je null.");
		checkName(firstName);
		this.firstName = firstName;
	}

	/**
	 * Dohvaća konačnu ocjenu studenta ovog zapisa.
	 * 
	 * @return konačnu ocjenu studenta.
	 */
	public int getFinalGrade() {
		return finalGrade;
	}

	private void setFinalGrade(int finalGrade) {
		if (finalGrade < MIN_FINAL_GRADE || finalGrade > MAX_FINAL_GRADE)
			throw new IllegalArgumentException("Predana ocjena nije između " + MIN_FINAL_GRADE + " i " + MAX_FINAL_GRADE);
		this.finalGrade = finalGrade;
	}
	
	/**
	 * Predani String zapis studenta parsira i stvara novi zapis na temelju sadržaja 
	 * {@code s}.<br>
	 * Svaki atribut u String zapisu mora biti odvojen barem tabulatorom.
	 * 
	 * @param s String zapis studenta
	 * 
	 * @return novi zapis studenta sa atributima zapisanim u predanom Stringu {@code s}.
	 * 
	 * @throws NullPointerException ako je predani String zapis {@code null}.
	 * @throws IllegalArgumentException ako predani String zapis nije u ispravnom 
	 * formatu u smislu da nedostaju atributi ili da sami atributi nisu ispravni 
	 * (osim konačne ocjene).
	 * @throws NumberFormatException ako atribut konačne ocjene ne može biti prikazan kao 
	 * cijeli broj.
	 *  
	 */
	public static StudentRecord parse(String s) {
		Objects.requireNonNull(s, "Predani String studentovog zapisa je null.");
		
		s = s.strip();
		String[] splitted = s.split("\t");
		if (splitted.length != 4)
			throw new IllegalArgumentException(
					"Neispravan format zapisa studenta: " + s);
		
		String jmbag = splitted[0];
		String lastName = splitted[1];
		String firstName = splitted[2];
		String gradeStr = splitted[3];
		int finalGrade = Integer.parseInt(gradeStr);
		
		StudentRecord record = new StudentRecord(jmbag, lastName, firstName, finalGrade);
		return record;
	}
	
	/**
	 * Provjerava jednakost dvaju zapisa studenta.<br>
	 * Kako je svaki student određen JMBAG-om, dva zapisa su jednaka ako i samo 
	 * ako su im JMBAG-ovi jednaki.
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (this.getClass() != obj.getClass())
			return false;
		
		StudentRecord other = (StudentRecord) obj;
		return this.jmbag.equals(other.jmbag);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(jmbag);
	}
	
	@Override
	public String toString() {
		char tab = '\t';
		return jmbag + tab + lastName + tab + firstName + tab + finalGrade;
	}
}
