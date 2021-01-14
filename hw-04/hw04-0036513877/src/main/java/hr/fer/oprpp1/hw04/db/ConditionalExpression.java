package hr.fer.oprpp1.hw04.db;

import java.util.Map;
import java.util.Objects;

/**
 * Predstavlja općeniti uvjetni izraz.
 * 
 * @author Tomislav Bjelčić
 *
 */
public class ConditionalExpression {

	/**
	 * Objekt koji dohvaća atribut, izraz koji će biti na lijevoj strani operatora.
	 */
	private IFieldValueGetter fieldGetter;
	/**
	 * String koji će biti izraz na desnoj strani izraza operatora.
	 */
	private String stringLiteral;
	/**
	 * Operator ovog uvjetnog izraza.
	 */
	private IComparisonOperator comparisonOperator;

	/**
	 * Stvara novi uvjetni izraz.
	 * 
	 * @param fieldGetter objekt koji dohvaća atribut koji će biti lijeva strana operatora.
	 * @param stringLiteral izraz na desnoj strani operatora.
	 * @param comparisonOperator operator.
	 * 
	 * @throws NullPointerException ako bilo koji od predanih parametara je {@code null}.
	 */
	public ConditionalExpression(IFieldValueGetter fieldGetter, String stringLiteral,
			IComparisonOperator comparisonOperator) {
		this.fieldGetter = Objects.requireNonNull(fieldGetter);
		this.stringLiteral = Objects.requireNonNull(stringLiteral);
		this.comparisonOperator = Objects.requireNonNull(comparisonOperator);
	}

	/**
	 * Vraća objekt koji dohvaća atribut na lijevoj strani operatora.
	 * 
	 * @return objekt koji dohvaća atribut.
	 */
	public IFieldValueGetter getFieldGetter() {
		return fieldGetter;
	}

	/**
	 * Vraća izraz na desnoj strani operatora.
	 * 
	 * @return izraz na desnoj strani operatora.
	 */
	public String getStringLiteral() {
		return stringLiteral;
	}

	/**
	 * Vraća operator uvjetnog izraza
	 * 
	 * @return operator uvjetnog izraza.
	 */
	public IComparisonOperator getComparisonOperator() {
		return comparisonOperator;
	}
	
	/**
	 * Stvara novi uvjetni izraz iz predanog Stringa.
	 * 
	 * @param s String koji predstavlja uvjetni izraz.
	 * @return uvjetni izraz predstavljen predanim Stringom.
	 * @throws NullPointerException ako je predani String {@code null}.
	 * @throws QueryException ako String zapis uvjetnog izraz nije u ispravnom formatu.
	 */
	public static ConditionalExpression parse(String s) {
		Objects.requireNonNull(s, "Predani uvjetni izraz je null.");
		
		s = s.strip();
		Map.Entry<String, IFieldValueGetter> entryFieldValue = null;
		
		for (var entry : FieldValueGetters.FIELD_GETTER_MAP.entrySet()) {
			if (s.startsWith(entry.getKey())) {
				entryFieldValue = entry;
				break;
			}
		}
		
		if (entryFieldValue == null)
			throw new QueryException("Uvjetni izraz \"" + s + "\" ne počinje ispravnim imenom atributa.");
		String fieldName = entryFieldValue.getKey();
		
		int off = fieldName.length();
		String rest = s.substring(off).strip();
		
		Map.Entry<String, IComparisonOperator> entryCompOper = null;
		for (var entry : ComparisonOperators.OPERATOR_MAP.entrySet()) {
			if (rest.startsWith(entry.getKey())) {
				entryCompOper = entry;
				break;
			}
		}
		
		if (entryCompOper == null)
			throw new QueryException(
					"Uvjetni izraz \"" + s + "\": nakon atributa " + fieldName + " nema operatora.");
		String op = entryCompOper.getKey();
		off = op.length();
		rest = rest.substring(off).strip();
		
		char quotation = '\"';
		if (rest.isEmpty() || rest.charAt(0) != quotation)
			throw new QueryException("Uvjetni izraz \"" + s + "\": "
					+ "nakon operatora " + op + " nema String literala unutar navodnika.");
		
		int lastQuoteIndex = rest.indexOf(quotation, 1);
		if (lastQuoteIndex == -1)
			throw new QueryException("Uvjetni izraz \"" + s + "\": "
					+ "nakon operatora " + op + " String literal nije zatvoren navodnikom");
			
		String literal = rest.substring(1, lastQuoteIndex);
		rest = rest.substring(lastQuoteIndex + 1).strip();
		if (!rest.isEmpty())
			throw new QueryException("Uvjetni izraz \"" + s + 
					"\": nakon String literala u navodnicima ne smije biti više ništa.");
		IFieldValueGetter fieldGetter = entryFieldValue.getValue();
		IComparisonOperator compOperator = entryCompOper.getValue();
		
		return new ConditionalExpression(fieldGetter, literal, compOperator);
	}
	
}
