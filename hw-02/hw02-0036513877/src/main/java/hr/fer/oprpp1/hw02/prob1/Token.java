package hr.fer.oprpp1.hw02.prob1;

import java.util.Objects;

/**
 * Razred predstavlja leksičku jedinku (token) koju generira {@link Lexer} tijekom 
 * leksičke analize ulaznog niza.
 * 
 * @author Tomislav Bjelčić
 *
 */
public class Token {
	/**
	 * Tip (vrsta) leksičke jedinke.
	 */
	private TokenType type;
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
	public Token(TokenType type, Object value) {
		this.type = Objects.requireNonNull(type, "Predana vrsta tokena je null.");
		this.value = value;
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
	 * Vraća tip ove leksičke jedinke.
	 * 
	 * @return
	 */
	public TokenType getType() {
		return type;
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
