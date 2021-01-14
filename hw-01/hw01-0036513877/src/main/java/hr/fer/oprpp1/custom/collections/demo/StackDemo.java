package hr.fer.oprpp1.custom.collections.demo;

import hr.fer.oprpp1.custom.collections.demo.PostfixExpressionEvaluator.InvalidExpressionException;

/**
 * Razred programa koji demonstrira računanje postfix izraza.
 * 
 * @author Tomislav Bjelčić
 *
 */
public class StackDemo {
	
	
	/**
	 * Program koji iz konzole (komandna linija, terminal) prima String koji predstavlja 
	 * postfix izraz koji se u programu mora izračunati. Ukoliko prilikom računanja 
	 * dođe do pogreške, poruka pogreške se ispisuje na tok za pogreške.
	 * 
	 * @param args polje koje mora sadržavati samo jedan String.
	 * @throws IllegalArgumentException ako polje argumenata ima više od 1 Stringa.
	 */
	public static void main(String[] args) {
		if (args.length != 1)
			throw new IllegalArgumentException("Neispravan unos argumenata glavnog programa, očekuje se 1 String,"
												+ " a uneseno ih je " + args.length);
		
		String expr = args[0];
		PostfixExpressionEvaluator evaluator = new PostfixExpressionEvaluator();
		
		System.out.println("Argumenti programa uspješno uneseni!\n");
		System.out.println("Evaluiram postfix izraz: \"" + expr + "\" ...\n");
		
		try {
			int result = evaluator.evaluate(expr);
			System.out.println("Evaluacija izraza uspješna!");
			System.out.println("Rezultat: " + result);
		} catch (ArithmeticException
				| InvalidExpressionException
				| IllegalArgumentException ex) {
			String msg = "Greška prilikom evaluacije postfix izraza \"" + expr + "\""
						+ "\nPoruka pogreške:\n\t" + ex.getMessage();
			System.err.println(msg);
		}
		
		
		
	}
	
	
}
