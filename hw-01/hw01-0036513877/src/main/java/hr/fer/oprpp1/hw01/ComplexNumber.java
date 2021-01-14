package hr.fer.oprpp1.hw01;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.hypot;
import static java.lang.Math.pow;
import static java.lang.Math.sin;

import java.util.Objects;

/**
 * Razred predstavlja nepromjenjivi kompleksni broj i omogućuje osnovne operacije nad kompleksnim brojevima.
 * Svaki kompleksni broj ima svoj realni i imaginarni dio, ali postoje i metode pomoću kojih se mogu
 * saznati polarne koordinate kompleksnog broja - modul i argument.
 * 
 * @author Tomislav Bjelčić
 *
 */
public class ComplexNumber {
	
	/**
	 * Realni dio kompleksnog broja.
	 */
	private double real;
	/**
	 * Imaginarni dio kompleksnog broja.
	 */
	private double imaginary;
	
	/**
	 * Stvara novi kompleksni broj {@code real + imaginary * i}.
	 * @param real realni dio kompleksnog broja.
	 * @param imaginary imaginarni dio kompleksnog broja.
	 */
	public ComplexNumber(double real, double imaginary) {
		this.real = real;
		this.imaginary = imaginary;
	}
	
	/**
	 * Vraća realni dio ovog kompleksnog broja.
	 * @return realni dio ovog kompleksnog broja.
	 */
	public double getReal() {
		return real;
	}
	
	/**
	 * Vraća imaginarni dio ovog kompleksnog broja.
	 * @return realni dio ovog kompleksnog broja.
	 */
	public double getImaginary() {
		return imaginary;
	}
	
	/**
	 * Računa i vraća modul ovog kompleksnog broja. Kada bi se kompleksni broj {@code z} prikazao 
	 * u Gaussovoj kompleksnoj ravnini, modul bi bio udaljenost točke tog kompleksnog broja 
	 * od ishodišta. Računa se kao |z| = sqrt(x<sup>2</sup> + y<sup>2</sup>), gdje x i y 
	 * predstavljaju realni i imaginarni dio od {@code z}.
	 * @return modul ovog kompleksnog broja.
	 */
	public double getMagnitude() {
		return hypot(real, imaginary);
	}
	
	/**
	 * Računa i vraća argument ovog kompleksnog broja. Kada bi se kompleksni broj {@code z} prikazao
	 * u Gaussovoj kompleksnoj ravnini, argument bi bio kut, u radijanima, između {@code z}, 
	 * ishodišta i pozitivnog smjera osi apscise, krećući od pozitivnog smjera osi apscise pa u smjeru 
	 * suprotnom od kazaljke na satu. Moguće vrijednosti su iz intervala od 0 (uključivo) do 2<i>pi</i>
	 * (isključivo).
	 * 
	 * <p>Primjerice, arg(1+i) = <i>pi</i>/4, arg(2i) = <i>pi</i>/2, ali arg(1-i) = 7<i>pi</i>/4
	 * @return
	 */
	public double getAngle() {
		double ang = atan2(imaginary, real);
		return ang < 0 ? ang + 2*PI : ang;
	}
	
	/**
	 * Stvara novi kompleksni broj koji ima samo realni dio. Takav kompleksan broj je
	 * onda realan i njegov imaginarni dio je jednak 0.
	 * 
	 * @param real realni dio kompleksnog broja.
	 * @return novi kompleksni, realni broj sa realnim dijelom {@code real}.
	 */
	public static ComplexNumber fromReal(double real) {
		return new ComplexNumber(real, 0.0);
	}
	
	/**
	 * Stvara novi kompleksni broj koji ima samo imaginarni dio. Takav kompleksan broj je 
	 * onda čisti imaginarni broj i njegov realni dio je jednak 0.
	 * 
	 * @param imaginary imaginarni dio kompleksnog broja.
	 * @return novi kompleksni, čisti imaginarni broj sa imaginarnim dijelom {@code imaginary}.
	 */
	public static ComplexNumber fromImaginary(double imaginary) {
		return new ComplexNumber(0.0, imaginary);
	}
	
	/**
	 * Stvara novi kompleksni broj sa modulom {@code magnitude} i argumentom 
	 * {@code angle}. Predani argument kompleksnog broja {@code angle} može biti i 
	 * izvan raspona od 0 (uključivo) do 2<i>pi</i>, no modul mora biti veći ili jednak 0 jer to predstavlja udaljenost.
	 * Pomoću njih se lako računaju realni i imaginarni dijelovi koristeći
	 * zapis kompleksnog broja {@code z} u polarnim koordinatama: z = |z| * (cos(arg) + i * sin(arg)),
	 * gdje je |z| modul od {@code z}, a arg argument od {@code z}.
	 * 
	 * @param magnitude modul kompleksnog broja.
	 * @param angle argument kompleksnog broja.
	 * @return novi kompleksni broj sa modulom {@code magnitude} i argumentom {@code angle}.
	 * @throws IllegalArgumentException ako je modul kompleksnog broja negativan.
	 */
	public static ComplexNumber fromMagnitudeAndAngle(double magnitude, double angle) {
		if (magnitude < 0.0)
			throw new IllegalArgumentException("Modul kompleksnog broja ne može biti negativan.");
		
		double real = magnitude * cos(angle);
		double imaginary = magnitude * sin(angle);
		
		return new ComplexNumber(real, imaginary);
	}
	
