package hr.fer.oprpp1.custom.scripting.elems;

import java.util.Objects;

/**
 * Predstavlja izraz varijable jezika SmartScript.
 * 
 * @author Tomislav Bjelčić
 *
 */
public class ElementVariable extends Element {
	
	/**
	 * Naziv varijable ovog izraza.
	 */
	private String name;
	
	/**
	 * Stvara novi izraz varijable sa imenom {@code name}.
	 * 
	 * @param name ime varijable.
	 * @throws NullPointerException ako je predano ime varijable {@code null}.
	 */
	public ElementVariable(String name) {
		this.name = Objects.requireNonNull(name);
	}
	
	/**
	 * Vraća naziv varijable ovog izraza.
	 * 
	 * @return naziv varijable ovog izraza.
	 */
	public String getName() {
		return name;
	}
	
	@Override
	public String asText() {
		return name;
	}
	
	
}
