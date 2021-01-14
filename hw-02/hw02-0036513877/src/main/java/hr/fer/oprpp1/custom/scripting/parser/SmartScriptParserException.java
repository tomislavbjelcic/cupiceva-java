package hr.fer.oprpp1.custom.scripting.parser;

/**
 * Iznimka koja se baca kada dođe do pogreške tijekom rada sintaksnog analizatora {@link SmartScriptParser}.
 * 
 * @author Tomislav Bjelčić
 *
 */
public class SmartScriptParserException extends RuntimeException {
	
	/**
	 * Stvara novu iznimku parsera bez poruke.
	 */
	public SmartScriptParserException() {}
	
	/**
	 * Stvara novu iznimku parsera sa porukom {@code msg}.
	 * 
	 * @param msg poruka iznimke.
	 */
	public SmartScriptParserException(String msg) {
		super(msg);
	}
	
}
