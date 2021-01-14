package hr.fer.oprpp1.custom.collections;

/**
 * Razred predstavlja iznimku praznog stoga: iznimku koja se baca prilikom obavljanja 
 * operacija koje pokušavaju pristupiti vrhu praznog stoga. Kako takve operacije 
 * tada nisu moguće jer nema elemenata, rezultat je ova iznimka.
 * 
 * @author Tomislav Bjelčić
 *
 */
public class EmptyStackException extends RuntimeException {
	
	/**
	 * Poruka pogreške koja je ista svim iznimkama EmptyStackException.
	 */
	public static final String MESSAGE = 
			"Pokušaj dohvata ili skidanja elementa sa praznog stoga.";
	
	/**
	 * Stvara novu iznimku praznog stoga sa prikladnom porukom.
	 */
	public EmptyStackException() {
		super(MESSAGE);
	}
	
}
