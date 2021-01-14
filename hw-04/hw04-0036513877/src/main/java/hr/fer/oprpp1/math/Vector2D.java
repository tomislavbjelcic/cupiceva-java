package hr.fer.oprpp1.math;

import java.util.Objects;

/**
 * Razred predstavlja vektor u prostoru <i>R</i><sup>2</sup>.
 * 
 * @author Tomislav Bjelčić
 *
 */
public class Vector2D {
	
	/**
	 * Prva komponenta ovog vektora.
	 */
	private double x;
	/**
	 * Druga komponenta ovog vektora.
	 */
	private double y;
	
	/**
	 * Stvara novi dvodimenzionalni vektor sa komponentama {@code (x, y)}.
	 * 
	 * @param x
	 * @param y
	 */
	public Vector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Stvara i vraća kopiju ovog vektora.
	 * 
	 * @return kopija ovog vektora.
	 */
	public Vector2D copy() {
		return new Vector2D(x, y);
	}

	/**
	 * Dohvaća prvu realnu komponentu ovog vektora.
	 * 
	 * @return prva komponenta ovog vektora.
	 */
	public double getX() {
		return x;
	}
	
	/**
	 * Dohvaća drugu realnu komponentu ovog vektora.
	 * 
	 * @return druga komponenta ovog vektora.
	 */
	public double getY() {
		return y;
	}
	
	/**
	 * Obavlja operaciju zbrajanja vektora {@code offset} nad ovim vektorom.
	 * 
	 * @param offset vektor s kojim se zbraja ovaj vektor.
	 * @throws NullPointerException ako je predani vektor {@code null}.
	 */
	public void add(Vector2D offset) {
		Objects.requireNonNull(offset, "Predani vektor je null.");
		x += offset.x;
		y += offset.y;
	}
	
	/**
	 * Stvara i vraća novi vektor koji je rezultat zbrajanja ovog vektora i 
	 * {@code offset}.
	 * 
	 * @param offset vektor s kojim se zbraja ovaj vektor.
	 * @return novi vektor koji je rezultat zbrajanja.
	 * @throws NullPointerException ako je predani vektor {@code null}.
	 */
	public Vector2D added(Vector2D offset) {
		Vector2D vec = copy();
		vec.add(offset);
		return vec;
	}
	
	/**
	 * Obavlja rotaciju ovog vektora za kut {@code angle} u radijanima u smjeru 
	 * suprotnom od kazaljke na satu.
	 * 
	 * @param angle kut rotacije u radijanima.
	 */
	public void rotate(double angle) {
		double sine = Math.sin(angle);
		double cosine = Math.cos(angle);
		
		double newX = x*cosine - y*sine;
		double newY = x*sine + y*cosine;
		
		x = newX;
		y = newY;
	}
	
	/**
	 * Stvara i vraća novi vektor koji je rezultat rotacije ovog vektora za {@code angle} u radijanima u smjeru 
	 * suprotnom od kazaljke na satu.
	 * 
	 * @param angle kut rotacije u radijanima.
	 * @return novi vektor koji je rezultat rotacije.
	 */
	public Vector2D rotated(double angle) {
		Vector2D vec = copy();
		vec.rotate(angle);
		return vec;
	}
	
	/**
	 * Množi ovaj vektor sa skalarom {@code scaler}.
	 * 
	 * @param scaler skalar s kojim se množi ovaj vektor.
	 */
	public void scale(double scaler) {
		x *= scaler;
		y *= scaler;
	}
	
	/**
	 * Stvara i vraća novi vektor koji je jednak ovom vektoru pomnoženom sa skalarom 
	 * {@code scaler}.
	 * 
	 * @param scaler skalar s kojim se množi ovaj vektor.
	 * @return novi vektor jednak ovom vektoru pomnoženom sa skalarom {@code scaler}.
	 */
	public Vector2D scaled(double scaler) {
		Vector2D vec = copy();
		vec.scale(scaler);
		return vec;
	}
}
