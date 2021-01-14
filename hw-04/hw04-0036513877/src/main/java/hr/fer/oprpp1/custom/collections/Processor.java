package hr.fer.oprpp1.custom.collections;

/**
 * Model objekta, takozvani Procesor, koji obavlja operaciju
 * nad predanim objektom.
 * @author Tomislav Bjelčić
 * @param <T> tip objekta nad kojim se obavlja operacija.
 */
@FunctionalInterface
public interface Processor<T> {
	
	
	/**
	 * Obavlja operaciju (akciju) nad predanim objektom.
	 * 
	 * @param value objekt nad kojim treba obaviti akciju.
	 */
	void process(T value);
	
}
