package hr.fer.zemris.math;

import java.util.Objects;

/**
 * Pomoćni razred koji definira dopuštene formate zapisa kompleksnih brojeva kao String te 
 * sadrži statičku metodu za stvaranje kompleksnog broja iz Stringa.
 * 
 * @author Tomislav Bjelčić
 *
 */
public class ComplexNumberFormat {
	

	/**
	 * Poruka pogreške koja se predaje iznimci {@code NumberFormatException} ukoliko 
	 * format zapisa kompleksnog broja kao String nije ispravna.
	 */
	private static final String ERROR_MESSAGE = "Invalid complex number format: ";
	/**
	 * Skup (polje) regularnih izraza koji predstavljaju sve ispravne formate zapisa 
	 * kompleksnog broja. Koriste se prilikom provjere je li dani String kompleksnog 
	 * broja ispravan.
	 */
	private static final String[] FORMATS_REGEX = {
		"\\s*[-\\+]?\\s*\\d+\\.?\\d*\\s*",						// čisti realan broj
		"\\s*[-\\+]?\\s*i\\d+\\.?\\d*\\s*",						// čisti imaginaran broj različit od i, -i
		"\\s*[-\\+]?\\s*i\\s*",									// +i, -i ili samo i
		"\\s*[-\\+]?\\s*\\d+\\.?\\d*\\s*[-\\+]\\s*i\\d+\\.?\\d*\\s*",		// općeniti kompleksan broj sa Re(z) != 0, Im(z) != 1 i -1
		"\\s*[-\\+]?\\s*\\d+\\.?\\d*\\s*[-\\+]\\s*i\\s*"					// općeniti kompleksan broj sa Re(z) != 0, Im(z) == 1 ili -1
	};
	
	/**
	 * Ne dopušta se stvaranje instanci ove klase jer su sva polja i metode
	 * statičkog tipa pa onda stvaranje novih instanci nema smisla.
	 */
	private ComplexNumberFormat() {}
	
	/**
	 * Parsira predani String kompleksnog broja i vraća novi stvoreni primjerak 
	 * kompleksnog broja. Ovoj metodi se delegira zadaća metode 
	 * {@link Complex#parse(String)}.
	 * 
	 * @param s String kompleksnog broja.
	 * @return parsirani kompleksni broj.
	 * @throws NullPointerException ako se kao String preda {@code null} referenca.
	 * @throws NumberFormatException ako format zapisa kompleksnog broja nije ispravan.
	 */
	public static Complex parse(String s) {
		Objects.requireNonNull(s, "Predani string je null.");
		
		if (s.isBlank())
			throw new NumberFormatException(ERROR_MESSAGE + "empty String.");
		int formatIndex = whichFormat(s);
		if (formatIndex == -1)
			throw new NumberFormatException(ERROR_MESSAGE + s);
		
		return formatComplex(s, formatIndex);
	}
	
	/**
	 * Pomoćna metoda koja provjerava postoji li ispravan format kompleksnog broja koji se 
	 * podudara sa predanim zapisom {@code s}. Ako postoji, metoda vraća poziciju 
	 * u polju regularnih izraza koji odgovara zapisu {@code s}, inače vraća -1.
	 * 
	 * @param s String zapis kompleksnog broja.
	 * @return poziciju formata ako postoji, a -1 ako ne postoji.
	 */
	private static int whichFormat(String s) {
		for (int i=0, len=FORMATS_REGEX.length; i<len; i++) {
			String format = FORMATS_REGEX[i];
			if (s.matches(format))
				return i;
		}
		return -1;
	}
	
	/**
	 * Pomoćna metoda koja stvara novi kompleksni broj na temelju zapisa kompleksnog 
	 * broja {@code s} koji je u formatu {@link ComplexNumberFormat#FORMATS_REGEX}[type].
	 * 
	 * @param s zapis kompleksnog broja.
	 * @param type pozicija u skupu ispravnih formata kompleksnih brojeva.
	 * @return novi kompleksni broj predstavljen zapisom {@code s}.
	 */
	private static Complex formatComplex(String s, int type) {
		String regex = "\\s*";
		String empty = "";
		Complex result = switch(type) {
			case 0 -> new Complex(Double.parseDouble(s.replaceAll(regex, empty)), 0.0);
			case 1 -> {
				String imStr = s.replace("i", "").replaceAll(regex, empty);	// makni "i"
				double im = Double.parseDouble(imStr);
				Complex complex = new Complex(0.0, im);
				yield complex;
			}
			case 2 -> {
				String imStr = s.replace("i", "1").replaceAll(regex, empty);
				double im = Double.parseDouble(imStr);
				Complex complex = new Complex(0.0, im);
				yield complex;
			}
			case 3 -> {
				int index = -1;
				for (int len=s.length(), i=len-1; i>=0; i--) {
					char c = s.charAt(i);
					if (c == '+' || c == '-') {	// takav char mora postojati jer se radi o takvom formatu
						index = i;
						break;
					}
				}
				double re = Double.parseDouble(s.substring(0, index).replaceAll(regex, empty));
				double im = Double.parseDouble(s.substring(index).replace("i", "").replaceAll(regex, empty));
				Complex complex = new Complex(re, im);
				yield complex;
			}
			case 4 -> {
				int index = -1;
				for (int len=s.length(), i=len-1; i>=0; i--) {
					char c = s.charAt(i);
					if (c == '+' || c == '-') {
						index = i;
						break;
					}
				}
				double re = Double.parseDouble(s.substring(0, index).replaceAll(regex, empty));
				double im = Double.parseDouble(s.substring(index).replace("i", "1").replaceAll(regex, empty));
				Complex complex = new Complex(re, im);
				yield complex;
			}
			default -> null;
		};
		return result;
	}
	
}