	/**
	 * Stvara novi kompleksni broj zapisan u obliku Stringa. Metoda čita taj String, parsira ga i 
	 * iz njega, ukoliko je zapis ispravan, čita realni i imaginarni dio te stvara takav kompleksni broj.
	 * 
	 * <p>Dozvoljeni zapisi uključuju čiste realne brojeve poput "351", "-317", "+3.51",
	 * čiste imaginarne brojeve poput "2i", "+1.25i", "i", "-5i", "-i", ali i kompleksne brojeve
	 * poput "-2.71-3.15i", "31+24i", "-1-i".
	 * 
	 * <p>Metoda neće parsirati String ukoliko se stavi previše nepotrebnih znakova + i - poput
	 * "-2.71+-3.15i". Imaginarna jedinica i mora stajati iza broja, a ne ispred, pa se Stringovi poput
	 * "i3.51" neće parsirati. U tim slučajevima će metoda baciti {@code NumberFormatException}.
	 * 
	 * @param s String kompleksnog broja.
	 * @return parsirani kompleksni broj.
	 * @throws NullPointerException ako se kao String preda {@code null} referenca.
	 * @throws NumberFormatException ako format kompleksnog broja u Stringu nije ispravan.
	 */
	public static ComplexNumber parse(String s) {
		return ComplexNumberFormat.parse(s);
	}
	
	/**
	 * Obavlja operaciju zbrajanja ovog kompleksnog broja sa predanim kompleksnim brojem 
	 * {@code c}. Pri tome metoda stvara novi kompleksni broj koji predstavlja taj zbroj, 
	 * dakle (ovaj + {@code c}).
	 * 
	 * @param c kompleksni broj s kojim se obavlja zbrajanje.
	 * @return novi kompleksni broj (ovaj + {@code c}).
	 * @throws NullPointerException ako se kao kompleksni broj {@code c} preda {@code null}.
	 */
	public ComplexNumber add(ComplexNumber c) {
		Objects.requireNonNull(c, "Predani kompleksni broj je null.");
		
		double re = this.real + c.real;
		double im = this.imaginary + c.imaginary;
		
		return new ComplexNumber(re, im);
	}
	
	/**
	 * Obavlja operaciju oduzimanja ovog kompleksnog broja sa predanim kompleksnim brojem 
	 * {@code c}. Pri tome metoda stvara novi kompleksni broj koji predstavlja tu razliku, 
	 * dakle (ovaj - {@code c}).
	 * 
	 * @param c kompleksni broj s kojim se obavlja oduzimanje.
	 * @return novi kompleksni broj (ovaj - {@code c}).
	 * @throws NullPointerException ako se kao kompleksni broj {@code c} preda {@code null}.
	 */
	public ComplexNumber sub(ComplexNumber c) {
		Objects.requireNonNull(c, "Predani kompleksni broj je null.");
		
		double re = this.real - c.real;
		double im = this.imaginary - c.imaginary;
		
		return new ComplexNumber(re, im);
	}
	
	/**
	 * Obavlja operaciju množenja ovog kompleksnog broja sa predanim kompleksnim brojem 
	 * {@code c}. Pri tome metoda stvara novi kompleksni broj koji predstavlja taj umnožak, 
	 * dakle (ovaj * {@code c}).
	 * 
	 * @param c kompleksni broj s kojim se obavlja množenje.
	 * @return novi kompleksni broj (ovaj * {@code c}).
	 * @throws NullPointerException ako se kao kompleksni broj {@code c} preda {@code null}.
	 */
	public ComplexNumber mul(ComplexNumber c) {
		Objects.requireNonNull(c, "Predani kompleksni broj je null.");
		
		double mag = this.getMagnitude() * c.getMagnitude();
		double ang = this.getAngle() + c.getAngle();
		
		return fromMagnitudeAndAngle(mag, ang);
	}
	
