package hr.fer.zemris.java.fractals;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.math.ComplexRootedPolynomial;

/**
 * Program koji od korisnika traži unos nultočaka kompleksnog polinoma i nakon toga iskoristi taj polinom za 
 * crtanje Newtonovog fraktala.<br>
 * Samo prikazivanje Newtonovog fraktala je paralelizirano, odnosno može se preko argumenata glavnog 
 * 
 * @author Tomislav Bjelčić
 *
 */
public class NewtonParallel {
	
	private static class Args {
		static final int WORKERS_DEFAULT
			= Runtime.getRuntime().availableProcessors();
		static final int TRACKS_DEFAULT = 4 * WORKERS_DEFAULT;
		
		static final String WORKERS_ARG = "--workers";
		static final String WORKERS_ARG_SHORT = "-w";
		static final String TRACKS_ARG = "--tracks";
		static final String TRACKS_ARG_SHORT = "-t";
		
		int workers = WORKERS_DEFAULT;
		int tracks = TRACKS_DEFAULT;
		
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
	
	public static void main(String[] args) {
		Args arguments = null;
		try {
			arguments = Args.parse(args);
		} catch (IllegalArgumentException ex) {
			System.out.println(ex.getMessage());
			return;
		}
		
		ComplexRootedPolynomial crp = Newton.getPolynomialFromInput();
		IFractalProducer producer = new ParallelFractalProducer(arguments.workers, arguments.tracks, crp);
		FractalViewer.show(producer);
	}
	
	
}
