package hr.fer.oprpp1.custom.collections;

import java.util.Objects;

/**
 * Implementacija stoga - LIFO strukture podataka koja omogućava postavljanje i 
 * uklanjanje elemenata sa vrha.
 * 
 * <p> Razred ne dopušta stavljanje {@code null} referenci na vrh stoga.
 * 
 * 
 * @author Tomislav Bjelčić
 * @params <E> tip elemenata stoga.
 *
 */
public class ObjectStack<E> {
	
	/**
	 * Kao spremnik stoga koristi se kolekcija implementirana poljem.
	 */
	private ArrayIndexedCollection<E> storage = new ArrayIndexedCollection<>();
	
	/**
	 * Provjerava je li stog prazan.
	 * 
	 * @return {@code true} ukoliko je stog prazan, inače {@code false}.
	 */
	public boolean isEmpty() {
		return storage.isEmpty();
	}
	
	/**
	 * Vraća broj pohranjenih elemenata na stogu.
	 * 
	 * @return broj pohranjenih elemenata na stogu.
	 */
	public int size() {
		return storage.size();
	}
	
	/**
	 * Stavlja novi element na vrh stoga.
	 * 
	 * @param value element koji se stavlja na vrh stoga.
	 * @throws NullPointerException ukoliko se pokuša staviti {@code null} referenca.
	 */
	public void push(E value) {
		storage.add(
				Objects.requireNonNull(value, "Stavljanje null referenci na stog"
												+ " nije dozvoljeno.")
				);
	}
	
	/**
	 * Skida element sa vrha stoga te ga vraća pozivatelju.
	 * 
	 * @return skinuti element.
	 * @throws EmptyStackException ukoliko se pokuša skinuti element sa praznog stoga.
	 */
	public E pop() {
		E toPop = peek();
		storage.remove(storage.size() - 1);
		return toPop;
	}
	
	/**
	 * Vraća element na vrhu stoga.
	 * 
	 * @return element na vrhu stoga.
	 * @throws EmptyStackException ukoliko je stog prazan.
	 */
	public E peek() {
		if (isEmpty())
			throw new EmptyStackException();
		
		return storage.get(storage.size() - 1);
	}
	
	/**
	 * Prazni stog, odnosno skida sve elemente sa stoga.
	 */
	public void clear() {
		storage.clear();
	}
	
	
}
