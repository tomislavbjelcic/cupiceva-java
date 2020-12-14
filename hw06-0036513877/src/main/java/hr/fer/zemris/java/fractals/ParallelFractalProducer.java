package hr.fer.zemris.java.fractals;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
import hr.fer.zemris.math.ComplexPolynomial;
import hr.fer.zemris.math.ComplexRootedPolynomial;
import static hr.fer.zemris.java.fractals.FractalConstants.*;

/**
 * Implementacija sučelja {@link IFractalProducer} koji koristi kompleksne polinome za 
 * generiranje podataka o fraktalu.<br>
 * Pri tome se sam zadatak generiranja podataka može podijeliti na proizvoljno mnogo 
 * dijelova (poslova) koje će obavljati proizvoljno mnogo dretvi.
 * 
 * @author Tomislav Bjelčić
 *
 */
public class ParallelFractalProducer extends AbstractFractalProducer {
	
	/**
	 * Predstavlja model posla koji obavlja izračun podataka potrebnih za vizualizaciju 
	 * fraktala.
	 * 
	 * @author Tomislav Bjelčić
	 *
	 */
	private static class Job implements Runnable {
		
		double reMin;
		double reMax;
		double imMin;
		double imMax;
		double rootTreshold;
		double convergenceTreshold;
		ComplexPolynomial function;
		ComplexRootedPolynomial functionRooted;
		ComplexPolynomial derivative;
		int width;
		int height;
		int maxIter;
		int ymin;
		int ymax;
		short[] data;
		AtomicBoolean cancel;
		
		Job(double reMin, double reMax, double imMin, double imMax, double rootTreshold, double convergenceTreshold,
				ComplexPolynomial function, ComplexRootedPolynomial functionRooted, ComplexPolynomial derivative,
				int width, int height, int maxIter, int ymin, int ymax, short[] data, AtomicBoolean cancel) {
			super();
			this.reMin = reMin;
			this.reMax = reMax;
			this.imMin = imMin;
			this.imMax = imMax;
			this.rootTreshold = rootTreshold;
			this.convergenceTreshold = convergenceTreshold;
			this.function = function;
			this.functionRooted = functionRooted;
			this.derivative = derivative;
			this.width = width;
			this.height = height;
			this.maxIter = maxIter;
			this.ymin = ymin;
			this.ymax = ymax;
			this.data = data;
			this.cancel = cancel;
		}
		
		@Override
		public void run() {
			NewtonRaphson.calculate​(reMin, reMax, imMin, imMax, rootTreshold, 
					convergenceTreshold, function, functionRooted, derivative, 
					width, height, maxIter, ymin, ymax, data, cancel);
		}
		
	}
	
	/**
	 * Broj dretvi koje obavljaju posao izračuna podataka.
	 */
	private int workers;
	/**
	 * Broj poslova (u smislu prikaza fraktala na rasteru: broj horizontalnih traka) 
	 * koje obavljaju dretve kojih ima {@code workers}.
	 */
	private int tracks;
	
	/**
	 * Stvara novi generator podataka o fraktalu koristeći polinom {@code functionRooted}, 
	 * podjelu na {@code tracks} poslova i {@code workers} dretvi.
	 * 
	 * @param functionRooted polinom u faktoriziranom obliku.
	 * @param tracks broj poslova.
	 * @param workers broj dretvi.
	 */
	public ParallelFractalProducer(int workers, int tracks, ComplexRootedPolynomial crp) {
		super(crp);
		this.workers = workers;
		this.tracks = tracks;
	}
	
	/**
	 * Generira podatke kao polje short brojeva koje se koriste za vizualizaciju 
	 * fraktala i generirani rezultat šalje promatraču {@code observer}.<br>
	 * Prilikom izračuna generiranih podataka koristi se onoliko mnogo dretvi koliko 
	 * je specificirano prilikom stvaranja instance ovog razreda, i posao generiranja 
	 * se isto tako dijeli na onoliko poslova koliko je specificirano.
	 */
	@Override
	public void produce(double reMin, double reMax, double imMin, double imMax,
			int width, int height, 
			long requestNo, IFractalResultObserver observer, 
			AtomicBoolean cancel) {
		
		short[] data = new short[width * height];
		int jobCount = tracks > height ? height : tracks;
		int rowCountPerJob = height / jobCount;
		
		BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();
		Runnable doNothing = () -> {};
		Runnable workerRunnable = () -> {
			while(true) {
				Runnable r = null;
				try {
					r = queue.take();
					if(r == doNothing)
						break;
				} catch (InterruptedException e) {
					continue;
				}
				r.run();
			}
		};
		Thread[] workersArray = new Thread[workers];
		
		// inicijaliziraj dretve radnika
		for(int i = 0; i < workersArray.length; i++) {
			workersArray[i] = new Thread(workerRunnable);
		}
		
		// pokreni dretve radnika koje će se zaglaviti na blokirajućem redu poslova jer je prazan
		for(int i = 0; i < workersArray.length; i++) {
			workersArray[i].start();
		}
		
		// napuni red korisnim poslovima
		for(int i = 0; i < jobCount; i++) {
			int ymin = i * rowCountPerJob;
			int ymax = i == jobCount-1 ? height - 1 : (i+1)*rowCountPerJob - 1;
			
			Job job = new Job(reMin, reMax, imMin, imMax, ROOT_TRESHOLD, 
					CONVERGENCE_TRESHOLD, function, functionRooted, derivative, 
					width, height, MAX_ITERATIONS, ymin, ymax, data, cancel);
			while(true) {
				try {
					queue.put(job);
					break;
				} catch (InterruptedException e) {}
			}
		}
		
		// na kraj reda stavi prazne poslove
		for(int i = 0; i < workersArray.length; i++) {
			while(true) {
				try {
					queue.put(doNothing);
					break;
				} catch (InterruptedException e) {}
			}
		}
		
		// sačekaj sve dretve radnika da završe
		for(int i = 0; i < workersArray.length; i++) {
			while(true) {
				try {
					workersArray[i].join();
					break;
				} catch (InterruptedException e) {}
			}
		}
		
		
		observer.acceptResult(data, (short)(function.order() + 1), requestNo);
	}

}
