package hr.fer.oprpp1.custom.scripting.lexer;


/**
 * Tip (vrsta) leksičke jedinke {@link SmartScriptToken} koju može izgenerirati leksički 
 * analizator {@link SmartScriptLexer}.
 * 
 * @author Tomislav Bjelčić
 *
 */
public enum SmartScriptTokenType {
	/**
	 * Označava tekst izvan tagova.
	 */
	ELEMENT_TEXT,
	/**
	 * Označava realni izraz.
	 */
	TAG_ELEMENT_CONSTANT_DOUBLE,
	/**
	 * Označava cjelobrojni izraz.
	 */
	TAG_ELEMENT_CONSTANT_INTEGER,
	/**
	 * Označava funkciju.
	 */
	TAG_ELEMENT_FUNCTION,
	/**
	 * Označava operator.
	 */
	TAG_ELEMENT_OPERATOR,
	/**
	 * Označava ime varijable ili ime taga.
	 */
	TAG_ELEMENT_VARIABLE_OR_NAME,
	/**
	 * Označava znakovni niz.
	 */
	TAG_ELEMENT_STRING,
	/**
	 * Označava otvaranje taga.
	 */
	TAG_OPEN,
	/**
	 * Označava zatvaranje taga.
	 */
	TAG_CLOSE,
	/**
	 * Označava kraj ulaznog programa.
	 */
	EOF
}
