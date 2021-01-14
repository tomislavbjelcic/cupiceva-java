package hr.fer.oprpp1.custom.scripting.lexer;


/**
 * Predstavlja iznimku koja se baca ukoliko dođe do pogreške rada leksičkog analizatora 
 * {@link SmartScriptLexer} ili ukoliko se rade ilegalni pozivi metode {@link SmartScriptLexer#nextToken()}.
 * 
 * @author Tomislav Bjelčić
 *
 */
public class SmartScriptLexerException extends RuntimeException {
	
	/**
	 * Stvara novu iznimku lexera bez poruke.
	 */
	public SmartScriptLexerException() {}
	
	/**
	 * Stvara novu iznimku lexera sa porukom {@code msg}.
	 * 
	 * @param msg poruka iznimke.
	 */
	public SmartScriptLexerException(String msg) {
		super(msg);
	}
	
}
