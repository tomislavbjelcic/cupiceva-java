package hr.fer.oprpp1.custom.collections;

import java.util.Objects;

/**
 * Objekt koji omogućuje dohvaćanje element po element (iteriranje) neke kolekcije, 
 * na zahtjev korisnika.<br>
 * Ovaj objekt može zahtjevati da kolekcija po kojoj iterira se ne smije mijenjati. 
 * U tom slučaju će poziv bilo koje njegovih metoda izazvati {@code ConcurrentModificationException}.
 * 
 * @author Tomislav Bjelčić
 *
 */
public interface ElementsGetter {
	
	/**
	 * Provjerava ima li preostalih elemenata koji još nisu bili dohvaćeni.
	 * 
	 * @return {@code true} ako postoji još nedohvaćenih elemenata, inače {@code false}.
	 */
	boolean hasNextElement();
	
	/**
	 * Dohvaća sljedeći nedohvaćeni element ukoliko takav postoji.
	 * Ukoliko nema više nedohvaćenih elemenata, metoda baca {@code NoSuchElementException}.
	 * 
	 * @return sljedeći nedohvaćeni element.
	 * @throws NoSuchElementException ako se pokuša dohvatiti element, a nema više nedohvaćenih elemenata.
	 */
	Object getNextElement();
	
	/**
	 * Procesuira, odnosno poziva metodu {@code process}, nad svim preostalim nedohvaćenim elementima.
	 * 
	 * @param p objekt Procesor koji procesuira preostale elemente.
	 * @throws NullPointerException ako je predani Procesor {@code p} {@code null}.
	 */
	default void processRemaining(Processor p) {
		Objects.requireNonNull(p, "Predani Procesor je null.");
		
		while(hasNextElement())
			p.process(getNextElement());
	}
}
