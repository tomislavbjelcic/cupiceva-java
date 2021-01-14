package hr.fer.zemris.lsystems.impl;

import hr.fer.oprpp1.custom.collections.ObjectStack;

/**
 * Razred predstavlja kontekst - strukturu podataka koja se koristi prilikom crtanja 
 * fraktala.<br>
 * Kontekst koristi stog na kojem se spremaju stanja kornjače. Posljedično, 
 * ovaj razred nudi metode sa stavljanje i uklanjanje stanja sa stoga, i dohvat 
 * trenutnog stanja s vrha stoga.
 * 
 * @author Tomislav Bjelčić
 *
 */
public class Context {
	
	/**
	 * Stog na kojem se spremaju stanja kornjače.
	 */
	private ObjectStack<TurtleState> states = new ObjectStack<>();
	
	/**
	 * Vraća trenutno stanje kornjače s vrha stoga.
	 * 
	 * @return trenutno stanje kornjače.
	 * 
	 * @throws EmptyStackException ako je stog bio prazan.
	 */
	public TurtleState getCurrentState() {
		return states.peek();
	}
	
	/**
	 * Stavlja stanje kornjače {@code state} na vrh stoga ovog konteksta.
	 * 
	 * @param state stanje kornjače koje se stavlja na vrh stoga.
	 * 
	 * @throws NullPointerException ako je predano stanje {@code null}.
	 */
	public void pushState(TurtleState state) {
		states.push(state);
	}
	
	/**
	 * Uklanja trenutno stanje sa vrha stoga.
	 * 
	 * @throws EmptyStackException ako je stog bio prazan.
	 */
	public void popState() {
		states.pop();
	}
	
}
