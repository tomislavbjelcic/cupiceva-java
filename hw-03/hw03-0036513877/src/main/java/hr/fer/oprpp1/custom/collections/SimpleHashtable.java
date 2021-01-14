package hr.fer.oprpp1.custom.collections;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Razred predstavlja implementaciju mape temeljena na tablici raspršenog adresiranja.<br>
 * Ključevi ove mape su jedinstveni i ne smiju biti {@code null} reference, dok vrijednosti smiju.<br>
 * Razred sadrži metode za manipulaciju i dohvat vrijednosti ove mape, te ima podršku za 
 * obilazak (iteraciju) po uređenim parovima {@link TableEntry} implementirajući {@link Iterable}.
 * 
 * @author Tomislav Bjelčić
 *
 * @param <K> tip ključa.
 * @param <V> tip vrijednosti.
 */
public class SimpleHashtable<K, V> implements Iterable<SimpleHashtable.TableEntry<K, V>>{
	
	/**
	 * Uređeni par {@code (ključ, vrijednost)} koji u mapi predstavlja preslikavanje 
	 * {@code ključ -> vrijednost}.
	 * 
	 * @author Tomislav Bjelčić
	 *
	 * @param <K> tip ključa.
	 * @param <V> tip vrijednosti.
	 */
	public static class TableEntry<K, V> {
		
		/**
		 * Ključ preslikavanja.
		 */
		private K key;
		/**
		 * Vrijednost preslikavanja.
		 */
		private V value;
		/**
		 * Referenca na sljedeći uređeni par u istom pretincu tablice mape.
		 */
		private TableEntry<K, V> next = null;
		
		/**
		 * Stvara novo preslikavanje {@code key -> value}.<br>
		 * Zbog ugovora mape {@link SimpleHashtable}, ključ ne smije biti {@code null}.
		 * 
		 * 
		 * @param key ključ preslikavanja.
		 * @param value vrijednost preslikavanja.
		 * 
		 * @throws NullPointerException ako je predani ključ {@code null}.
		 */
		public TableEntry(K key, V value) {
			this.key = Objects.requireNonNull(key, "Predani ključ je null.");
			setValue(value);
		}

		/**
		 * Dohvaća vrijednost ovog preslikavanja.
		 * 
		 * @return vrijednost ovog preslikavanja.
		 */
		public V getValue() {
			return value;
		}

		/**
		 * Postavlja vrijednost ovog preslikavanja na novu vrijednost {@code value}.
		 * 
		 * @param value nova vrijednost preslikavanja.
		 */
		public void setValue(V value) {
			this.value = value;
		}

		/**
		 * Dohvaća ključ ovog preslikavanja.
		 * 
		 * @return ključ ovog preslikavanja.
		 */
		public K getKey() {
			return key;
		}
		
		/**
		 * Uspoređuje uređene parove (preslikavanja mape) po ključevima.<br>
		 * Dva uređena para su jednaka ako i samo ako su im jednaki ključevi. 
		 * U svim ostalim slučajevima metoda vraća {@code false}.
		 * 
		 * @param obj uređeni par s kojim se uspoređuje ovaj uređeni par.
		 * @return {@code true} ako se ključevi podudaraju, inače {@code false}.
		 */
		@Override
		public boolean equals(Object obj) {
			if (obj == null)
				return false;
			if (this == obj)
				return true;
			if (this.getClass() != obj.getClass())
				return false;
			
			TableEntry<?,?> other = (TableEntry<?,?>) obj;
			boolean eq = this.key.equals(other.key); 
			//		&& Objects.equals(this.value, other.value); ne jer se gleda samo ključ
			return eq;
		}
		
		/**
		 * Vraća String reprezentaciju ovog preslikavanja.
		 * 
		 * @return String reprezentacija ovog preslikavanja.
		 */
		@Override
		public String toString() {
			String keyStr = key.toString();
			String valueStr = String.valueOf(value);
			StringBuilder sb = new StringBuilder();
			sb.append(keyStr).append('=').append(valueStr);
			return sb.toString();
		}
		
	}
	
