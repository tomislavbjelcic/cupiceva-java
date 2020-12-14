package hr.fer.zemris.math;

import java.util.Objects;
import java.util.function.Function;

/**
 * Pomoćni razred sa korisnim javnim statičkim metodama.
 * 
 * @author Tomislav Bjelčić
 *
 */
public class Util {
	
	/**
	 * Onemogući stvaranje instanci ovog razreda.
	 */
	private Util() {}
	
	/**
	 * Implementacija funkcijskog sučelja {@link Function} čija metoda {@code apply} 
	 * kao argument prima objekt i dodaje oble zagrade njenoj String reprezentaciji.
	 */
	private static final Function<Object, String> ADD_PARENTHESES = new Function<>() {
		
		private final char openParenthesis = '(';
		private final char closedParenthesis = ')';
		
		@Override
		public String apply(Object obj) {
			String added = openParenthesis + obj.toString() + closedParenthesis;
			return added;
		}
		
	};
	
	/**
	 * Vraća String koji je po sadržaju jednak String reprezentaciji objekta {@code obj} 
	 * (poziv metode {@code obj.toString()}) sa dodanim oblim zagradama na početku i na kraju.
	 * 
	 * @param obj
	 * @return String "(S)", gdje je S rezultat poziva {@code obj.toString()}.
	 * @throws NullPointerException ako je predani objekt {@code null}.
	 */
	public static String addParentheses(Object obj) {
		return ADD_PARENTHESES.apply(Objects.requireNonNull(obj, "Predani objekt je null."));
	}
}
