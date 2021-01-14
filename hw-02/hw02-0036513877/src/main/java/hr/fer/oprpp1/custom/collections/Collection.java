package hr.fer.oprpp1.custom.collections;

import java.util.Objects;

/**
 * Općenita kolekcija objekata (elemenata) koja sadrži metode za dohvat 
 * informacija i metode za manipulaciju elemenata ove kolekcije.
 * 
 * @author Tomislav Bjelčić
 *
 */
public interface Collection {
	
	/**
	 * Provjerava je li ova kolekcija prazna.
	 * 
	 * @return {@code true} ukoliko ova kolekcija nema elemenata, 
	 * inače vraća {@code false}.
	 */
	default boolean isEmpty() {
		return size() == 0;
	}
	
	
	/**
	 * Vraća broj trenutno pohranjenih elemenata u kolekciji.
	 * 
	 * @return broj trenutno pohranjenih elemenata u kolekciji.
	 */
	int size();
	
	
	/**
	 * Dodaje predani objekt {@code value} u ovu kolekciju.
	 * 
	 * @param value objekt kojeg želimo dodati u kolekciju.
	 */
	void add(Object value);
	
	
	/**
	 * Provjerava sadrži li ova kolekcija predani objekt {@code value}.
	 * Metoda se oslanja na {@link Object#equals(Object)} metodu kako bi odredila
	 * jednakost objekata.
	 * 
	 * @param value predani objekt za kojeg se provjerava postoji li u ovoj kolekciji.
	 * Legalno je predati i {@code null} referencu.
	 * @return {@code true} ukoliko kolekcija sadrži predani objekt, inače vraća {@code false}.
	 */
	boolean contains(Object value);
	
	
	/**
	 * Uklanja jednu instancu predanog objekta {@code value} iz kolekcije, i ukoliko je takav objekt postojao, vraća {@code true}.
	 * Ukoliko predanog objekta nije bilo u kolekciji metoda vraća {@code false}.
	 * 
	 * <p>Metoda se oslanja na {@link Object#equals(Object)} metodu kako bi odredila
	 * jednakost objekata.
	 * 
	 * 
	 * @param value predani objekt kojeg je potrebno ukloniti iz ove kolekcije (jednu instancu).
	 * @return {@code true} ukoliko je jedna instanca takvog objekta uspješno uklonjena, 
	 * inače vraća {@code false}.
	 */
	boolean remove(Object value);
	
	/**
	 * Stvara novo polje objekata iste veličine kao i ova kolekcija te popunjava novo stvoreno 
	 * polje elementima ove kolekcije.
	 * 
	 * @return novo stvoreno polje popunjeno elementima ove kolekcije.
	 * @throws UnsupportedOperationException ukoliko operacija nije podržana.
	 */
	Object[] toArray();
	
	/**
	 * Za svaki element ove kolekcije poziva metodu {@link Processor#process(Object)} pomoću
	 * predanog objekta Procesora {@code processor}.
	 * 
	 * @param processor objekt Procesor koji obavlja poziv metode {@code process} nad svakim 
	 * elementom ove kolekcije.
	 */
	default void forEach(Processor processor) {
		Objects.requireNonNull(processor, "Predani procesor je null.");
		
		ElementsGetter getter = createElementsGetter();
		getter.processRemaining(processor);
	};
	
	/**
	 * Dodaje sve elemente iz predane kolekcije {@code col} u ovu kolekciju.
	 * 
	 * @param col kolekcija iz koje želimo dodati elemente.
	 * @throws NullPointerException ukoliko je predana kolekcija {@code null}.
	 */
	default void addAll(Collection col) {
		Objects.requireNonNull(col, "Predana kolekcija je null.");
		
		Processor adder = this::add;
		col.forEach(adder);
	}
	
	/**
	 * Uklanja sve elemente iz ove kolekcije.
	 */
	void clear();
	
	/**
	 * Stvara i vraća novi objekt, {@code ElementsGetter}, sposoban za dohvaćanje svih elemenata ove 
	 * kolekcije jedan po jedan.<br>
	 * Tijekom života objekta {@code ElementsGetter}-a korisnik mora osigurati da ne 
	 * mijenja ovu kolekciju. Ukoliko se u međuvremenu kolekcija promijeni, bilo koji poziv 
	 * metode objekta {@code ElementsGetter}-a će baciti {@code ConcurrentModificationException}.
	 * 
	 * @return {@code ElementsGetter} koji dohvaća elemente ove kolekcije.
	 */
	ElementsGetter createElementsGetter();
	
	default void addAllSatisfying(Collection col, Tester tester) {
		Objects.requireNonNull(col, "Predana kolekcija je null.");
		Objects.requireNonNull(tester, "Predani tester je null.");
		
		ElementsGetter getter = col.createElementsGetter();
		getter.processRemaining(obj -> {
			if (tester.test(obj))
				add(obj);
		});
	}
}
