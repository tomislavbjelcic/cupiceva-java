package hr.fer.oprpp1.custom.scripting.elems;

/**
 * Predstavlja izraz realne konstante jezika SmartScript.
 * 
 * @author Tomislav Bjelčić
 *
 */
public class ElementConstantDouble extends Element {
	
	/**
	 * Realna vrijednost ovog izraza.
	 */
	private double value;
	
	/**
	 * Stvara novi izraz realne konstante sa vrijednošću {@code value}
	 * 
	 * @param value realna vrijednost izraza.
	 */
	public ElementConstantDouble(double value) {
		this.value = value;
	}
	
	/**
	 * Vraća realnu vrijednost ovog izraza.
	 * 
	 * @return realna vrijednost ovog izraza.
	 */
	public double getValue() {
		return value;
	}
	
	@Override
	public String asText() {
		return String.valueOf(value);
	}
	
}
