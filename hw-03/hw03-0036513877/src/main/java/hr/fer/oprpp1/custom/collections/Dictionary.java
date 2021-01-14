package hr.fer.oprpp1.custom.collections;

import java.util.Objects;

/**
 * Razred predstavlja riječnik, strukturu podataka koja sadrži preslikavanja na principu 
 * ključ -> vrijednost.<br>
 * Ključevi su jedinstveni i ne smiju biti {@code null}. Svaki pohranjeni ključ 
 * ima pridruženu vrijednost, koja može biti i {@code null}.
 * 
 * @author Tomislav Bjelčić
 *
 * @param <K> tip ključa.
 * @param <V> tip vrijednosti.
 */
public class Dictionary<K, V> {
	
	/**
	 * Predstavlja uređeni par (ključ, vrijednost). 
	 * Takvi parovi se spremaju u riječnik i predstavljaju jedno preslikavanje 
	 * ključ -> vrijednost.
	 * 
	 * @author Tomislav Bjelčić
	 *
	 * @param <K> tip ključa.
	 * @param <V> tip vrijednosti.
	 */
	private static class Entry<K, V> {
		/**
		 * Ključ preslikavanja.
		 */
		K key;
		/**
		 * Vrijednost preslikavanja.
		 */
		V value;
		
		/**
		 * Stvara novo preslikavanje {@code key -> value}.<br>
		 * Zbog ugovora riječnika {@link Dictionary}, ključ ne smije biti {@code null}.
		 * 
		 * 
		 * @param key ključ preslikavanja.
		 * @param value vrijednost preslikavanja.
		 * 
		 * @throws NullPointerException ako je predani ključ {@code null}.
		 */
		Entry(K key, V value) {
			this.key = Objects.requireNonNull(key, "Predani ključ je null.");
			this.value = value;
		}
		
		/**
		 * Uspoređuje uređene parove po ključevima.<br>
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
			
			Entry<?,?> other = (Entry<?,?>) obj;
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
	 * Skup preslikavanja (modelira se kao lista) ovog riječnika.
	 */
	private List<Entry<K, V>> storage = new ArrayIndexedCollection<>();
	
	
	
	/**
	 * Provjerava je li ovaj riječnik prazan, odnosno postoje li preslikavanja u 
	 * riječniku.
	 * 
	 * @return {@code true} ako postoji bar jedno preslikavanje, inače {@code false}.
	 */
	public boolean isEmpty() {
		return storage.isEmpty();
	}
	
	/**
	 * Vraća broj preslikavanja ovog riječnika.
	 * 
	 * @return broj preslikavanja ovog riječnika.
	 */
	public int size() {
		return storage.size();
	}
	
	/**
	 * Briše sva preslikavanja ovog riječnika.
	 */
	public void clear() {
		storage.clear();
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
		int index = this.indexOf(key);
		V retVal = null;
		if (index != -1) {
			Entry<K, V> old = storage.get(index);
			retVal = old.value;
			old.value = value;
		} else {
			Entry<K, V> newEntry = new Entry<>(key, value);
			storage.add(newEntry);
		}
		return retVal;
	}
	
	/**
	 * Dohvaća vrijednost preslikavanja sa ključem {@code key} 
	 * ako takvo preslikavanje postoji.<br>
	 * Ako ne postoji preslikavanje sa ključem {@code key}, metoda vraća {@code null}.
	 * 
	 * @param key ključ čija se pridružena vrijednost dohvaća.
	 * @return vrijednost preslikavanja sa ključem {@code key} ako postoji, inače 
	 * {@code null}.
	 * @throws NullPointerException ako je predani ključ {@code null}.
	 */
	public V get(Object key) {
		int index = this.indexOf(key);
		return (index == -1 ? null : storage.get(index).value);
	}
	
	/**
	 * Uklanja preslikavanje sa ključem {@code key} iz ovog riječnika.<br>
	 * Pri tome metoda vraća vrijednost koja je bila pridružena ključu ukoliko je takvo 
	 * preslikavanje postojalo, inače vraća {@code null}.
	 * 
	 * @param key ključ čije se preslikavanje uklanja iz riječnika.
	 * @return vrijednost pridružena ključu ako je takvog preslikavanja bilo prije 
	 * uklanjanja, inače {@code null}.
	 * @throws NullPointerException ako je predani ključ {@code null}
	 */
	public V remove(K key) {
		int index = indexOf(key);
		V retVal = null;
		if (index != -1) {
			retVal = storage.get(index).value;
			storage.remove(index);
		}
		return retVal;
	}
	
	/**
	 * Provjerava postoji li preslikavanje sa ključem {@code key} u ovom riječniku.
	 * 
	 * @param key ključ preslikavanja.
	 * @return {@code true} ako preslikavanje sa ključem {@code key} postoji, inače 
	 * {@code null}.
	 * @throws NullPointerException ako je predani ključ {@code null}.
	 */
	public boolean containsKey(Object key) {
		return indexOf(key) != -1;
	}
	
	/**
	 * Pomoćna metoda koja vraća poziciju preslikavanja sa ključem {@code key} u 
	 * internoj listi uređenih parova (preslikavanja). Ako takvo preslikavanje 
	 * ne postoji, metoda vraća -1.
	 * 
	 * @param key ključ preslikavanja.
	 * @return pozicija preslikavanja sa ključem {@code key} u internoj listi uređenih 
	 * parova. -1 ako takvo preslikavanje ne postoji.
	 * @throws NullPointerException ako je predani ključ {@code null}.
	 */
	private int indexOf(Object key) {
		Entry<?, ?> entry = new Entry<>(key, null);
		return storage.indexOf(entry);
	}
}
