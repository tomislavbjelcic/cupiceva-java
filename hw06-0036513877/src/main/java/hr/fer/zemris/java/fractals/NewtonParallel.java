package hr.fer.zemris.java.fractals;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.math.ComplexRootedPolynomial;

/**
 * Program koji od korisnika traži unos nultočaka kompleksnog polinoma i nakon toga iskoristi taj polinom za 
 * crtanje Newtonovog fraktala.<br>
 * Sam izračun Newtonove iteracije za prikaz fraktala je paralelizirano. 
 * Može se preko argumenata glavnog programa specificirati koliko dretvi će sudjelovati u 
 * izračunu i na koliko manjih poslova će rastaviti cjelokupni posao. 
 * 
 * @author Tomislav Bjelčić
 *
 */
public class NewtonParallel {
	
	/**
	 * Pomoćni razred koji predstavlja vrijednosti argumenata glavnog programa.
	 * 
	 * @author Tomislav Bjelčić
	 *
	 */
	private static class Args {
		/**
		 * Pretpostavljeni broj dretvi.
		 */
		static final int WORKERS_DEFAULT
			= Runtime.getRuntime().availableProcessors();
		/**
		 * Pretpostavljeni broj poslova.
		 */
		static final int TRACKS_DEFAULT = 4 * WORKERS_DEFAULT;
		
		/**
		 * Dulji zapis argumenta za broj drevi.
		 */
		static final String WORKERS_ARG = "--workers";
		/**
		 * Kraći zapis argumenta za broj dretvi.
		 */
		static final String WORKERS_ARG_SHORT = "-w";
		/**
		 * Dulji zapis argumenta za broj poslova.
		 */
		static final String TRACKS_ARG = "--tracks";
		/**
		 * Kraći zapis argumenta za broj poslova.
		 */
		static final String TRACKS_ARG_SHORT = "-t";
		
		/**
		 * Broj dretvi.
		 */
		int workers = WORKERS_DEFAULT;
		/**
		 * Broj poslova.
		 */
		int tracks = TRACKS_DEFAULT;
		
