package hr.fer.oprpp1.custom.scripting.elems;

import java.util.Objects;

/**
 * Predstavlja izraz operatora jezika SmartScript. Podržani operatori su + - / * ^.
 * 
 * @author Tomislav Bjelčić
 *
 */
public class ElementOperator extends Element {
	
	/**
	 * Oznaka operatora predstavljenog ovim izrazom.
	 */
	private String symbol;
	
	/**
	 * Stvara novi izraz operatora oznake {@code symbol}.
	 * 
	 * @param symbol oznaka operatora.
	 * @throws NullPointerException ako je predana oznaka {@code null}.
	 */
	public ElementOperator(String symbol) {
		this.symbol = Objects.requireNonNull(symbol);
	}
	
	/**
	 * Vraća oznaku operatora ovog izraza.
	 * 
	 * @return oznaka operatora ovog izraza.
	 */
	public String getSymbol() {
		return symbol;
	}
	
	@Override
	public String asText() {
		return symbol;
	}
	
	
}
