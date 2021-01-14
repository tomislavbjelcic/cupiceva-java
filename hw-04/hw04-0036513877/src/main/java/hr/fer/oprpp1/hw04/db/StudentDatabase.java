package hr.fer.oprpp1.hw04.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Predstavlja model baze podataka sa jednom tablicom koja sadrži zapise studenta 
 * {@link StudentRecord}.
 * 
 * @author Tomislav Bjelčić
 *
 */
public class StudentDatabase {
	
	/**
	 * Retci (n-torke) jedine tablice u obliku liste zapisa studenata.
	 */
	private List<StudentRecord> records = new ArrayList<>();
	/**
	 * Indeks napravljen za primarni ključ tablice zapisa studenata (JMBAG) u obliku mape 
	 * implementirane tablicom raspršenog adresiranja sa ključem JMBAG-a i vrijednošću 
	 * zapisa studenta.
	 */
	private Map<String, StudentRecord> index = new HashMap<>();
	
	/**
	 * Stvara novu bazu podataka sa jednom tablicom i puni ju sa zapisima studenta u 
	 * obliku Stringa. Svi takvi zapisi se predaju kao lista Stringova 
	 * (parametar {@code dbText}).
	 * 
	 * @param dbText lista String zapisa svakog studenta s kojima se puni ova baza.
	 * 
	 * @throws NullPointerException ako je predana lista zapisa {@code null}.
	 * @throws IllegalArgumentException ako u predanoj listi postoji String zapis koji nije u ispravnom 
	 * formatu u smislu da nedostaju atributi ili da sami atributi nisu ispravni 
	 * (osim konačne ocjene).
	 * @throws NumberFormatException ako u predanoj listi postoji String zapis čiji atribut konačne ocjene ne može biti prikazan kao 
	 * cijeli broj.
	 */
	public StudentDatabase(List<String> dbText) {
		Objects.requireNonNull(dbText, "Predana lista linija teksta je null.");
		
		fillStudentDatabase(dbText);
	}
	
	private void fillStudentDatabase(List<String> dbText) {
		dbText.forEach(line -> {
			StudentRecord record = StudentRecord.parse(line);
			String jmbag = record.getJmbag();
			
			if (index.containsKey(jmbag)) {
				// zapis ćemo ignorirati
				// mogla se bacati i iznimka
				return;
			}
			records.add(record);
			index.put(jmbag, record);
		});
	}
	
	/**
	 * Dohvaća zapis studenta iz baze na temelju predanog JMBAG-a.<br>
	 * Kako se radi o pretraživanju pomoću primarnog ključa, koristi se 
	 * indeks kako bi se pretraživanje obavilo u konstantnom vremenu.
	 * 
	 * @param jmbag JMBAG studenta čiji zapis se dohvaća.
	 * @return zapis studenta sa JMBAG-om {@code jmbag} ako takav postoji u bazi, 
	 * inače {@code null}.
	 * @throws NullPointerException ako je predani JMBAG {@code null}.
	 */
	public StudentRecord forJMBAG(String jmbag) {
		return index.get(Objects.requireNonNull(jmbag, "Predani JMBAG je null."));
	}
	
	/**
	 * Stvara i vraća listu zapisa studenta koji zadovoljavaju predani {@code filter}.
	 * 
	 * @param filter objekt koji daje odluku za svakog studenta zadovoljava li neki kriterij.
	 * @return lista zapisa studenta koji zadovoljavaju predani filter.
	 * @throws NullPointerException ako je predani filter {@code null}.
	 */
	public List<StudentRecord> filter(IFilter filter) {
		Objects.requireNonNull(filter, "Predani filter je null.");
		
		return records.stream()
						.filter(filter::accepts)
						.collect(Collectors.toList());
	}
	
}