		/**
		 * Preuzima argumente glavnog programa, parsira ih i stvara objekt ovog 
		 * razreda sa postavljenim vrijednostima argumenata.
		 * 
		 * @param args argumenti glavnog programa.
		 * @return instancu objekta ovog razreda sa postavljenim vrijednostima argumenata.
		 * @throws IllegalArgumentException ako predani argumenti nisu ispravni.
		 */
		static Args parse(String[] args) {
			boolean workersSpecified = false;
			boolean tracksSpecified = false;
			char eq = '=';
			Args arguments = new Args();
			
			for(int i=0; i<args.length; i++) {
				String s = args[i];
				if (s.startsWith(WORKERS_ARG)) {
					if (workersSpecified)
						throw new IllegalArgumentException("Number of workers specified more than once.");
					
					int nextIndex = WORKERS_ARG.length();
					if (nextIndex == s.length())
						throw new IllegalArgumentException("Invalid argument input: " + s + ": unspecified number of workers.");
					
					char nextCh = s.charAt(nextIndex);
					if (nextCh != eq)
						throw new IllegalArgumentException("Invalid argument input: " + s + ": correct usage: " + WORKERS_ARG + "=<number_of_workers>");
					
					String rest = s.substring(nextIndex + 1);
					int w = 0;
					try {
						w = Integer.parseInt(rest);
					} catch (NumberFormatException ex) {
						throw new IllegalArgumentException("Invalid argument input: " + s + ": " + rest + " is not an integer.");
					}
					
					if (w<1)
						throw new IllegalArgumentException("Invalid argument input: " + s + ": number of workers has to be greater than 1.");
					
					arguments.workers = w;
					workersSpecified = true;
					continue;
				}
				
				if (s.equals(WORKERS_ARG_SHORT)) {
					if (workersSpecified)
						throw new IllegalArgumentException("Number of workers specified more than once.");
					
					int next = ++i;
					if (next == args.length)
						throw new IllegalArgumentException("Invalid argument input: " + s + ": unspecified number of workers.");
					
					String nextStr = args[next];
					String joined = s + " " + nextStr;
					int w = 0;
					try {
						w = Integer.parseInt(nextStr);
					} catch (NumberFormatException ex) {
						throw new IllegalArgumentException("Invalid argument input: " + joined + ": " + nextStr + " is not an integer.");
					}
					
					if (w<1)
						throw new IllegalArgumentException("Invalid argument input: " + joined + ": number of workers has to be greater than 1.");
					
					arguments.workers = w;
					workersSpecified = true;
					continue;
				}
				
				if (s.startsWith(TRACKS_ARG)) {
					if (tracksSpecified)
						throw new IllegalArgumentException("Number of tracks specified more than once.");
					
					int nextIndex = TRACKS_ARG.length();
					if (nextIndex == s.length())
						throw new IllegalArgumentException("Invalid argument input: " + s + ": unspecified number of tracks.");
					
					char nextCh = s.charAt(nextIndex);
					if (nextCh != eq)
						throw new IllegalArgumentException("Invalid argument input: " + s + ": correct usage: " + TRACKS_ARG + "=<number_of_tracks>");
					
					String rest = s.substring(nextIndex + 1);
					int t = 0;
					try {
						t = Integer.parseInt(rest);
					} catch (NumberFormatException ex) {
						throw new IllegalArgumentException("Invalid argument input: " + s + ": " + rest + " is not an integer.");
					}
					
					if (t<1)
						throw new IllegalArgumentException("Invalid argument input: " + s + ": number of tracks has to be greater than 1.");
					
					arguments.tracks = t;
					tracksSpecified = true;
					continue;
				}
				
				if (s.equals(TRACKS_ARG_SHORT)) {
					if (tracksSpecified)
						throw new IllegalArgumentException("Number of tracks specified more than once.");
					
					int next = ++i;
					if (next == args.length)
						throw new IllegalArgumentException("Invalid argument input: " + s + ": unspecified number of tracks.");
					
					String nextStr = args[next];
					String joined = s + " " + nextStr;
					int t = 0;
					try {
						t = Integer.parseInt(nextStr);
					} catch (NumberFormatException ex) {
						throw new IllegalArgumentException("Invalid argument input: " + joined + ": " + nextStr + " is not an integer.");
					}
					
					if (t<1)
						throw new IllegalArgumentException("Invalid argument input: " + joined + ": number of tracks has to be greater than 1.");
					
					arguments.tracks = t;
					tracksSpecified = true;
					continue;
				}
				
				throw new IllegalArgumentException("Invalid argument input: " + s);
			}
			return arguments;
		}
	}
	
	/**
	 * Glavni program koji, nakon što je dohvaćen polinom koji se koristi za prikaz fraktala, 
	 * obavlja poziv prikaznika fraktala.<br>
	 * Prikaznik fraktala će pri tome koristiti implementaciju sučelja {@link IFractalProducer} koja koristi 
	 * više dretvi i posao izračuna Newtonovih iteracija je podijeljen na više dijela. 
	 * Broj dretvi i broj poslova se mogu specificirati preko argumenata {@code args}.
	 * 
	 * @param args argumenti glavnog programa u kojima se nalazi specifikacija parametara 
	 * izračuna.
	 */
	public static void main(String[] args) {
		Args arguments = null;
		try {
			arguments = Args.parse(args);
		} catch (IllegalArgumentException ex) {
			System.out.println(ex.getMessage());
			return;
		}
		
		int w = arguments.workers;
		int t = arguments.tracks;
		ComplexRootedPolynomial crp = Newton.getPolynomialFromInput();
		IFractalProducer producer = new ParallelFractalProducer(w, t, crp);
		FractalViewer.show(producer);
	}
	
	
}
