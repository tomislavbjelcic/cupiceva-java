package hr.fer.zemris.math;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Predstavlja polinom nad kompleksnim brojevima u faktoriziranom obliku:<br>
 * P(z) = a * (z - z<sub>1</sub>) * (z - z<sub>2</sub>) * ... * (z - z<sub>n</sub>),<br>
 * gdje je {@code a} vodeći koeficijent, a z<sub>1</sub> do z<sub>n</sub> nultočke.
 * 
 * @author Tomislav Bjelčić
 *
 */
public class ComplexRootedPolynomial {

	/**
	 * Vodeći koeficijent.
	 */
	private Complex constant;
	/**
	 * Nultočke ovog polinoma.
	 */
	private Complex[] roots;

	/**
	 * Stvara novi faktorizirani polinom sa vodećim koeficijentom {@code constant} 
	 * i nultočkama {@code roots}.
	 * 
	 * @param constant vodeći koeficijent polinoma
	 * @param roots nultočke polinoma
	 * @throws NullPointerException ako je bilo koji od predanih argumenata {@code null}, 
	 * osim u slučaju kad je {@code constant} jednak 0, tada se nultočke diskreditiraju jer nemaju smisla 
	 * (ako je vodeći koeficijent jednak 0, cijeli polinom je funkcijski jednak 0, pa ima beskonačno mnogo 
	 * nultočaka). Tada je u potpunosti nebitno što su daljnji argumenti, pa oni mogu biti i {@code null} 
	 * bez izazivanja iznimke.
	 */
	public ComplexRootedPolynomial(Complex constant, Complex ... roots) {
		this.constant = Objects.requireNonNull(constant, "Konstanta faktoriziranog polinoma je null.");
		
		if (constant.equals(Complex.ZERO)) {
			this.roots = new Complex[0];
			return;
		}
		Objects.requireNonNull(roots, "Predane nultočke polinoma su null.");
		this.roots = new Complex[roots.length];
		for (int i=0; i<roots.length; i++)
			this.roots[i] = Objects.requireNonNull(roots[i], "Predana nultočka pod rednim brojem " + (i+1) + " je null.");
	}

	/**
	 * Evaluira vrijednost polinoma u točki {@code z}.
	 * 
	 * @param z kompleksni broj koji se "uvrštava" u polinom.
	 * @return kompleksni broj jednak evaluaciji P({@code z}).
	 * @throws NullPointerException ako je predani kompleksni broj {@code null}.
	 */
	public Complex apply(Complex z) {
		Objects.requireNonNull(z, "Predani kompleksni broj je null.");
		Complex result = constant;
		for (Complex r : roots) {
			result = result.multiply(z.sub(r));
		}
		return result;
	}

	/**
	 * Vraća poziciju (indeks), ako takva postoji, nultočke koja je najmanje udaljena od predane 
	 * kompleksne točke {@code z}, i pri tome je njihova udaljenost na Gaussovoj ravnini 
	 * manja ili jednaka od vrijednosti {@code treshold}.<br>
	 * Ako ne postoji takva nultočka, odnosno ako su udaljenosti svih nultočaka 
	 * od točke {@code z} veće od vrijednosti {@code treshold}, ili ako ovaj polinom 
	 * nema nultočaka (ako se radi o konstanti, u što je uključeno ako ima beskonačno mnogo nultočaka za 
	 * polinom funkcijski jednak 0), metoda vraća -1.<br>
	 * Vraćena pozicija će odgovarati redosljedu kojim su nultočke specificirane 
	 * prilikom stvaranja ovog polinoma, krećući od 0.
	 * 
	 * @param z kompleksna točka od koje se traži pozicija najbliže nultočke.
	 * @param treshold granična vrijednost udaljenosti.
	 * @return pozicija nultočke koja je najbliža točki {@code z}.
	 * @throws NullPointerException ako je predani kompleksni broj {@code null}.
	 */
	public int indexOfClosestRootFor(Complex z, double treshold) {
		Objects.requireNonNull(z, "Predani kompleksni broj je null.");

		int rootLen = roots.length;
		if (rootLen == 0)
			return -1;

		double minDistance = z.sub(roots[0]).module();
		int index = minDistance <= treshold ? 0 : -1;
		for (int i=1; i<rootLen; i++) {
			Complex root = roots[i];
			double distance = z.sub(root).module();
			if (distance < minDistance && distance <= treshold) {
				minDistance = distance;
				index = i;
			}
		}
		return index;
	}

	/**
	 * Stvara kompleksni polinom razreda {@link ComplexPolynomial} (oblik sa sumom i potencijama) 
	 * iz ovog polinoma (faktorizirani oblik).
	 * 
	 * @return kompleksni polinom u obliku suma i potencija.
	 */
	public ComplexPolynomial toComplexPolynom() {
		ComplexPolynomial p = new ComplexPolynomial(constant);
		for (Complex r : roots) {
			ComplexPolynomial linearFactor
			= new ComplexPolynomial(r.negate(), Complex.ONE);
			p = p.multiply(linearFactor);
		}
		return p;
	}

	/**
	 * Vraća String reprezentaciju ovog polinoma.
	 * 
	 * @return String reprezentacija ovog polinoma.
	 */
	@Override
	public String toString() {
		String constantString = Util.addParentheses(constant);
		if (roots.length == 0)
			return constantString;

		String zm = "z-";
		String mul = "*";
		String rootsStr = Arrays.stream(roots)
				.map(c -> Util.addParentheses(zm + Util.addParentheses(c)))
				.collect(Collectors.joining(mul));

		return constantString + mul + rootsStr;
	}
}
