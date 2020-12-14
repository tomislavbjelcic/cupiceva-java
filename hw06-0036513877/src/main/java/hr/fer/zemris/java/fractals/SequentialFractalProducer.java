package hr.fer.zemris.java.fractals;

import java.util.concurrent.atomic.AtomicBoolean;

import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
import hr.fer.zemris.math.ComplexRootedPolynomial;
import static hr.fer.zemris.java.fractals.FractalConstants.*;

/**
 * Implementacija sučelja {@link IFractalProducer} koji koristi kompleksne polinome za 
 * generiranje podataka o fraktalu i pri tome koristi jednu dretvu.
 * 
 * @author Tomislav Bjelčić
 *
 */
public class SequentialFractalProducer extends AbstractFractalProducer {
	
	/**
	 * Stvara novi generator podataka o fraktalu koristeći polinom {@code functionRooted}.
	 * 
	 * @param functionRooted polinom u faktoriziranom obliku.
	 */
	protected SequentialFractalProducer(ComplexRootedPolynomial functionRooted) {
		super(functionRooted);
	}

	/**
	 * Generira podatke kao polje short brojeva koje se koriste za vizualizaciju 
	 * fraktala i generirani rezultat šalje promatraču {@code observer}.<br>
	 * Prilikom izračuna generiranih podataka koristi se samo jedna dretva.
	 */
	@Override
	public void produce(double reMin, double reMax, double imMin, double imMax,
			int width, int height, 
			long requestNo, IFractalResultObserver observer, 
			AtomicBoolean cancel) {
		
		short[] data = new short[width * height];
		int ymin = 0;
		int ymax = height - 1;
		
		NewtonRaphson.calculate​(reMin, reMax, imMin, imMax, ROOT_TRESHOLD, 
				CONVERGENCE_TRESHOLD, function, functionRooted, derivative, 
				width, height, MAX_ITERATIONS, ymin, ymax, data, cancel);
		
		observer.acceptResult(data, (short)(function.order() + 1), requestNo);
	}
	
}
