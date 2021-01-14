package hr.fer.oprpp1.hw04.db;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Razred predstavlja parser upita u bazu studentskih zapisa.<br>
 * Ovaj parser je sposoban pročitati upit u obliku Stringa (u određenom formatu) 
 * i stvoriti listu uvjetnih izraza koji će se kasnije evaluirati i spojiti 
 * sa logičkim I (AND).
 * 
 * @author Tomislav Bjelčić
 *
 */
public class QueryParser {

	/**
	 * Lista uvjetnih izraza.
	 */
	private List<ConditionalExpression> exprs;
	/**
	 * JMBAG koji se koristio u upitu ako je upit bio direktan.
	 */
	private String queriedJmbag;

	/**
	 * Stvara novi parser upita i pri tome stvara listu uvjetnih izraza na temelju 
	 * upita {@code query}.
	 * 
	 * @param query upit u bazu zapisa studenata.
	 * @throws NullPointerException ako je predani upit {@code null}.
	 * @throws QueryException ako predani upit nije u ispravnom formatu.
	 */
	public QueryParser(String query) {
		Objects.requireNonNull(query, "Predani upit je null.");
		parseQuery(query);
	}

	private void parseQuery(String query) {
		query = query.strip();
		if (query.isEmpty())
			throw new QueryException("Prazan upit.");

		String regex = "[Aa][Nn][Dd]";
		query = query.replaceAll(regex, " and ");
		String[] splitted = query.split("and");
		exprs = Arrays.stream(splitted)
				.map(ConditionalExpression::parse)
				.collect(Collectors.toUnmodifiableList());

		if (exprs.size() == 1) {
			ConditionalExpression first = exprs.get(0);
			if (first.getFieldGetter() == FieldValueGetters.JMBAG
					&& first.getComparisonOperator() == ComparisonOperators.EQUALS)
				queriedJmbag = first.getStringLiteral();
		}
	}

	/**
	 * Provjerava je li upit bio direktan.<br>
	 * Direktan upit je upit koji ima jedan uvjetni izraz oblika {@code jmbag = "nešto"}.
	 * 
	 * @return {@code true} ako je upit bio direktan, inače {@code false}.
	 */
	public boolean isDirectQuery() {
		return queriedJmbag != null;
	}

	/**
	 * Vraća JMBAG koji se koristio u upitu ako je upit bio direktan. Ako nije, metoda 
	 * izaziva {@code IllegalStateException}.
	 * 
	 * @return JMBAG koji se koristio u direktnom upitu.
	 * @throws IllegalStateException ako upit nije bio direktan.
	 */
	public String getQueriedJMBAG() {
		if (!isDirectQuery())
			throw new IllegalStateException("Upit nije bio direktan.");

		return queriedJmbag;
	}

	/**
	 * Dohvaća listu uvjetnih izraza iz upita.
	 * 
	 * @return lista uvjetnih izraza iz upita.
	 */
	public List<ConditionalExpression> getQuery() {
		return exprs;
	}


}
