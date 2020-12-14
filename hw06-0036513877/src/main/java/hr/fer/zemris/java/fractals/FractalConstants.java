package hr.fer.zemris.java.fractals;

/**
 * Razred koji sadrži neke predefinirane vrijednosti koje se koriste za 
 * računanje podataka za vizualizaciju fraktala.
 * 
 * @author Tomislav Bjelčić
 *
 */
public class FractalConstants {
	
	private FractalConstants() {}
	
	public static final int MAX_ITERATIONS = 1 << 12;
	public static final double CONVERGENCE_TRESHOLD = 0.001;
	public static final double ROOT_TRESHOLD = 0.002;
	
}
