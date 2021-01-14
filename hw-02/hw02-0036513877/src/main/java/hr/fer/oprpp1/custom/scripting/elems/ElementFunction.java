package hr.fer.oprpp1.custom.scripting.elems;

import java.util.Objects;

import hr.fer.oprpp1.custom.scripting.lexer.SmartScriptLexer;

/**
 * Predstavlja izraz funkcije jezika SmartScript. U izvornom programu funkcija počinje znakom {@code @}.
 * 
 * @author Tomislav Bjelčić
 *
 */
public class ElementFunction extends Element {
	
	/**
	 * Ime funkcije predstavljen ovim izrazom.
	 */
	private String name;
	
	/**
	 * Stvara novi izraz funkcije sa imenom {@code name}.
	 * 
	 * @param name ime funkcije.
	 * @throws NullPointerException ako je predano ime {@code null}.
	 */
	public ElementFunction(String name) {
		this.name = Objects.requireNonNull(name);
	}
	
	/**
	 * Vraća ime funkcije predstavljen ovim izrazom.
	 * 
	 * @return ime funkcije.
	 */
	public String getName() {
		return name;
	}
	
	@Override
	public String asText() {
		return name;
	}
	
	@Override
	public String toString() {
		return "" + SmartScriptLexer.FUNCTION_SYMBOL + asText();
	}
	
}