	/**
	 * Implementacija iteratora nad ovom mapom.
	 * 
	 * @author Tomislav Bjelčić
	 *
	 */
	private class IteratorImpl implements Iterator<SimpleHashtable.TableEntry<K, V>> {
		
		/**
		 * Referenca na preslikavanje koje je zadnje vraćeno pozivom metode next.<br>
		 * Poziv metode remove će ukloniti ovo preslikavanje.
		 */
		TableEntry<K, V> currentEntry = null;
		/**
		 * Referenca na preslikavanje koje u listi prethodi {@code currentEntry}, dakle u istom pretincu.<br>
		 * Ako je ova referenca {@code null}, to znači da je {@code currentEntry} prvi zapis u pretincu 
		 * (nema prethodnika).
		 */
		TableEntry<K, V> currentEntryPrev = null;
		/**
		 * Pozicija pretinca preslikavanja {@code currentEntry}.
		 */
		int currentSlot = -1;
		
		/**
		 * Referenca na preslikavanje koje je kandidat za sljedeći poziv metode next.
		 */
		TableEntry<K, V> nextEntry = null;
		/**
		 * Referenca na prethodno preslikavanje od {@code nextEntry} u ulančanoj listi u istom pretincu.<br>
		 * Kao i sa {@code currentPrevEntry}, ako je ova referenca {@code null}, to znači da je 
		 * {@code nextEntry prvi zapis u pretincu.}
		 */
		TableEntry<K, V> nextEntryPrev = null;
		/**
		 * Pozicija pretinca preslikavanja {@code nextEntry}.
		 */
		int nextSlot = -1;
		{
			locateNextSlotStart();
		}
		
		/**
		 * Zapamćen broj strukturnih promjena mape nad kojim ovaj iterator prolazi.<br>
		 * Koristi se za detektiranje modifikacije tijekom iteriranja pri čemu dolazi do 
		 * iznimke {@code ConcurrentModificationException}.
		 */
		long savedModCount = modificationCount;
		/**
		 * Zastavica koja se postavlja pri svakom pozivu metode next i poništava pri svakom 
		 * pozivu metode remove.<br>
		 * Koristi se za detektiranje ilegalnih poziva koje tada uzrokuju 
		 * {@code IllegalStateException}.
		 */
		boolean nextCalled = false;
		
		
		void locateNextEntry() {
			var possibleNext = nextEntry.next;
			if (possibleNext != null) {
				nextEntryPrev = nextEntry;
				nextEntry = possibleNext;
			} else {
				// nema više elemenata u trenutnom slotu, potraži početak sljedećeg nepraznog slota
				locateNextSlotStart();
			}
		}
		
		void locateNextSlotStart() {
			nextEntry = nextEntryPrev = null;
			nextSlot++;
			for (int len=table.length; nextSlot<len; nextSlot++) {
				if (table[nextSlot] != null) {
					nextEntry = table[nextSlot];
					break;
				}
			}
		}
		
