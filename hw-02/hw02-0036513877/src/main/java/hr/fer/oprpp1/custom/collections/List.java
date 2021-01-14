package hr.fer.oprpp1.custom.collections;

/**
 * Uređena kolekcija objekata koja nudi metode za pristup i manipulaciju 
 * elementima indeksiranjem, što znači da svaki element ima svoju pripadnu 
 * poziciju (indeks) pomoću koje joj se može pristupati.
 * 
 * @author Tomislav Bjelčić
 *
 */
public interface List extends Collection {
	
	/**
	 * Dohvaća element na poziciji {@code index}.
	 * 
	 * @param index pozicija elementa.
	 * @return element na poziciji {@code index}.
	 * @throws IndexOutOfBoundsException ako je specificirana pozicija
	 * izvan raspona od 0 (uključivo) do veličine liste (isključivo).
	 */
	Object get(int index);
	
	/**
	 * Umeće predani objekt {@code value} na poziciju {@code position}.
	 * 
	 * <p>Kao posljedica će svi elementi koji su prethodno bili na pozicijama većim 
	 * ili jednakim od {@code position} sada biti na pozicijama za jedan veći.
	 * 
	 * @param value objekt koji se umeće.
	 * @param position pozicija na kojoj se predani objekt umeće.
	 * @throws NullPointerException ako je predani objekt {@code null}.
	 * @throws IndexOutOfBoundsException ako je specificirana pozicija
	 * izvan raspona od 0 (uključivo) do veličine liste (uključivo).
	 */
	void insert(Object value, int position);
	
	/**
	 * Vraća poziciju prvog pojavljivanja predanog objekta {@code value}.
	 * 
	 * <p>Ukoliko takav element ne postoji (što podrazumijeva i ako se 
	 * preda {@code null} referenca), metoda vraća -1.
	 * 
	 * <p>Metoda se oslanja na {@link Object#equals(Object)} metodu kako bi odredila
	 * jednakost objekata.
	 * 
	 * @param value objekt čija se pozicija prvog pojavljivanja razrješava.
	 * @return poziciju prvog pojavljivanja predanog objekta, a ukoliko
	 * takav objekt ne postoji, -1.
	 */
	int indexOf(Object value);
	
	/**
	 * Uklanja element na specificiranoj poziciji {@code index}.
	 * 
	 * <p>Kao posljedica će svi elementi koji su prethodno bili na pozicijama većim 
	 * ili jednakim od {@code position} sada biti na pozicijama za jedan manji.
	 * 
	 * @param index pozicija sa koje se element uklanja.
	 * @throws IndexOutOfBoundsException ako je specificirana pozicija
	 * izvan raspona od 0 (uključivo) do veličine liste (isključivo).
	 */
	void remove(int index);
	
}
