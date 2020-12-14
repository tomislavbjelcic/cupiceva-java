package hr.fer.zemris.math;

import java.util.Arrays;
import java.util.Objects;

/**
 * Predstavlja polinom nad kompleksnim brojevima u obliku:<br>
 * P(z) = a<sub>n</sub>*z<sup>n</sup> + a<sub>n-1</sub>*z<sup>n-1</sup> + ... + 
 * a<sub>2</sub>*z<sup>2</sup> + a<sub>1</sub>*z<sup>1</sup> + a<sub>0</sub>,<br>
 * gdje su a<sub>n</sub>, ..., a<sub>n</sub> kompleksni koeficijenti polinoma pri čemu a<sub>n</sub> 
 * (tzv. vodeći koeficijent) nije jednak 0. 
 * 
 * @author Tomislav Bjelčić
 *
 */
public class ComplexPolynomial {
	
	/**
	 * Koeficijenti polinoma čije pozicije u polju odgovaraju eksponentu potencije uz koje se nalaze.
	 */
	private Complex[] coefs;
	
	
	/**
	 * Stvara novi polinom sa koeficijentima {@code coefs}.
	 * 
	 * @param coefs koeficijenti polinoma.
	 * @throws NullPointerException ako je bilo koji od predanih argumenata {@code null}.
	 * @throws IllegalArgumentException ako nije predan niti jedan koeficijent.
	 */
	public ComplexPolynomial(Complex... coefs) {
		Objects.requireNonNull(coefs, "Predani koeficijenti polinoma su null.");
		
		int coefsLen = coefs.length;
		if (coefsLen < 1)
			throw new IllegalArgumentException("Polinom mora imati barem jedan koeficijent.");
		
		this.coefs = new Complex[coefsLen];
		boolean leadingFound = false;
		for (int i=coefsLen-1; i>=0; i--) {
			Complex c = Objects.requireNonNull(coefs[i], "Koeficijent z" + i + " je null.");
			boolean isLeading = !leadingFound && (!c.equals(Complex.ZERO) || i==0);
			if (isLeading) {
				leadingFound = true;
				this.coefs = new Complex[i+1];
			}
			if (leadingFound)
				this.coefs[i] = c;
		}
	}
	
	/**
	 * Vraća stupanj ovog polinoma.
	 * 
	 * @return stupanj ovog polinoma.
	 */
	public short order() {
		return (short) (coefs.length - 1);
	}
	
	/**
	 * Vraća produkt (u funkcijskom smislu) ovog polinoma i polinoma {@code p}.
	 * 
	 * @param p polinom s kojim se množi ovaj polinom.
	 * @return produkt ovog polinoma i polinoma {@code p}.
	 * @throws NullPointerException ako je predani polinom {@code null}.
	 */
	public ComplexPolynomial multiply(ComplexPolynomial p) {
		Objects.requireNonNull(p, "Predani polinom je null.");
		
		int coefCountThis = this.coefs.length;
		int coefCountOther = p.coefs.length;
		int coefCountProduct = coefCountThis + coefCountOther - 1;
		Complex[] productCoefs = new Complex[coefCountProduct];
		Arrays.fill(productCoefs, Complex.ZERO);
		for (int i=0; i<coefCountThis; i++) {
			for (int j=0; j<coefCountOther; j++) {
				Complex z1 = this.coefs[i];
				Complex z2 = p.coefs[j];
				Complex prod = z1.multiply(z2);
				Complex added = productCoefs[i+j].add(prod);
				productCoefs[i+j] = added;
			}
		}
		return new ComplexPolynomial(productCoefs);
	}
	
	/**
	 * Vraća prvu derivaciju ovog polinoma.
	 * 
	 * @return prva derivacija ovog polinoma.
	 */
	public ComplexPolynomial derive() {
		int coefCount = coefs.length;
		if (coefCount == 1)
			return new ComplexPolynomial(Complex.ZERO);
		
		// polinom je stupnja barem 1
		Complex[] derivedCoefs = new Complex[coefCount - 1];
		for (int k=1; k<coefCount; k++) {
			Complex coef = coefs[k].multiply(new Complex(k, 0));
			derivedCoefs[k-1] = coef;
		}
		return new ComplexPolynomial(derivedCoefs);
	}
	
	/**
	 * Evaluira vrijednost polinoma u točki {@code z}.
	 * 
	 * @param z kompleksni broj koji se "uvrštava" u polinom.
	 * @return kompleksni broj jednak evaluaciji P({@code z}).
	 * @throws NullPointerException ako je predani kompleksni broj {@code null}.
	 */
	public Complex apply(Complex z) {
		Complex sum = coefs[coefs.length - 1];
		
		for (int i=coefs.length-2; i>=0; i--) {
			sum = sum.multiply(z).add(coefs[i]);
		}
		return sum;
	}
	
	/**
	 * Vraća String reprezentaciju ovog polinoma.
	 * 
	 * @return String reprezentacija ovog polinoma.
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		char mul = '*';
		char pot = '^';
		char z = 'z';
		char plus = '+';
		for (int k=coefs.length - 1; k>=0; k--) {
			String coefStr = Util.addParentheses(coefs[k]);
			String toAdd = k == 0 ? coefStr : coefStr + mul + z + pot + k + plus;
			sb.append(toAdd);
		}
		return sb.toString();
	}
}
