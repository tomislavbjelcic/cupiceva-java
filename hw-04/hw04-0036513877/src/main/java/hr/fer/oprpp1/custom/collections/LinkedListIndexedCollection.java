package hr.fer.oprpp1.custom.collections;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Implementacija uređene kolekcije objekata pomoću dvostruko povezane liste.
 * 
 * <p>Razred omogućava pohranu više identičnih elemenata, ali ne omogućava
 * pohranu {@code null} referenci.
 * 
 * @author Tomislav Bjelčić
 * @param <E> tip elemenata ove liste.
 *
 */
public class LinkedListIndexedCollection<E> implements List<E> {
	
	/**
	 * Pomoćni razred koji predstavlja čvor dvostruko povezane liste.
	 *
	 */
	private static class ListNode<E> {
		/**
		 * Referenca na sljedeći čvor (desni susjed).
		 */
		ListNode<E> next;		// nadalje nema potrebe za privatnim modifikatorima jer je razred privatan
		/**
		 * Referenca na prethodni čvor (lijevi susjed).
		 */
		ListNode<E> prev;
		/**
		 * Vrijednost, odnosno podatak koju pohranjuje taj čvor.
		 */
		E value;
		
		ListNode(ListNode<E> next, ListNode<E> prev, E value) {
			this.next = next;
			this.prev = prev;
			this.value = value;
		}
		
		/**
		 * Metoda koja vraća prethodnika ili sljedbenika, ovisno o predanom parametru
		 * {@code direction} koji će dati informaciju otkud se iterira kroz listu.
		 * 
		 * @param direction smjer iteracije.
		 * @return prethodni ili sljedeći čvor.
		 */
		ListNode<E> getNeighbor(int direction) {
			return direction == ITERATION_FROM_HEAD ? next : prev;
		}
	}
	
	/**
	 * Implementacija {@code ElementsGetter}-a, objekta koji dohvaća elemente kolekcije jedan 
	 * po jedan, prikladan za ovu kolekciju.
	 * 
	 * @author Tomislav Bjelčić
	 * @params <E> tip elemenata koji se dohvaća.
	 */
	private static class LinkedListElementsGetter<E> implements ElementsGetter<E> {
		/**
		 * Referenca na trenutni čvor dvostruko povezane liste sa koje će se dohvatiti sljedeći element.
		 */
		ListNode<E> currentNode;
		/**
		 * Pamti (snima), u trenutku stvaranja ovog objekta, broj strukturnih modifikacija 
		 * dvostruko povezane liste.
		 */
		final long savedModCount;
		/**
		 * Referenca na vanjsku kolekciju. Ovo je potrebno jer je ovaj razred statičan pa nema izravan pristup 
		 * članskih varijabli vanjskog razreda.
		 */
		LinkedListIndexedCollection<E> list;
		
		LinkedListElementsGetter(LinkedListIndexedCollection<E> list) {
			this.list = list;
			currentNode = list.first;
			savedModCount = list.modificationCount;
		}
		
		/**
		 * Pomoćna metoda koja provjerava je li se vanjska kolekcija strukturno 
		 * promijenila i ako jest, izaziva {@code ConcurrentModificationException}.
		 * 
		 * @throws ConcurrentModificationException ako se vanjska kolekcija strukturno promijenila.
		 */
		void checkForConcurrentModification() {
			if (savedModCount != list.modificationCount)
				throw new ConcurrentModificationException();
		}
		
		/**
		 * {@inheritDoc}
		 * 
		 * @throws ConcurrentModificationException ako se u međuvremenu kolekcija promijeni.
		 */
		@Override
		public boolean hasNextElement() {
			checkForConcurrentModification();
			return currentNode != null;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @throws ConcurrentModificationException ako se u međuvremenu kolekcija promijeni.
		 * @throws NoSuchElementException ako se pokuša dohvatiti element, a nema više nedohvaćenih elemenata.
		 */
		@Override
		public E getNextElement() {
			checkForConcurrentModification();
			if (!hasNextElement())
				throw new NoSuchElementException("Nema više elemenata.");
			
			E element = currentNode.value;
			currentNode = currentNode.next;
			return element;
		}
		
	}
	
	/**
	 * Referenca na prvi čvor (glava) u listi. 
	 * Njegov prethodnik je {@code null}.
	 */
	private ListNode<E> first;
	/**
	 * Referenca na zadnji čvor (rep) u listi.
	 * Njegov sljedbenik je {@code null}.
	 */
	private ListNode<E> last;
	/**
	 * Broj pohranjenih elemenata.
	 */
	private int size = 0;
	/**
	 * Broj strukturnih promjena ove kolekcije otkad je stvorena.
	 */
	private long modificationCount = 0L;
	