		/**
		 * {@inheritDoc}
		 * 
		 * @throws ConcurrentModificationException ako se metoda pozove a da je 
		 * prethodno mapa izvana (bez korištenja metode {@link Iterator#remove()}) 
		 * strukturno promijenjena.
		 */
		@Override
		public boolean hasNext() {
			checkForConcurrentModification();
			return nextEntry != null;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @throws ConcurrentModificationException ako se metoda pozove a da je 
		 * prethodno mapa izvana (bez korištenja metode {@link Iterator#remove()}) 
		 * strukturno promijenjena.
		 * @throws NoSuchElementException ako se metoda pozove a da je prethodno iterator 
		 * već obišao cijelu mapu.
		 */
		@Override
		public TableEntry<K, V> next() {
			checkForConcurrentModification();
			if (!hasNext())
				throw new NoSuchElementException("Nema više elemenata.");
			
			nextCalled = true;
			
			currentEntry = nextEntry;
			currentEntryPrev = nextEntryPrev;
			currentSlot = nextSlot;
			locateNextEntry();
			
			return currentEntry;
		}
		
		/**
		 * {@inheritDoc}
		 * 
		 * @throws ConcurrentModificationException ako se metoda pozove a da je 
		 * prethodno mapa izvana (bez korištenja metode {@link Iterator#remove()}) 
		 * strukturno promijenjena.
		 * @throws IllegalStateException ako se metoda pozove dvaput nad istim elementom, 
		 * odnosno ako prethodno nije bila pozvana metoda next.
		 */
		@Override
		public void remove() {
			checkForConcurrentModification();
			if (!nextCalled)
				throw new IllegalStateException();
			
			nextCalled = false;
			
			SimpleHashtable.this.removeTableEntry(currentEntry, currentEntryPrev, currentSlot);
			nextEntryPrev = currentEntryPrev;
			savedModCount++;
		}
		
		void checkForConcurrentModification() {
			if (savedModCount != modificationCount)
				throw new ConcurrentModificationException("Strukturna promjena mape izvana tijekom iteriranja.");
		}
	}
	
	/**
	 * Pretpostavljeni inicijalni broj pretinca tablice mape.
	 */
	public static final int DEFAULT_INITIAL_CAPACITY = 16;
	/**
	 * Granica popunjenosti tablice kada će se broj pretinaca udvostručiti.
	 */
	public static final double LOAD_FACTOR_THRESHOLD = 0.75;
	
	/**
	 * Tablica (polje) raspršenog adresiranja sa određenim brojem pretinaca (duljina polja).
	 */
	private TableEntry<K, V>[] table;
	/**
	 * Broj pohranjenih uređenih parova {@code (ključ, vrijednost)}, odnosno preslikavanja 
	 * {@code ključ -> vrijednost}.
	 */
	private int size = 0;
	/**
	 * Broj strukturnih promjena mape otkad je stvorena.
	 */
	private long modificationCount = 0L;
	
	/**
	 * Stvara novu mapu sa pretpostavljenim brojem pretinaca tablice 16.
	 */
	public SimpleHashtable() {
		this(DEFAULT_INITIAL_CAPACITY);
	}
	
	/**
	 * Stvara novu mapu sa brojem pretinaca tablice koja je potencija broja 2 veća ili jednaka 
	 * od {@code initialCapacity}.<br>
	 * Primjerice, ako se kao argument preda broj 20, broj pretinaca bit će 32.
	 * 
	 * @param initialCapacity donja granica broja pretinaca tablice. Pravi broj pretinaca 
	 * bit će potencija broja 2 veća ili jednaka od {@code initialCapacity}.
	 * @throws IllegalArgumentException ako je predani broj pretinaca manji od 1, ili takav da 
	 * potencija broja 2 veća ili jednaka predanom broju nije prikaziva u obliku 32-bitnog 
	 * dvojnog komplementa cijelih brojeva.
	 */
	public SimpleHashtable(int initialCapacity) {
		int capacity = calculateCapacity(initialCapacity);
		table = createArray(capacity);
	}
	
	/**
	 * Pomoćna metoda koja računa potenciju broja 2 veću ili jednaku predanom broju 
	 * {@code capacity}.
	 * 
	 * @param capacity donja granica
	 * @return potencija broja 2 veća ili jednaka {@code capacity}.
	 * @throws IllegalArgumentException ako je predani broj pretinaca manji od 1, ili takav da 
	 * potencija broja 2 veća ili jednaka predanom broju nije prikaziva u obliku 32-bitnog 
	 * dvojnog komplementa cijelih brojeva.
	 */
	private static int calculateCapacity(int capacity) {
		if (capacity < 1)
			throw new IllegalArgumentException("Neispravan početni kapacitet: " + capacity);
		
		if (capacity > (Integer.MIN_VALUE >>> 1))
			throw new IllegalArgumentException("Zbog opsega prikaza cijelih brojeva "
					+ "nije moguće pronaći sljedeću potenciju broja 2 veću od "
					+ capacity);
		
		int leadingZeroCount = Integer.numberOfLeadingZeros(capacity - 1) - 1;
		return Integer.MIN_VALUE >>> leadingZeroCount;
	}
	
	/**
	 * Pomoćna metoda koja stvara novo prazno polje veličine {@code capacity} čiji 
	 * svaki element je preslikavanje mape, dakle tipa {@code TableEntry<K, V>}.
	 * 
	 * @param <K> tip ključa.
	 * @param <V> tip vrijednosti.
	 * @param capacity veličina stvorenog polja.
	 * @return novo polje veličine {@code capacity}.
	 */
	@SuppressWarnings("unchecked")
	private static <K, V> TableEntry<K, V>[] createArray(int capacity) {
		return (TableEntry<K, V>[]) new TableEntry[capacity];
	}
	
	/**
	 * Pomoćna metoda koja računa u koji pretinac se mora staviti preslikavanje sa 
	 * ključem {@code key}.<br>
	 * Broj (pozicija) pretinca se računa sljedećom formulom: 
	 * {@code |H| % P}, gdje {@code H} predstavlja funkciju raspršenja ključa 
	 * {@link Object#hashCode()}, a {@code P} broj pretinaca tablice. Modulo operacija 
	 * osigurava da će preslikavanje uvijek biti smješteno u pretinac u intervalu 
	 * {@code [0, P-1]}.
	 * 
	 * @param key
	 * @return pozicija pretinca tablice u kojoj pripada preslikavanje sa ključem {@code key}.
	 */
	private int slot(Object key) {
		return Math.abs(key.hashCode()) % table.length;
	}
	
	/**
	 * Stvara novo preslikavanje {@code key -> value} ako preslikavanje 
	 * sa takvim ključem ne postoji i povratna vrijednost će biti {@code null}.<br>
	 * Ako ključ {@code key} već postoji, njegova pridružena vrijednost će se zamijeniti 
	 * novom vrijednošću {@code value}, a stara vrijednost će se vratiti pozivatelju. 
	 * 
	 * @param key ključ preslikavanja.
	 * @param value nova vrijednost preslikavanja.
	 * @return staru vrijednost ako je preslikavanje sa ključem {@code key} postojalo, 
	 * inače vraća {@code null}.
	 * @throws NullPointerException ako je predani ključ {@code null}.
	 */
	public V put(K key, V value) {
		Objects.requireNonNull(key, "Predani ključ je null.");
		
		int slot = slot(key);
		
		var current = table[slot];
		if (current == null) {
			table[slot] = new TableEntry<>(key, value);
			size++;
			modified();
			adjustTable();
			return null;
		}
		
		for (; ; current = current.next) {
			if (key.equals(current.key)) {
				V oldVal = current.value;
				current.value = value;
				return oldVal;
			}
			if (current.next == null)
				break;
		}
		
		// ključ key nije pronađen, stvori novi
		var newEntry = new TableEntry<>(key, value);
		size++;
		current.next = newEntry;
		modified();
		adjustTable();
		return null;
	}
	
	/**
	 * Pomoćna metoda koja provjerava popunjenost tablice, te ako je ona veća ili jednaka 
	 * prethodno definiranoj granici, stvara tablicu sa dvostruko više pretinaca te sva preslikavanja iz 
	 * stare tablice stavlja u novu.
	 */
	private void adjustTable() {
		if (loadFactor() < LOAD_FACTOR_THRESHOLD)
			return;
		
		TableEntry<K, V>[] entries = toArray();
		table = createArray(table.length << 1);
		size = 0;
		for (var entry : entries) {
			put(entry.key, entry.value);
		}
		modified();
	}
	
	/**
	 * Vraća popunjenost tablice.<br>
	 * Popunjenost se definira kao omjer pohranjenih preslikavanja i broja pretinca tablice.
	 * 
	 * @return popunjenost tablice.
	 */
	private double loadFactor() {
		double loadFactor = size / ((double) table.length);
		return loadFactor;
	}
	
	/**
	 * Pretražuje tablicu i vraća referencu na preslikavanje sa ključem {@code key}.<br>
	 * Ako preslikavanje sa takvim ključem nije postojalo, metoda vraća {@code null}.
	 * 
	 * @param key ključ preslikavanja.
	 * @return uređeni par (preslikavanje) sa ključem {@code key} ako je takvo postojalo, inače {@code null}.
	 * @throws NullPointerException ako je predani ključ {@code null}.
	 */
	private TableEntry<K, V> getTableEntry(Object key) {
		Objects.requireNonNull(key, "Predani ključ je null.");
		
		int slot = slot(key);
		
		for (var current = table[slot]; current != null ; current = current.next) {
			if (key.equals(current.key))
				return current;
		}
		
		return null;
	}
	
	/**
	 * Pretražuje tablicu po pretincima i dohvaća prvo preslikavanje koje ima vrijednost {@code value}.<br>
	 * Ako niti jedno preslikavanje nema vrijednost {@code value}, metoda vraća {@code null}.
	 * 
	 * @param value vrijednost za čije se preslikavanje pretražuje prva pojava.
	 * @return prvo preslikavanje sa vrijednosti {@code value} ako takvo postoji, inače {@code null}.
	 */
	private TableEntry<K, V> getTableEntryUsingValue(Object value) {
		for (int slot=0, len=table.length; slot<len; slot++) {
			for (var current = table[slot]; current != null ; current = current.next) {
				if (Objects.equals(value, current.value))
					return current;
			}
		}
		return null;
	}
	
	/**
	 * Dohvaća vrijednost pridruženu predanom ključu ako takvo preslikavanje postoji, 
	 * inače metoda vraća {@code null}.<br>
	 * Ova metoda nije ispravan način za provjeravanje postoji li preslikavanje sa ključem {@code key}, jer 
	 * je moguće da takvo preslikavanje postoji i njezina pridružena vrijednost je upravo {@code null}, što je dozvoljeno.
	 * 
	 * @param key ključ preslikavanja.
	 * @return vrijednost pridružena predanom ključu ako takvog ključa ima u mapi, inače {@code null}.
	 * @throws NullPointerException ako je predani ključ {@code null}.
	 */
	public V get(Object key) {
		var entry = getTableEntry(key);
		return entry == null ? null : entry.value;
	}
	
	/**
	 * Vraća broj pohranjenih preslikavanja {@code ključ -> vrijednost}, odnosno uređenih parova 
	 * {@code (ključ, vrijednost)}.
	 * 
	 * @return broj pohranjenih preslikavanja u ovoj mapi.
	 */
	public int size() {
		return size;
	}
	
	/**
	 * Provjerava postoji li preslikavanje sa ključem {@code key}.
	 * 
	 * @param key ključ preslikavanja.
	 * @return {@code true} ako preslikavanje sa ključem {@code key} postoji, inače {@code false}.
	 * @throws NullPointerException ako je predani ključ {@code null}.
	 */
	public boolean containsKey(Object key) {
		return getTableEntry(key) != null;
	}
	
	/**
	 * Provjerava postoji li preslikavanje sa vrijednosću {@code value}.
	 * 
	 * @param key vrijednost preslikavanja.
	 * @return {@code true} ako postoji barem jedno preslikavanje sa vrijednošću {@code value}, inače {@code false}.
	 */
	public boolean containsValue(Object value) {
		return getTableEntryUsingValue(value) != null;
	}
	
	/**
	 * Pomoćna metoda koja prima referencu na preslikavanje {@code entry} te njenog 
	 * prethodnika {@code prev} <b>u istom pretincu {@code slot}</b>, te uklanja preslikavanje {@code entry} iz tablice, 
	 * odnosno mape. Pri tome vrijednost uklonjenog preslikavanja vraća pozivatelju.<br>
	 * Ako je {@code prev == null}, to označuje da je {@code entry} prvi zapis u pretincu {@code slot}.
	 * 
	 * @param entry referenca na preslikavanje koje se miče
	 * @param prev referenca na preslikavanje prethodno od {@code entry} u pretincu {@code slot}.
	 * @param slot pretinac tablice u kojoj se nalaze {@code entry} i {@code prev}.
	 * @return vrijednost uklonjenog preslikavanja, dakle vrijednost od {@code entry}.
	 */
	private V removeTableEntry(TableEntry<K, V> entry, TableEntry<K, V> prev, int slot) {
		if (prev == null) {
			table[slot] = entry.next;
		} else {
			prev.next = entry.next;
		}
		
		entry.next = null;
		size--;
		modified();
		return entry.value;
	}
	
	/**
	 * Uklanja preslikavanje iz mape sa ključem {@code key}.<br>
	 * Usput, ako je takvo preslikavanje postojalo, vrijednost preslikavanja se vraća pozivatelju, u suprotnom 
	 * (ako nije postojalo) metoda vraća {@code null}.
	 * 
	 * @param key ključ preslikavanja.
	 * @return vrijednost uklonjenog preslikavanja ako je takav postojao, inače {@code null}.
	 * @throws NullPointerException ako je predani ključ {@code null}.
	 */
	public V remove(Object key) {
		Objects.requireNonNull(key, "Predani ključ je null.");
		
		int slot = slot(key);
		
		var current = table[slot];
		if (current == null) {
			return null;
		}
		
		for (TableEntry<K, V> prev = null; current != null; ) {
			if (key.equals(current.key)) {
				return removeTableEntry(current, prev, slot);
			}
			
			prev = current;
			current = current.next;
		}
		return null;
	}
	
	/**
	 * Provjerava je li ova mapa prazna (nema niti jedno preslikavanje).
	 * 
	 * @return {@code true} ako u ovoj mapi ne postoji niti jedno preslikavanje, inače {@code false}.
	 */
	public boolean isEmpty() {
		return size == 0;
	}
	
	/**
	 * Stvara novo polje u kojem se nalaze sva preslikavanja ove mape.
	 * 
	 * @return novo polje sa svim preslikavanjima ove mape.
	 */
	public TableEntry<K, V>[] toArray() {
		TableEntry<K, V>[] array = createArray(size);
		/*
		for (int i=0, slot=0, len=table.length; slot<len && i<size; slot++) {
			for (var current = table[slot]; current != null ; current = current.next) {
				array[i++] = current;
			}
		}
		*/
		var it = iterator();
		for (int i=0; i<size; i++) {
			array[i] = it.next();
		}
		return array;
	}
	
	/**
	 * Vraća String reprezentaciju ove mape.
	 * 
	 * @return String reprezentacija ove mape.
	 */
	@Override
	public String toString() {
		// one liner ali bezveze se stvara novo polje
		// return Arrays.toString(toArray());
		
		StringBuilder sb = new StringBuilder().append('[');
		var it = iterator();
		while (it.hasNext()) {
			var next = it.next();
			sb.append(next);
			var more = it.hasNext();
			if (more)
				sb.append(", ");
		}
		return sb.append(']').toString();
	}
	
	/**
	 * Uklanja sva preslikavanja iz ove mape.
	 */
	public void clear() {
		var it = iterator();
		while (it.hasNext()) {
			it.next();
			it.remove();
		}
		
		/*
		Prethodni kod pomaže Garbage collectoru, no brže ćemo ukloniti sve ako koristimo
		
		for (int i=0, len=table.length; i<len; i++)
			table[i] = null;
		size = 0;
		
		modified();
		*/
		
		
		
		return;
	}

	/**
	 * Stvara novi {@link Iterator}, objekt sposoban za prolazak (iteriranje) kroz sva preslikavanja ove mape.<br>
	 * Svaki element iteracije je tipa {@code TableEntry<K, V>}.
	 * 
	 * @return novi iterator nad preslikavanjima ove mape.
	 */
	@Override
	public Iterator<TableEntry<K, V>> iterator() {
		return new IteratorImpl();
	}
	
	/**
	 * Pomoćna metoda čiji poziv označava da se radi o strukturnoj promjeni mape te se posljedično 
	 * povećava broj strukturnih promjena za 1.
	 */
	private void modified() {
		modificationCount++;
	}
	
}
