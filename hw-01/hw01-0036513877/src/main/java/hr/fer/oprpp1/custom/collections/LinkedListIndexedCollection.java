package hr.fer.oprpp1.custom.collections;

import java.util.Objects;

/**
 * Implementacija uređene kolekcije objekata pomoću dvostruko povezane liste.
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
public class LinkedListIndexedCollection extends Collection {
	
	/**
	 * Pomoćni razred koji predstavlja čvor dvostruko povezane liste.
	 *
	 */
	private static class ListNode {
		/**
		 * Referenca na sljedeći čvor (desni susjed).
		 */
		ListNode next;		// nadalje nema potrebe za privatnim modifikatorima jer je razred privatan
		/**
		 * Referenca na prethodni čvor (lijevi susjed).
		 */
		ListNode prev;
		/**
		 * Vrijednost, odnosno podatak koju pohranjuje taj čvor.
		 */
		Object value;
		
		ListNode(ListNode next, ListNode prev, Object value) {
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
		ListNode getNeighbor(int direction) {
			return direction == ITERATION_FROM_HEAD ? next : prev;
		}
	}
	
	/**
	 * Referenca na prvi čvor (glava) u listi. 
	 * Njegov prethodnik je {@code null}.
	 */
	private ListNode first;
	/**
	 * Referenca na zadnji čvor (rep) u listi.
	 * Njegov sljedbenik je {@code null}.
	 */
	private ListNode last;
	/**
	 * Broj pohranjenih elemenata.
	 */
	private int size = 0;
	
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
	public LinkedListIndexedCollection(Collection col) {
		addAll(col);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @throws NullPointerException ako je predani objekt {@code null}.
	 */
	@Override
	public void add(Object value) {
		Objects.requireNonNull(value, "Predani element je null");
		addLast(value);
	}
	
	/**
	 * Pomoćna metoda koja dodaje predani objekt {@code value} na kraj liste.
	 * 
	 * @param value objekt koji se dodaje na kraj liste.
	 */
	private void addLast(Object value) {
		ListNode newListNode = new ListNode(null, last, value);
		if (isEmpty()) {
			first = last = newListNode;
		} else {
			last.next = newListNode;
			last = newListNode;
		}
		size++;
	}
	
	/**
	 * Pomoćna metoda koja uklanja zadnji element liste.
	 */
	private void removeLast() {
		if (size < 1) {
			first.value = null;
			first = last = null;
		} else {
			ListNode toRemove = last;
			last = last.prev;
			
			last.next = null;
			toRemove.prev = null;
			toRemove.value = null;
		}
		size--;
	}
	
	/**
	 * Pomoćna metoda koja uklanja prvi element liste.
	 */
	private void removeFirst() {
		if (size < 1) {
			first.value = null;
			first = last = null;
		} else {
			ListNode toRemove = first;
			first = first.next;
			
			first.prev = null;
			toRemove.next = null;
			toRemove.value = null;
		}
		size--;
	}
	
	/**
	 * Pomoćna metoda koja dodaje predani objekt {@code value} na početak liste.
	 * 
	 * @param value objekt koji se dodaje na početak liste.
	 */
	private void addFirst(Object value) {
		ListNode newListNode = new ListNode(first, null, value);
		if (isEmpty()) {
			first = last = newListNode;
		} else {
			first.prev = newListNode;
			first = newListNode;
		}
		size++;
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
	public Object get(int index) {
		Objects.checkIndex(index, size);
		
		ListNode node = getNode(index);
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
	private ListNode getNode(int index) {
		int searchDirection = iterationDirection(index);
		ListNode currentNode = searchDirection == ITERATION_FROM_HEAD ? first : last;
		
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
	private ListNode getNode(Object value) {
		for (ListNode currentNode = first; currentNode != null; currentNode = currentNode.next) {
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
		ListNode rightPosListNode = getNode(position);
		ListNode leftPosListNode = rightPosListNode.prev;
		
		ListNode newListNode = new ListNode(rightPosListNode, leftPosListNode, value);
		
		leftPosListNode.next = newListNode;
		rightPosListNode.prev = newListNode;
		
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
		ListNode currentNode = first;
		
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
	public void remove(int index) {
		Objects.checkIndex(index, size);
		
		removeNode(getNode(index));
	}
	
	/**
	 * Pomoćna metoda koja uklanja predani čvor iz liste.
	 * 
	 * @param čvor koji se uklanja.
	 */
	private void removeNode(ListNode node) {
		if (node.next == null) {
			removeLast();
			return;
		}
		if (node.prev == null) {
			removeFirst();
			return;
		}
		
		ListNode left = node.prev;
		ListNode right = node.next;
		
		node.value = node.next = node.prev = null;
		left.next = right;
		right.prev = left;
		size--;
	}
	
	@Override
	public boolean remove(Object value) {
		if (value == null)
			return false;
		
		ListNode valueNode = getNode(value);
		if (valueNode != null) {
			removeNode(valueNode);
			return true;
		} else
			return false;
	}
	
	@Override
	public Object[] toArray() {
		Object[] array = new Object[size];
		
		ListNode currentNode = first;
		for (int i=0; i<size; i++) {
			array[i] = currentNode.value;
			currentNode = currentNode.next;
		}
		return array;
	}
	
	@Override
	public void forEach(Processor processor) {
		Objects.requireNonNull(processor, "Predani procesor je null.");

		for (ListNode currentNode = first; currentNode != null; currentNode = currentNode.next) {
			processor.process(currentNode.value);
		}
	}
	
}
