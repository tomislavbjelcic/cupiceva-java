package hr.fer.oprpp1.custom.scripting.elems;

import java.util.Objects;

import hr.fer.oprpp1.custom.scripting.lexer.SmartScriptLexer;

/**
 * Predstavlja izraz znakovnog niza jezika SmartScript. Svaki znakovni niz je omeđen navodnicima, 
 * primjerice {@code "neki_znakovni_niz"}.
 * 
 * @author Tomislav Bjelčić
 *
 */
public class ElementString extends Element {
	
	/**
	 * Znakovni niz ovog izraza.
	 */
	private String value;
	
	/**
	 * Stvara novi izraz znakovnog niza {@code value}.
	 * 
	 * @param value
	 */
	public ElementString(String value) {
		this.value = Objects.requireNonNull(value);
	}
	
	/**
	 * Vraća znakovni niz ovog izraza.
	 * 
	 * @return znakovni niz ovog izraza.
	 */
	public String getValue() {
		return value;
	}
	
	@Override
	public String asText() {
		return value;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(SmartScriptLexer.STRING_SYMBOL);
		for (int i=0, len=value.length(); i<len; i++) {
			char ch = value.charAt(i);
			String seq = "";
			if (isEscaped(ch)) {
				seq = switch(ch) {
					case SmartScriptLexer.ESCAPE,
						SmartScriptLexer.STRING_SYMBOL -> "" + SmartScriptLexer.ESCAPE
														+ ch;
					case '\t' -> "" + SmartScriptLexer.ESCAPE + "t";
					case '\r' -> "" + SmartScriptLexer.ESCAPE + "r";
					case '\n' -> "" + SmartScriptLexer.ESCAPE + "n";
					default -> "";
				};
				sb.append(seq);
				continue;
			}
			sb.append(ch);
		}
		sb.append(SmartScriptLexer.STRING_SYMBOL);
		return sb.toString();
	}
	
	private static boolean isEscaped(char ch) {
		boolean result = switch(ch) {
			case SmartScriptLexer.ESCAPE,
				SmartScriptLexer.STRING_SYMBOL,
				'\t', '\n', '\r' -> true;
			default -> false;
		};
		return result;
	}
	
	
}
