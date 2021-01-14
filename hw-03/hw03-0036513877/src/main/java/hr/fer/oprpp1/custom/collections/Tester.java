package hr.fer.oprpp1.custom.collections;

/**
 * Predstavlja Tester, objekt koji prima neki drugi objekt i vraća sud 
 * je li taj predani objekt prihvatljiv ili nije.
 * 
 * @author Tomislav Bjelčić
 * @param <T> tip predanog objekta nad kojim se odlučuje je li prihvatljiv ili nije.
 *
 */
@FunctionalInterface
public interface Tester<T> {
	
	/**
	 * Provjerava je li predani objekt {@code obj} prihvatljiv ili nije.
	 * 
	 * @param obj objekt čija prihvatljivost se ispituje.
	 * @return {@code true} ako je objekt prihvatljiv, inače {@code false}.
	 */
	boolean test(T obj);
	
}
