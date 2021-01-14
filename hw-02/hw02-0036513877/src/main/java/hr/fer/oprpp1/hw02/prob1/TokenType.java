package hr.fer.oprpp1.hw02.prob1;

/**
 * Tip (vrsta) leksičke jedinke {@link Token} koju može izgenerirati leksički 
 * analizator {@link Lexer}.
 * 
 * @author Tomislav Bjelčić
 *
 */
public enum TokenType {
	/**
	 * Označava kraj ulaznog niza znakova (End Of File).
	 */
	EOF, 
	/**
	 * Označava tip riječi.
	 */
	WORD, 
	/**
	 * Označava tip cjelobrojnog broja.
	 */
	NUMBER, 
	/**
	 * Označava tip specijalnih znakova (simbola).
	 */
	SYMBOL
}
