package hr.fer.zemris.lsystems.impl;

/**
 * Iznimka koja se baca ukoliko se prilikom konfiguracije L-sustava pozivaju metode 
 * sa neispravnim argumentima.
 * 
 * @author Tomislav Bjelčić
 *
 */
public class LSystemBuilderException extends IllegalArgumentException {
	
	/**
	 * Stvara novu iznimku bez poruke.
	 */
	public LSystemBuilderException() {}
	
	/**
	 * Stvara novu iznimku sa porukom {@code msg}.
	 * 
	 * @param msg poruka iznimke.
	 */
	public LSystemBuilderException(String msg) {
		super(msg);
	}

}
