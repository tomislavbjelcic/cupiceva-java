package hr.fer.oprpp1.hw04.db;

/**
 * Predstavlja objekt koji je sposoban dohvaćati određeni atribut zapisa studenta 
 * iz predanog zapisa.
 * 
 * @author Tomislav Bjelčić
 *
 */
@FunctionalInterface
public interface IFieldValueGetter {
	
	/**
	 * Dohvaća atribut zapisa studenta (u obliku Stringa, neće se koristiti za atribut 
	 * konačne ocjene) iz predanog zapisa {@code record}.
	 * 
	 * @param record zapis studenta iz kojeg se dohvaća neki atribut.
	 * @return atribut iz zapisa studenta.
	 */
	String get(StudentRecord record);
	
}
