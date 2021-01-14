package hr.fer.oprpp1.custom.collections.demo;

import java.util.Objects;

import hr.fer.oprpp1.custom.collections.EmptyStackException;
import hr.fer.oprpp1.custom.collections.ObjectStack;

/**
 * Kalkulator (evaluator) sposoban za računanje matematičkih izraza u 
 * <a href="https://en.wikipedia.org/wiki/Reverse_Polish_notation">postfix notaciji</a>.
 * 
 * <p>Evaluator podržava izraze koji uključuju samo cjelobrojne operande i ograničen 
 * skup operatora.
 * 
 * @author Tomislav Bjelčić
 *
 */
public class PostfixExpressionEvaluator {
	
	/**
	 * Razred predstavlja iznimku koja se baca kada je predani izraz prilikom računanja 
	 * postfix izraza neispravan.
	 * 
	 * @author Tomislav Bjelčić
	 *
	 */
	public static class InvalidExpressionException extends RuntimeException {
		/**
		 * Stvara novu iznimku sa porukom {@code msg}.
		 * 
		 * @param msg poruka iznimke.
		 */
		public InvalidExpressionException(String msg) {
			super(msg);
		}
	}
	
	
	/**
	 * Skup (polje) podržanih operatora.
	 */
	private static final String[] SUPPORTED_OPERATORS = {"+", "-", "*", "/", "%"};
	/**
	 * Stog koji se koristi prilikom računanja postfix izraza.
	 */
	private ObjectStack stack = new ObjectStack();
	
	/**
	 * Računa predani postfix izraz.
	 * 
	 * @param expr izraz koji se računa.
	 * @return rezultat izraza.
	 * @throws NullPointerException ako je predani izraz {@code null}.
	 * @throws IllegalArgumentException ako je izraz prazan ili se sastoji samo od bjelina.
	 * @throws InvalidExpressionException ako izraz sadrži neispravan token, 
	 * postoje neiskorišteni operatori ili ostane više od jednog broja kad se iskoriste
	 * svi operatori.
	 * Uzrok neispravnosti tokena može biti nepodržani operator, nepodržani tip operanda 
	 * ili jednostavno neispravno zapisani konstrukt.
	 * Primjerice, pokušaj računanja sljedećih izraza će završiti ovom iznimkom: 
	 * "5.5 1 +" (operand 5.5 nije cjelobrojan), "5 2 ^" (operator ^ nije podržan),
	 * "5# 5 +" (konstrukt 5# je neispravan), "3 5 + /" (operator / se ne može iskoristiti), 
	 * "1 4 2 +" (ostanu brojevi 1 i 6).
	 * @throws ArithmeticException ukoliko prilikom računanja izraza se obavlja
	 * matematički nedozvoljena operacija poput dijeljenja s nulom.
	 */
	public int evaluate(String expr) {
		Objects.requireNonNull(expr, "Predani postfix izraz je null.");
		
		Object[] tokens = tokenize(expr);
		
		for (Object token : tokens) {
			if (token.getClass() == Integer.class) { // token je broj
				stack.push(token);
				continue;
			} else {	// token je operator, dakle String
				String operator = (String) token;
				int left = 0, right = 0;
				try {
					// na stogu mogu postojati samo brojevi, stoga je castanje sigurno
					// međutim zbog neispravnosti izraza moguće je skidanje s praznog stoga
					right = ((Integer) stack.pop()).intValue();
					left = ((Integer) stack.pop()).intValue();
					
					int result = performOperation(left, right, operator);	// može baciti ArithmeticException
					stack.push(result);
				} catch (EmptyStackException ex) {	// desi se jer nema 2 broja na stogu
					throw new InvalidExpressionException(
							"Neispravan izraz: nema dovoljno operanada uz operator " + operator
							);	// maskiraj grešku praznog stoga greškom neispravnog postfix izraza
				} catch (ArithmeticException ex) {
					// baci istu iznimku sa drugom porukom
					throw new ArithmeticException(
									"Neispravan izraz: nedozvoljena operacija " +
									left + operator + right
									);
				}
				
			}
		}
		
		if (stack.size() != 1) {
			throw new InvalidExpressionException("Neispravan izraz: postoje neiskorišteni operandi u izrazu.");
		}
		
		return ((Integer) stack.pop()).intValue();
	}
	
	/**
	 * Pomoćna metoda koja prima postfix izraz u obliku Stringa, 
	 * rastavlja ga na jedinke (takozvane tokene) i pri tome provjerava ispravnost 
	 * tokena u smislu da provjerava jesu li operatori podržani i jesu li svi operandi 
	 * cijeli brojevi.
	 * 
	 * @param expr izraz koji se rastavlja na tokene.
	 * @return polje tokena.
	 * @throws IllegalArgumentException ako je izraz prazan ili se sastoji samo od bjelina.
	 * @throws InvalidExpressionException ako izraz sadrži neispravan token.
	 */
	private static Object[] tokenize(String expr) {
		if (expr.isBlank())
			throw new IllegalArgumentException("Izraz kojeg treba evaluirati je prazan.");
		
		String[] noSpaces = expr.split(" ");
		Object[] tokens = new Object[noSpaces.length];
		
		for (int i=0, len=noSpaces.length; i<len; i++) {
			String token = noSpaces[i];
			if (isOperator(token)) {
				tokens[i] = token;
				continue;
			}
			
			try {
				Object number = Integer.parseInt(token);
				tokens[i] = number;
			} catch(NumberFormatException ex) {
				throw new InvalidExpressionException("Neispravan token: " + token);
			}
		}
		return tokens;
	}
	
	/**
	 * Pomoćna metoda koja provjerava je li predani operator u skupu podržanih operatora.
	 * 
	 * @param operator
	 * @return {@code true} ukoliko je operator podržan, inače {@code false}.
	 */
	private static boolean isOperator(String operator) {
		for (String op : SUPPORTED_OPERATORS) {
			if (op.equals(operator))
				return true;
		}
		return false;
	}
	
	/**
	 * Pomoćna metoda koja prima cjelobrojne operande i operator, obavlja operaciju i 
	 * pozivatelju vraća rezultat operacije.
	 * 
	 * <p> Primjerice, poziv {@code performOperation(3, 5, "+")} bi vratilo 8.
	 * 
	 * @param left prvi, lijevi operand.
	 * @param right drugi, desni operand.
	 * @param operation operator
	 * @return rezultat operacije.
	 * @throws ArithmeticException ako se obavlja matematički nedozvoljena operacija
	 * poput dijeljenja s nulom.
	 */
	private static int performOperation(int left, int right, String operation) {
		int result = switch(operation) {
			case "+" -> left + right;
			case "-" -> left - right;
			case "*" -> left * right;
			case "/" -> left / right;
			case "%" -> left % right;
			default -> 0;	// nikad se neće desiti
		};
		return result;
	}
	
}
