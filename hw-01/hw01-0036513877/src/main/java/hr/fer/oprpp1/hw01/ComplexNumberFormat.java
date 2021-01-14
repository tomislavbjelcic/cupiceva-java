package hr.fer.oprpp1.hw01;

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
	private static final String ERROR_MESSAGE = "Neispravan format kompleksnog broja: ";
	/**
	 * Skup (polje) regularnih izraza koji predstavljaju sve ispravne formate zapisa 
	 * kompleksnog broja. Koriste se prilikom provjere je li dani String kompleksnog 
	 * broja ispravan.
	 */
	private static final String[] FORMATS_REGEX = {
		"[-\\+]?\\d+\\.?\\d*",						// čisti realan broj
		"[-\\+]?\\d+\\.?\\d*i",						// čisti imaginaran broj različit od i, -i
		"[-\\+]?i",									// +i, -i ili samo i
		"[-\\+]?\\d+\\.?\\d*[-\\+]\\d+\\.?\\d*i",		// općeniti kompleksan broj sa Re(z) != 0, Im(z) != 1 i -1
		"[-\\+]?\\d+\\.?\\d*[-\\+]i"					// općeniti kompleksan broj sa Re(z) != 0, Im(z) == 1 ili -1
	};
	
	/**
	 * Ne dopušta se stvaranje instanci ove klase jer su sva polja i metode
	 * statičkog tipa pa onda stvaranje novih instanci nema smisla.
	 */
	private ComplexNumberFormat() {}
	
	/**
	 * Parsira predani String kompleksnog broja i vraća novi stvoreni primjerak 
	 * kompleksnog broja. Ovoj metodi se delegira zadaća metode 
	 * {@link ComplexNumber#parse(String)}, pogledajte njezinu dokumentaciju 
	 * za više informacija o metodi.
	 * 
	 * @param s String kompleksnog broja.
	 * @return parsirani kompleksni broj.
	 * @throws NullPointerException ako se kao String preda {@code null} referenca.
	 * @throws NumberFormatException ako format zapisa kompleksnog broja nije ispravan.
	 */
	public static ComplexNumber parse(String s) {
		Objects.requireNonNull(s, "Predani string je null.");
		
		s = s.strip();
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
	private static ComplexNumber formatComplex(String s, int type) {
		ComplexNumber result = switch(type) {
			case 0 -> ComplexNumber.fromReal(Double.parseDouble(s));
			case 1 -> {
				String imStr = s.replace("i", "");	// makni "i" sa kraja
				double im = Double.parseDouble(imStr);
				ComplexNumber complex = ComplexNumber.fromImaginary(im);
				yield complex;
			}
			case 2 -> {
				String imStr = s.replace("i", "1");
				double im = Double.parseDouble(imStr);
				yield ComplexNumber.fromImaginary(im);
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
				double re = Double.parseDouble(s.substring(0, index));
				double im = Double.parseDouble(s.substring(index).replace("i", ""));
				yield new ComplexNumber(re, im);
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
				double re = Double.parseDouble(s.substring(0, index));
				double im = Double.parseDouble(s.substring(index).replace("i", "1"));
				yield new ComplexNumber(re, im);
			}
			default -> null;
		};
		return result;
	}
	
}