	/**
	 * Obavlja operaciju dijeljenja ovog kompleksnog broja sa predanim kompleksnim brojem 
	 * {@code c}. Pri tome metoda stvara novi kompleksni broj koji predstavlja taj kvocijent, 
	 * dakle (ovaj / {@code c}).
	 * 
	 * @param c kompleksni broj s kojim se obavlja dijeljenje.
	 * @return novi kompleksni broj (ovaj / {@code c}).
	 * @throws NullPointerException ako se kao kompleksni broj {@code c} preda {@code null}.
	 */
	public ComplexNumber div(ComplexNumber c) {
		Objects.requireNonNull(c, "Predani kompleksni broj je null.");
		if (c.real == 0.0 && c.imaginary == 0.0)
			throw new IllegalArgumentException("Dijeljenje s nulom.");
		
		double mag = this.getMagnitude() / c.getMagnitude();
		double ang = this.getAngle() - c.getAngle();
		
		return fromMagnitudeAndAngle(mag, ang);
	}
	
	/**
	 * Obavlja operaciju potenciranja ovog kompleksnog broja na {@code n}-tu potenciju. 
	 * Pri tome metoda stvara novi kompleksni broj koji predstavlja tu potenciju, 
	 * dakle (ovaj kompleksni broj)<sup>n</sup>.
	 * 
	 * @param n eksponent potencije.
	 * @return novi kompleksni broj (ovaj kompleksni broj)<sup>n</sup>.
	 * @throws IllegalArgumentException ako je predani eksponent negativan.
	 */
	public ComplexNumber power(int n) {
		if (n < 0)
			throw new IllegalArgumentException(
					"Potencija kompleksnog broja mora biti veći ili jednak 0, a predano je " + n);
		if (n == 0)
			return fromReal(1.0);
		if (n == 1)
			return new ComplexNumber(real, imaginary);
		
		double mag = pow(getMagnitude(), n);
		double ang = this.getAngle() * n;
		
		return fromMagnitudeAndAngle(mag, ang);
	}
	
	/**
	 * Obavlja operaciju {@code n}-tog korjenovanja ovog kompleksnog broja. 
	 * Pri tome metoda stvara polje od {@code n} korijena ovog kompleksnog broja.
	 * 
	 * @param n stupanj korijena
	 * @return polje koje sadrži {@code n} kompleksnih brojeva korijena.
	 * @throws IllegalArgumentException ako je predani stupanj korijena {@code n} 
	 * negativan ili 0.
	 */
	public ComplexNumber[] root(int n) {
		if (n <= 0)
			throw new IllegalArgumentException("Korijen mora biti pozitivan, a predano je " + n);
		
		ComplexNumber[] roots = new ComplexNumber[n];
		double mag = pow(getMagnitude(), 1.0/n);
		double currentAng = getAngle();
		for (int k=0; k<n; k++) {
			double ang = (currentAng + 2*k*PI) / n;
			roots[k] = fromMagnitudeAndAngle(mag, ang);
		}
		return roots;
	}
	
	/**
	 * Vraća String reprezentaciju ovog kompleksnog broja.
	 * 
	 * @return String reprezentacija ovog kompleksnog broja.
	 */
	@Override
	public String toString() {
		if (real == 0.0 && imaginary == 0.0)
			return "0";
		String realStr = real == 0.0 ? "" : Double.toString(real);
		
		String imaginaryStr;
		if (imaginary == 0)
			imaginaryStr = "";
		else if (abs(imaginary) == 1.0)
			imaginaryStr = imaginary > 0.0 ? "" : "-";
		else
			imaginaryStr = Double.toString(imaginary);
		
		String inBetween = null;
		if (real == 0.0 || imaginary <= 0.0)
			inBetween = "";
		else
			inBetween = "+";
		
		return realStr + inBetween + imaginaryStr + (imaginary == 0.0 ? "" : "i");
	}
	
	/**
	 * Provjerava jednakost ovog kompleksnog broja sa predanim objektom {@code obj}.
	 * Očekuje se da će referenca {@code obj} biti referenca na kompleksni broj.
	 * Ukoliko je to istina provjeravaju se jednakosti realnih i imaginarnih dijelova 
	 * do na prvih 6 decimala. Ukoliko referenca {@code obj} ne pokazuje na kompleksni 
	 * broj ili je pak {@code null}, metoda vraća false.
	 * 
	 * @params obj objekt s kojim provjeravamo jednakost ovog kompleksnog broja.
	 * @return {@code true} ako je {@code obj} kompleksni broj i ako su im realni i 
	 * imaginarni dijelovi jednaki do na prvih 6 decimala, inače {@code false}.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (obj.getClass() != ComplexNumber.class)
			return false;
		
		ComplexNumber complex = (ComplexNumber) obj;
		
		double epsilon = 1e-6;
		double deltaRe = abs(real - complex.real);
		double deltaIm = abs(imaginary - complex.imaginary);
		
		return deltaRe < epsilon && deltaIm < epsilon;
	}
	
}
