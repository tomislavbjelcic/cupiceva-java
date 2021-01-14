package hr.fer.oprpp1.custom.scripting.lexer;

import java.util.Objects;

/**
 * Razred predstavlja leksičku jedinku (token) koju generira {@link SmartScriptLexer} tijekom 
 * leksičke analize ulaznog niza.
 * 
 * @author Tomislav Bjelčić
 *
 */
public class SmartScriptToken {
	/**
	 * Tip (vrsta) leksičke jedinke.
	 */
	private SmartScriptTokenType type;
	/**
	 * Vrijednost sadržana u ovoj leksičkoj jedinki.
	 */
	private Object value;
	
	/**
	 * Stvara novu leksičku jedinku specificiranog tipa {@code type} i vrijednosti 
	 * {@code value}.
	 * 
	 * @param type tip leksičke jedinke.
	 * @param value vrijednost leksičke jedinke.
	 * @throws NullPointerException ukoliko je specificirani tip {@code null}.
	 */
	public SmartScriptToken(SmartScriptTokenType type, Object value) {
		this.type = Objects.requireNonNull(type);
		this.value = value;
	}
	
	/**
	 * Vraća tip ove leksičke jedinke.
	 * 
	 * @return
	 */
	public SmartScriptTokenType getType() {
		return type;
	}
	
	/**
	 * Vraća vrijednost ove leksičke jedinke. Može biti i {@code null}.
	 * 
	 * @return vrijednost ove leksičke jedinke.
	 */
	public Object getValue() {
		return value;
	}
	
	/**
	 * Vraća String reprezentaciju ove leksičke jedinke.
	 * 
	 * @return String reprezentacija ove leksičke jedinke.
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append('(').append(type.toString())
			.append(", ").append(String.valueOf(value))
			.append(')');
		return sb.toString();
	}
	
}
