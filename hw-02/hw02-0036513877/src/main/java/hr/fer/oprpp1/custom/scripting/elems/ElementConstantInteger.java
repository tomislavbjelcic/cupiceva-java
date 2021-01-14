package hr.fer.oprpp1.custom.scripting.elems;

/**
 * Predstavlja izraz cjelobrojne konstante jezika SmartScript.
 * 
 * @author Tomislav Bjelčić
 *
 */
public class ElementConstantInteger extends Element {
	
	/**
	 * Cjelobrojna vrijednost ovog izraza.
	 */
	private int value;
	
	/**
	 * Stvara novi izraz cjelobrojne konstante sa vrijednošću {@code value}
	 * 
	 * @param value cjelobrojna vrijednost izraza.
	 */
	public ElementConstantInteger(int value) {
		this.value = value;
	}
	
	/**
	 * Vraća cjelobrojnu vrijednost ovog izraza.
	 * 
	 * @return cjelobrojna vrijednost ovog izraza.
	 */
	public int getValue() {
		return value;
	}
	
	@Override
	public String asText() {
		return String.valueOf(value);
	}
	
	
}
