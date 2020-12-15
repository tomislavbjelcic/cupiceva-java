package hr.fer.zemris.java.fractals;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.math.Complex;
import hr.fer.zemris.math.ComplexRootedPolynomial;

/**
 * Program koji od korisnika traži unos nultočaka kompleksnog polinoma i nakon toga iskoristi taj polinom za 
 * crtanje Newtonovog fraktala.
 * 
 * @author Tomislav Bjelčić
 *
 */
public class Newton {
	
	/**
	 * Poruka koja se ispisuje prilikom pokretanja programa.
	 */
	private static final String GREETING_MESSAGE 
		= "Welcome to Newton-Raphson iteration-based fractal viewer.";
	/**
	 * Poruka koja se ispisuje korisniku da krene unositi nultočke polinoma.
	 */
	private static final String ROOT_INPUT_MESSAGE 
		= "Please enter at least two roots, one root per line. Enter 'done' when done.";
	/**
	 * Što korisnik mora unijeti da bi označio kraj unosa nultočaka.
	 */
	private static final String DONE = "done";
	/**
	 * Poruka ispisa neposredno prije prikaza fraktala.
	 */
	private static final String INPUT_ENTERED_MESSAGE
		= "Image of fractal will appear shortly. Thank you.";
	/**
	 * Minimalan broj nultočaka koje korisnik mora unijeti.
	 */
	private static final int MIN_ROOT_COUNT = 2;
	
	/**
	 * Zahtjeva unos nultočaka od korisnika i na temelju njih konstruira polinom (u faktoriziranom obliku) 
	 * sa takvim nultočkama i vodećim koeficijentom 1, te takav polinom vraća pozivatelju.
	 * 
	 * @return kompleksni polinom sa nultočkama koje je unio korisnik i vodećim koeficijentom 1.
	 */
	static ComplexRootedPolynomial getPolynomialFromInput() {
		System.out.println(GREETING_MESSAGE);
		System.out.println(ROOT_INPUT_MESSAGE);
		
		int rootsEntered = 0;
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		List<Complex> roots = new ArrayList<>();
		for (;;) {
			System.out.print("Root " + (rootsEntered+1) + "> ");
			String line = sc.nextLine();
			
			if (line.strip().toLowerCase().equals(DONE)) {
				if (rootsEntered < MIN_ROOT_COUNT) {
					System.out.println("At least two roots have to be entered!");
					continue;
				} else break;
			}
			
			try {
				Complex root = Complex.parse(line);
				roots.add(root);
			} catch (NumberFormatException ex) {
				System.out.println(ex.getMessage());
				continue;
			}
			
			rootsEntered++;
		}
		
		Complex[] rootsArray = roots.toArray(Complex[]::new);
		ComplexRootedPolynomial crp = new ComplexRootedPolynomial(Complex.ONE, rootsArray);
		System.out.println(INPUT_ENTERED_MESSAGE);
		return crp;
	}
	
	/**
	 * Glavni program koji, nakon što je dohvaćen polinom koji se koristi za prikaz fraktala, 
	 * obavlja poziv prikaznika fraktala.<br>
	 * Prikaznik fraktala će pri tome koristiti implementaciju sučelja {@link IFractalProducer} koja koristi 
	 * jednu dretvu.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		ComplexRootedPolynomial crp = getPolynomialFromInput();
		IFractalProducer producer = new SequentialFractalProducer(crp);
		FractalViewer.show(producer);
	}
	
}
