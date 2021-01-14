package hr.fer.oprpp1.custom.collections;

import java.util.Arrays;
import java.util.Objects;

/**
 * Razred modelira općenitu kolekciju objekata.
 * Ovaj konkretan razred ne može se koristiti kao kolekcija i većina njenih metoda nije ispravno implementirano,
 * ali može se naslijediti i takvi razredi tada mogu nadjačati ponuđene javne metode i na taj način ponuditi
 * ispravnu implementaciju za konkretne izvedene kolekcije.
 * 
 * @author Tomislav Bjelčić
 *
 */
public class Collection {
	
	
	/**
	 * Zaštićeni konstruktor namijenjen izvedenim razredima.
	 */
	protected Collection() {}
	
	
	/**
	 * Provjerava je li ova kolekcija prazna.
	 * 
	 * @return {@code true} ukoliko ova kolekcija nema elemenata, 
	 * inače vraća {@code false}.
	 */
	public boolean isEmpty() {
		return size() == 0;
	}
	
	
	/**
	 * Vraća broj trenutno pohranjenih elemenata u kolekciji.
	 * 
	 * @return broj trenutno pohranjenih elemenata u kolekciji.
	 * @implSpec ova implementacija uvijek vraća 0.
	 */
	public int size() {
		return 0;
	}
	
	
	/**
	 * Dodaje predani objekt {@code value} u ovu kolekciju.
	 * 
	 * @param value objekt kojeg želimo dodati u kolekciju.
	 * @implSpec ova implementacija ne radi ništa.
	 */
	public void add(Object value) {}
	
	
	/**
	 * Provjerava sadrži li ova kolekcija predani objekt {@code value}.
	 * Metoda se oslanja na {@link Object#equals(Object)} metodu kako bi odredila
	 * jednakost objekata.
	 * 
	 * @param value predani objekt za kojeg se provjerava postoji li u ovoj kolekciji.
	 * Legalno je predati i {@code null} referencu.
	 * @return {@code true} ukoliko kolekcija sadrži predani objekt, inače vraća {@code false}.
	 * @implSpec ova implementacija uvijek vraća {@code false}.
	 */
	public boolean contains(Object value) {
		return false;
	}
	
	
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
	 * @implSpec ova implementacija uvijek vraća {@code false}.
	 */
	public boolean remove(Object value) {
		return false;
	}
	
	/**
	 * Stvara novo polje objekata iste veličine kao i ova kolekcija te popunjava novo stvoreno 
	 * polje elementima ove kolekcije.
	 * 
	 * @return novo stvoreno polje popunjeno elementima ove kolekcije.
	 * @throws UnsupportedOperationException ukoliko operacija nije podržana.
	 * @implSpec ova implementacija uvijek baca {@code UnsupportedOperationException}.
	 */
	public Object[] toArray() {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Za svaki element ove kolekcije poziva metodu {@link Processor#process(Object)} pomoću
	 * predanog objekta Procesora {@code processor}.
	 * 
	 * @param processor objekt Procesor koji obavlja poziv metode {@code process} nad svakim 
	 * elementom ove kolekcije.
	 * @implSpec ova implementacija ne radi ništa.
	 */
	public void forEach(Processor processor) {}
	
	/**
	 * Dodaje sve elemente iz predane kolekcije {@code col} u ovu kolekciju.
	 * 
	 * @param col kolekcija iz koje želimo dodati elemente.
	 * @throws NullPointerException ukoliko je predana kolekcija {@code null}.
	 */
	public void addAll(Collection col) {
		Objects.requireNonNull(col, "Predana kolekcija je null.");
		
		
		/**
		 * Lokalni razred Procesora čija zadaća je dodavanje elemenata {@code value}
		 * predanih putem {@code process} metode u vanjsku kolekciju.
		 * 
		 * 
		 *
		 */
		class CollectionAdderProcessor extends Processor {
			@Override
			public void process(Object value) {
				Collection.this.add(value);
			}
		}
		
		col.forEach(new CollectionAdderProcessor());
	}
	
	/**
	 * Uklanja sve elemente iz ove kolekcije.
	 * 
	 * @implSpec ova implementacija ne radi ništa.
	 */
	public void clear() {}
	
	/**
	 * Vraća String reprezentaciju ove kolekcije.
	 * 
	 * @return String reprezentacija ove kolekcije.
	 */
	@Override
	public String toString() {
		return Arrays.toString(toArray());
	}
	
	
}
