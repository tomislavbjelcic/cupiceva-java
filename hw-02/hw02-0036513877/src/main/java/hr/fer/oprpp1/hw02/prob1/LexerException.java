package hr.fer.oprpp1.hw02.prob1;

/**
 * Predstavlja iznimku koja se baca ukoliko dođe do pogreške rada leksičkog analizatora 
 * {@link Lexer} ili ukoliko se rade ilegalni pozivi metode {@link Lexer#nextToken()}.
 * 
 * @author Tomislav Bjelčić
 *
 */
public class LexerException extends RuntimeException {
	
	/**
	 * Stvara novu iznimku lexera bez poruke.
	 */
	public LexerException() {}
	
	/**
	 * Stvara novu iznimku lexera sa porukom {@code msg}.
	 * 
	 * @param msg poruka iznimke.
	 */
	public LexerException(String msg) {
		super(msg);
	}
	
}