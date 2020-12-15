package hr.fer.zemris.math;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.hypot;
import static java.lang.Math.pow;
import static java.lang.Math.sin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * Predstavlja nepromjenjivi kompleksni broj {@code z = real + i * imaginary}.
 * 
 * @author Tomislav Bjelčić
 *
 */
public class Complex {
	
	/**
	 * Kompleksni broj {@code z = 0}.
	 */
	public static final Complex ZERO = new Complex(0,0);
	/**
	 * Kompleksni broj {@code z = 1}.
	 */
	public static final Complex ONE = new Complex(1,0);
	/**
	 * Kompleksni broj {@code z = -1}.
	 */
	public static final Complex ONE_NEG = new Complex(-1,0);
	/**
	 * Kompleksni broj {@code z = i}.
	 */
	public static final Complex IM = new Complex(0,1);
	/**
	 * Kompleksni broj {@code z = -i}.
	 */
	public static final Complex IM_NEG = new Complex(0,-1);
	
	/**
	 * Realni dio ovog kompleksnog broja.
	 */
	private double real;
	/**
	 * Imaginarni dio ovog kompleksnog broja.
	 */
	private double imaginary;
	
	/**
	 * Stvara novi kompleksni broj {@code z = real + i * imaginary}.
	 * 
	 * @param real realni dio
	 * @param imaginary imaginarni dio
	 */
	public Complex(double real, double imaginary) {
		this.real = real;
		this.imaginary = imaginary;
	}
	
	/**
	 * Stvara novi kompleksni broj pomoću polarnih koordinata: modula 
	 * {@code module} i argumenta (kuta) kompleksnog broja {@code angle}:<br>
	 * {@code z = module * (cos(angle) + i * sin(angle))}.
	 * 
	 * @param module modul kompleksnog broja
	 * @param angle argument kompleksnog broja.
	 * @return kompleksni broj {@code z = module * (cos(angle) + i * sin(angle))}.
	 * @throws IllegalArgumentException ako je predani modul negativan.
	 */
	public static Complex fromModuleAndAngle(double module, double angle) {
		if (module < 0.0)
			throw new IllegalArgumentException("Modul kompleksnog broja ne može biti negativan.");
		
		double real = module * cos(angle);
		double imaginary = module * sin(angle);
		
		return new Complex(real, imaginary);
	}
	
	/**
	 * Računa i vraća modul ovog kompleksnog broja. Kada bi se kompleksni broj {@code z} prikazao 
	 * u Gaussovoj kompleksnoj ravnini, modul bi bio udaljenost točke tog kompleksnog broja 
	 * od ishodišta. Računa se kao |z| = sqrt(x<sup>2</sup> + y<sup>2</sup>), gdje x i y 
	 * predstavljaju realni i imaginarni dio od {@code z}.
	 * @return modul ovog kompleksnog broja.
	 */
	public double module() {
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
	public double angle() {
		double ang = atan2(imaginary, real);
		return ang < 0 ? ang + 2*PI : ang;
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
	public Complex multiply(Complex c) {
		Objects.requireNonNull(c, "Predani kompleksni broj je null.");
		
		double re = this.real * c.real - this.imaginary * c.imaginary;
		double im = this.real * c.imaginary + c.real * this.imaginary;
		return new Complex(re, im);
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
	public Complex divide(Complex c) {
		Objects.requireNonNull(c, "Predani kompleksni broj je null.");
		
		/*
		if (c.equals(ZERO))
			throw new IllegalArgumentException("Dijeljenje s nulom.");
		*/
		
		double cmodsq = c.real * c.real + c.imaginary * c.imaginary;
		double re = (this.real * c.real + this.imaginary * c.imaginary) / cmodsq;
		double im = (c.real * this.imaginary - this.real * c.imaginary) / cmodsq;
		return new Complex(re, im);
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
	public Complex add(Complex c) {
		Objects.requireNonNull(c, "Predani kompleksni broj je null.");
		
		double real = this.real + c.real;
		double imaginary = this.imaginary + c.imaginary;
		return new Complex(real, imaginary);
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
	public Complex sub(Complex c) {
		Objects.requireNonNull(c, "Predani kompleksni broj je null.");
		
		double real = this.real - c.real;
		double imaginary = this.imaginary - c.imaginary;
		return new Complex(real, imaginary);
	}
	
	/**
	 * Vraća novi kompleksni broj {@code z = -ovaj}.
	 * 
	 * @return novi kompleksni broj {@code z = -ovaj}.
	 */
	public Complex negate() {
		return new Complex(-real, -imaginary);
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
	public Complex power(int n) {
		if (n < 0)
			throw new IllegalArgumentException(
					"Potencija kompleksnog broja mora biti veći ili jednak 0, a predano je " + n);
		if (n == 0)
			return ONE;
		if (n == 1)
			return this;
		
		double mod = pow(module(), n);
		double ang = angle() * n;
		
		return fromModuleAndAngle(mod, ang);
	}
	
	/**
	 * Obavlja operaciju {@code n}-tog korjenovanja ovog kompleksnog broja. 
	 * Pri tome metoda stvara listu od {@code n} korijena ovog kompleksnog broja.
	 * 
	 * @param n stupanj korijena
	 * @return lista koja sadrži {@code n} kompleksnih brojeva korijena.
	 * @throws IllegalArgumentException ako je predani stupanj korijena {@code n} 
	 * negativan ili 0.
	 */
	public List<Complex> root(int n) {
		if (n <= 0)
			throw new IllegalArgumentException("Korijen mora biti pozitivan, a predano je " + n);
		
		List<Complex> roots = new ArrayList<>(n);
		double module = pow(module(), 1.0/n);
		double currentAng = angle();
		for (int k=0; k<n; k++) {
			double ang = (currentAng + 2*k*PI) / n;
			Complex r = fromModuleAndAngle(module, ang);
			roots.add(r);
		}
		return roots;
	}
	
	/**
	 * Stvara novi kompleksni broj zapisan u obliku Stringa. Metoda čita taj String, parsira ga i 
	 * iz njega, ukoliko je zapis ispravan, čita realni i imaginarni dio te stvara takav kompleksni broj.<br>
	 * Ukoliko String {@code s} nije u ispravnom formatu kompleksnog broja, metoda izaziva {@code NumberFormatException}.
	 * 
	 * @param s String kompleksnog broja.
	 * @return parsirani kompleksni broj.
	 * @throws NullPointerException ako se kao String preda {@code null} referenca.
	 * @throws NumberFormatException ako format kompleksnog broja u Stringu nije ispravan.
	 */
	public static Complex parse(String s) {
		return ComplexNumberFormat.parse(s);
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
		if (obj.getClass() != this.getClass())
			return false;
		
		Complex complex = (Complex) obj;
		
		double epsilon = 1e-6;
		double deltaRe = abs(real - complex.real);
		double deltaIm = abs(imaginary - complex.imaginary);
		
		return deltaRe < epsilon && deltaIm < epsilon;
	}
	
	/**
	 * Vraća String reprezentaciju ovog kompleksnog broja.
	 * 
	 * @return String reprezentacija ovog kompleksnog broja.
	 */
	@Override
	public String toString() {
		double imaginaryAbs = abs(imaginary);
		char inBetween = imaginary < 0 ? '-' : '+';
		StringBuilder sb = new StringBuilder();
		String realStr = String.format("%.3f", real);
		String imaginaryAbsStr = String.format("%.3f", imaginaryAbs);
		sb.append(realStr).append(inBetween)
				.append('i').append(imaginaryAbsStr);
		return sb.toString();
	}
	
}