	/**
	 * Pomoćna varijabla kodirana pomoću cijelog broja koja 
	 * se koristi u metodama u kojima je potrebno odrediti otkud 
	 * započeti iteraciju kroz listu.
	 *  
	 * <p>Ova varijabla označuje da 
	 * se kroz listu iterira od glave.
	 */
	private static final int ITERATION_FROM_HEAD = 1;
	/**
	 * Pomoćna varijabla kodirana pomoću cijelog broja koja 
	 * se koristi u metodama u kojima je potrebno odrediti otkud 
	 * započeti iteraciju kroz listu. 
	 * 
	 * <p>Ova varijabla označuje da 
	 * se kroz listu iterira od repa.
	 */
	private static final int ITERATION_FROM_TAIL = -1;
	
	/**
	 * Stvara novu praznu kolekciju.
	 */
	public LinkedListIndexedCollection() {}
	
	/**
	 * Stvara novu kolekciju koja se popunjava elementima predane
	 * kolekcije {@code col}.
	 * 
	 * @param col kolekcija čijim se elementima puni ova kolekcija.
	 * @throws NullPointerException ako je predana kolekcija {@code null}.
	 */
	public LinkedListIndexedCollection(Collection<? extends E> col) {
		addAll(col);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @throws NullPointerException ako je predani objekt {@code null}.
	 */
	@Override
	public void add(E value) {
		Objects.requireNonNull(value, "Predani element je null");
		addLast(value);
	}
	
	/**
	 * Pomoćna metoda koja se poziva iz metoda koje rade strukturnu promjenu 
	 * ove kolekcije. Metoda povećava broj strukturnih promjena za 1.
	 */
	private void modified() {
		modificationCount++;
	}
	
	/**
	 * Pomoćna metoda koja dodaje predani objekt {@code value} na kraj liste.
	 * 
	 * @param value objekt koji se dodaje na kraj liste.
	 */
	private void addLast(E value) {
		ListNode<E> newListNode = new ListNode<>(null, last, value);
		if (isEmpty()) {
			first = last = newListNode;
		} else {
			last.next = newListNode;
			last = newListNode;
		}
		size++;
		modified();
	}
	
	/**
	 * Pomoćna metoda koja uklanja zadnji element liste.
	 */
	private void removeLast() {
		if (size < 1) {
			first.value = null;
			first = last = null;
		} else {
			ListNode<E> toRemove = last;
			last = last.prev;
			
			last.next = null;
			toRemove.prev = null;
			toRemove.value = null;
		}
		size--;
		modified();
	}
	
	/**
	 * Pomoćna metoda koja uklanja prvi element liste.
	 */
	private void removeFirst() {
		if (size < 1) {
			first.value = null;
			first = last = null;
		} else {
			ListNode<E> toRemove = first;
			first = first.next;
			
			first.prev = null;
			toRemove.next = null;
			toRemove.value = null;
		}
		size--;
		modified();
	}
	
	/**
	 * Pomoćna metoda koja dodaje predani objekt {@code value} na početak liste.
	 * 
	 * @param value objekt koji se dodaje na početak liste.
	 */
	private void addFirst(E value) {
		ListNode<E> newListNode = new ListNode<>(first, null, value);
		if (isEmpty()) {
			first = last = newListNode;
		} else {
			first.prev = newListNode;
			first = newListNode;
		}
		size++;
		modified();
	}
	
	@Override
	public int size() {
		return size;
	}
	
	@Override
	public boolean contains(Object value) {
		if (value == null)
			return false;
		
		return getNode(value) != null;
	}
	
	
	/**
	 * Dohvaća element na poziciji {@code index}.
	 * 
	 * @param index pozicija elementa.
	 * @return element na poziciji {@code index}.
	 * @throws IndexOutOfBoundsException ako je specificirana pozicija
	 * izvan raspona od 0 (uključivo) do veličine kolekcije (isključivo).
	 */
	@Override
	public E get(int index) {
		Objects.checkIndex(index, size);
		
		ListNode<E> node = getNode(index);
		return node.value;
	}
	
	/**
	 * Pomoćna metoda koja dohvaća čvor na poziciji {@code index}.
	 * 
	 * <p>Kako se radi o dvostruko povezanoj listi, nije moguće izravno pristupiti 
	 * bilo kojem čvoru, stoga se to mora učiniti iteriranjem i brojanjem pređenih
	 * čvorova. Metoda to radi na optimalan način, odnosno ako se dohvaća čvor sa pozicije 
	 * koja je bliža početku, iteriranje kreće od glave, a je pozicija bliža kraju, 
	 * iteriranje kreće od repa.
	 * 
	 * @param index pozicija s koje se dohvaća čvor.
	 * @return čvor na poziciji {@code} index.
	 */
	private ListNode<E> getNode(int index) {
		int searchDirection = iterationDirection(index);
		ListNode<E> currentNode = searchDirection == ITERATION_FROM_HEAD ? first : last;
		
		int fromClosestEndIndex = searchDirection == ITERATION_FROM_HEAD ? index : (size - 1) - index;
		for (int i=0; i<fromClosestEndIndex; i++) {
			currentNode = currentNode.getNeighbor(searchDirection);
		}
		
		return currentNode;
	}
	
	/**
	 * Pomoćna metoda koja vraća prvi čvor (krećući od glave) koji pohranjuje vrijednost 
	 * predanog objekta {@code value}. Ukoliko takav čvor ne postoji, metoda vraća 
	 * {@code null}.
	 * 
	 * @param value objekt za koga se dohvaća čvor koji sadrži takvu vrijednost.
	 * @return čvor koji sadrži vrijednost {@code value}.
	 */
	private ListNode<E> getNode(Object value) {
		for (ListNode<E> currentNode = first; currentNode != null; currentNode = currentNode.next) {
			if (currentNode.value.equals(value))
				return currentNode;
		}
		return null;
	}
	
	/**
	 * Pomoćna metoda koja za predanu poziciju {@code index} vraća informaciju 
	 * (kodirana kao cijeli broj) koja predstavlja odgovor na pitanje otkud treba 
	 * pokrenuti potragu (iteraciju) da bi se u najmanje koraka došlo do čvora na
	 * poziciji {@code index}.
	 * 
	 * @param index pozicija.
	 * @return cijeli broj koji govori treba li iterirati od glave ili od repa.
	 */
	private int iterationDirection(int index) {
		int mid = (size - 1)/2;
		return index <= mid ? ITERATION_FROM_HEAD : ITERATION_FROM_TAIL;
	}
	
	
	@Override
	public void clear() {
		first = last = null;
		size = 0;
		modified();
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
	@Override
	public void insert(E value, int position) {
		Objects.requireNonNull(value, "Predani element za umetanje je null.");
		Objects.checkIndex(position, size+1);
		
		if (position == 0) {
			addFirst(value);
			return;
		}
		if (position == size) {
			addLast(value);
			return;
		}
		
		/*  ubacivanje nije na ekstremima, što znači da postoji čvor na position
			i postoji njegov prethodnik
		*/
		ListNode<E> rightPosListNode = getNode(position);
		ListNode<E> leftPosListNode = rightPosListNode.prev;
		
		ListNode<E> newListNode = new ListNode<>(rightPosListNode, leftPosListNode, value);
		
		leftPosListNode.next = newListNode;
		rightPosListNode.prev = newListNode;
		
		size++;
		modified();
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
	@Override
	public int indexOf(Object value) {
		ListNode<E> currentNode = first;
		
		for (int i=0; i<size; i++) {
			if (currentNode.value.equals(value))
				return i;
			currentNode = currentNode.next;
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
	@Override
	public void remove(int index) {
		Objects.checkIndex(index, size);
		
		removeNode(getNode(index));
	}
	
	/**
	 * Pomoćna metoda koja uklanja predani čvor iz liste.
	 * 
	 * @param čvor koji se uklanja.
	 */
	private void removeNode(ListNode<E> node) {
		if (node.next == null) {
			removeLast();
			return;
		}
		if (node.prev == null) {
			removeFirst();
			return;
		}
		
		ListNode<E> left = node.prev;
		ListNode<E> right = node.next;
		
		node.value = null;
		node.next = node.prev = null;
		left.next = right;
		right.prev = left;
		size--;
		modified();
	}
	
	@Override
	public boolean remove(Object value) {
		if (value == null)
			return false;
		
		ListNode<E> valueNode = getNode(value);
		if (valueNode != null) {
			removeNode(valueNode);
			return true;
		} else
			return false;
	}
	
	@Override
	public Object[] toArray() {
		Object[] array = new Object[size];
		
		ListNode<E> currentNode = first;
		for (int i=0; i<size; i++) {
			array[i] = currentNode.value;
			currentNode = currentNode.next;
		}
		return array;
	}
	
	@Override
	public void forEach(Processor<? super E> processor) {
		Objects.requireNonNull(processor, "Predani procesor je null.");

		for (ListNode<E> currentNode = first; currentNode != null; currentNode = currentNode.next) {
			processor.process(currentNode.value);
		}
	}
	
	/**
	 * Vraća String reprezentaciju ove kolekcije.
	 * 
	 * @return String reprezentacija ove kolekcije.
	 */
	@Override
	public String toString() {
		return Arrays.toString(toArray());
	}

	@Override
	public ElementsGetter<E> createElementsGetter() {
		return new LinkedListElementsGetter<E>(this);
	}
	
}
