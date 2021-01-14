package hr.fer.oprpp1.custom.collections;

import java.util.Arrays;
import java.util.Objects;

/**
 * Implementacija uređene kolekcije objekata (liste) promjenjive veličine potpomognuta poljem u pozadini.
 * 
 * <p>Razred ispravno implementira sve neispravno implementirane 
 * metode razreda {@link Collection}.
 * Uz to, razred nudi i metode kojima se može kolekcijom manipulirati indeksiranjem.
 * 
 * <p>Razred omogućava pohranu više identičnih elemenata, ali ne omogućava
 * pohranu {@code null} referenci.
 * 
 * @author Tomislav Bjelčić
 *
 */
public class ArrayIndexedCollection extends Collection {
	
	
	/**
	 * Pretpostavljena inicijalna veličina (kapacitet) polja unutar ove kolekcije.
	 */
	public static final int DEFAULT_INITIAL_CAPACITY = 16;
	
	/**
	 * Broj pohranjenih elemenata. On je manji ili jednak kapacitetu polja
	 * {@code elements}.
	 */
	private int size = 0;
	/**
	 * Pozadinsko polje objekata u kojem se pohranjuju elementi ove kolekcije.
	 */
	private Object[] elements;
	
	/**
	 * Stvara novu praznu kolekciju sa pretpostavljenim inicijalnim 
	 * kapacitetom polja 16.
	 */
	public ArrayIndexedCollection() {
		this(DEFAULT_INITIAL_CAPACITY);
	}
	
	/**
	 * Stvara novu praznu kolekciju sa inicijalnim kapacitetom 
	 * {@code initialCapacity}.
	 * 
	 * @param initialCapacity inicijalni kapacitet polja.
	 * @throws IllegalArgumentException ako je specificirani inicijalni kapacitet
	 * manji od 1.
	 */
	public ArrayIndexedCollection(int initialCapacity) {
		if (initialCapacity < 1)
			throw new IllegalArgumentException("Neispravan početni kapacitet: " + initialCapacity);
		
		elements = new Object[initialCapacity];
	}
	
	/**
	 * Stvara novu kolekciju koja se popunjava elementima predane
	 * kolekcije {@code col}.
	 * 
	 * @param col kolekcija čijim se elementima puni ova kolekcija.
	 * @throws NullPointerException ako je predana kolekcija {@code null}.
	 */
	public ArrayIndexedCollection(Collection col) {
		this(col, DEFAULT_INITIAL_CAPACITY);
	}
	
	/**
	 * Stvara novu kolekciju inicijalnog kapaciteta {@code initialCapacity} 
	 * koja se popunjava elementima predane kolekcije {@code col}.
	 * 
	 * <p>Ako je inicijalni kapacitet manji od veličine kolekcije {@code col}, 
	 * tada će veličina kolekcije {@code col} odrediti inicijalni kapacitet.
	 * 
	 * @param col kolekcija čijim se elementima puni ova kolekcija.
	 * @param initialCapacity inicijalni kapacitet polja.
	 * 
	 * @throws NullPointerException ako je predana kolekcija {@code null}.
	 * @throws IllegalArgumentException ako je predana kolekcija prazna
	 * i inicijalni kapacitet je manji od 1.
	 */
	public ArrayIndexedCollection(Collection col, int initialCapacity) {
		this(
				Math.max(
							col.size(),
							initialCapacity
						)
			);					// zadaću stvaranja dovoljno velikog polja za pohranu preuzima samo jedan konstruktor
		
		// polje je u ovom trenutku stvoreno, potrebno ga je samo napuniti elementima predane kolekcije
		addAll(col);
	}
	
	@Override
	public int size() {
		return size;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @throws NullPointerException ako je predani objekt {@code null}.
	 */
	@Override
	public void add(Object value) {
		Objects.requireNonNull(value, "Predani element je null.");
		
		if (checkFull())
			doubleCapacity();
		
		elements[size++] = value;
	}
	
	/**
	 * Pomoćna metoda koja provjerava je li kapacitet polja popunjen.
	 */
	private boolean checkFull() {
		return size == elements.length;
	}
	
	/**
	 * Udvostručuje kapacitet polja.
	 */
	private void doubleCapacity() {
		int doubledCapacity = elements.length * 2;
		Object[] doubledCapacityCopiedArray = Arrays.copyOf(elements, doubledCapacity);
		elements = doubledCapacityCopiedArray;
	}
	
	/**
	 * Dohvaća element na poziciji {@code index}.
	 * 
	 * @param index pozicija elementa.
	 * @return element na poziciji {@code index}.
	 * @throws IndexOutOfBoundsException ako je specificirana pozicija
	 * izvan raspona od 0 (uključivo) do veličine kolekcije (isključivo).
	 */
	public Object get(int index) {
		Objects.checkIndex(index, size);
		return elements[index];
	}
	
	@Override
	public void clear() {
		Arrays.fill(elements, 0, size, null);
		size = 0;
	}
	
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
	 * izvan raspona od 0 (uključivo) do veličine kolekcije (uključivo).
	 */
	public void insert(Object value, int position) {
		Objects.requireNonNull(value, "Predani element za umetanje je null.");
		Objects.checkIndex(position, size+1);
		
		if (checkFull())
			doubleCapacity();
		
		for (int i=size-1; i>=position; i--) {
			elements[i+1] = elements[i];
		}
		elements[position] = value;
		size++;
	}
	
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
	public int indexOf(Object value) {
		if (value == null)
			return -1;
		for (int i=0; i<size; i++) {
			if (value.equals(elements[i]))
				return i;
		}
		
		return -1;
	}
	
	/**
	 * Uklanja element na specificiranoj poziciji {@code index}.
	 * 
	 * <p>Kao posljedica će svi elementi koji su prethodno bili na pozicijama većim 
	 * ili jednakim od {@code position} sada biti na pozicijama za jedan manji.
	 * 
	 * @param index pozicija sa koje se element uklanja.
	 * @throws IndexOutOfBoundsException ako je specificirana pozicija
	 * izvan raspona od 0 (uključivo) do veličine kolekcije (isključivo).
	 */
	public void remove(int index) {
		Objects.checkIndex(index, size);
		
		for (int i=index; i<size-1; i++) {
			elements[i] = elements[i+1];
		}
		size--;
	}
	
	@Override
	public boolean remove(Object value) {
		int index = indexOf(value);
		
		if (index == -1)
			return false;
		remove(index);
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @throws NullPointerException ukoliko je predani Procesor {@code null}.
	 */
	@Override
	public void forEach(Processor processor) {
		Objects.requireNonNull(processor, "Predani procesor je null.");
		
		for (int i=0; i<size; i++)
			processor.process(elements[i]);
	}
	
	@Override
	public Object[] toArray() {
		return Arrays.copyOf(elements, size);
	}
	
	@Override
	public boolean contains(Object value) {
		return indexOf(value) != -1;
	}
	
}
