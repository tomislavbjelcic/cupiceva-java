package hr.fer.zemris.java.fractals;

import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.math.ComplexPolynomial;
import hr.fer.zemris.math.ComplexRootedPolynomial;

/**
 * Apstraktni nadrazred svih implementacija sučelja {@link IFractalProducer} koje 
 * generiraju podatke za vizualizaciju fraktala koristeći kompleksne polinome.
 * 
 * @author Tomislav Bjelčić
 *
 */
public abstract class AbstractFractalProducer implements IFractalProducer {
	
	/**
	 * Polinom u faktoriziranom obliku.
	 */
	protected ComplexRootedPolynomial functionRooted;
	/**
	 * Polinom u obliku sume i potencija.
	 */
	protected ComplexPolynomial function;
	/**
	 * Prva derivacija polinoma.
	 */
	protected ComplexPolynomial derivative;
	
	/**
	 * Konstruktor namijenjen podrazredima koji prima polinom u faktoriziranom obliku koji će 
	 * se koristiti za generiranje podataka o fraktalu. Polinom se zatim
	 * pretvara u polinom u obliku sume i potencija te se derivira.
	 * 
	 * @param functionRooted polinom u faktoriziranom obliku.
	 */
	protected AbstractFractalProducer(ComplexRootedPolynomial functionRooted) {
		this.functionRooted = functionRooted;
		function = functionRooted.toComplexPolynom();
		derivative = function.derive();
	}
	
}
