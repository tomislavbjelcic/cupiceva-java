package hr.fer.oprpp1.hw04.db;

/**
 * Predstavlja operator u uvjetnom izrazu.
 * 
 * @author Tomislav Bjelčić
 *
 */
@FunctionalInterface
public interface IComparisonOperator {
	
	/**
	 * Obavlja operaciju uvjetnog izraza.
	 * 
	 * @param value1 izraz na lijevoj strani operatora.
	 * @param value2 izraz na desnoj strani operatora.
	 * @return rezultat operacije uvjetnog izraza koji sadrži ovaj operator.
	 */
	boolean satisfied(String value1, String value2);
	
}
