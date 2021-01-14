package hr.fer.oprpp1.hw04.db;

/**
 * Predstavlja objekt koji je sposoban prosuđivati je li neki zapis studenta 
 * zadovoljava definirani kriterij.
 * 
 * @author Tomislav Bjelčić
 *
 */
@FunctionalInterface
public interface IFilter {
	
	/**
	 * Provjerava je li zapis studenta {@code record} zadovoljava definirani kriterij.
	 * 
	 * @param record
	 * @return {@code true} ako zadovoljava, inače {@code false}.
	 */
	boolean accepts(StudentRecord record);
	
}
