package hr.fer.oprpp1.custom.collections;

/**
 * Model objekta, takozvani Procesor, koji obavlja operaciju
 * nad predanim objektom.
 * @author Tomislav Bjelčić
 *
 */
@FunctionalInterface
public interface Processor {
	
	
	/**
	 * Obavlja operaciju (akciju) nad predanim objektom.
	 * 
	 * @param value objekt nad kojim treba obaviti akciju.
	 */
	void process(Object value);
	
}
