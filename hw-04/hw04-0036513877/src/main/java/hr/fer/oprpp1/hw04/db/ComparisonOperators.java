package hr.fer.oprpp1.hw04.db;

import java.util.Map;

/**
 * Razred koji sadrži implementacije različitih operatora uvjetnog izraza.
 * 
 * @author Tomislav Bjelčić
 *
 */
public class ComparisonOperators {
	
	/**
	 * Operator manje "<".
	 */
	public static final IComparisonOperator LESS 
				= (v1, v2) -> v1.compareTo(v2) < 0;
	/**
	 * Operator manje ili jednako "<=".
	 */
	public static final IComparisonOperator LESS_OR_EQUALS 
				= (v1, v2) -> v1.compareTo(v2) <= 0;
	/**
	 * Operator veće ">".
	 */
	public static final IComparisonOperator GREATER 
				= (v1, v2) -> v1.compareTo(v2) > 0;
	/**
	 * Operator veće ili jednako ">=".
	 */
	public static final IComparisonOperator GREATER_OR_EQUALS 
				= (v1, v2) -> v1.compareTo(v2) >= 0;
	/**
	 * Operator jednako "=".
	 */
	public static final IComparisonOperator EQUALS 
				= String::equals;
	/**
	 * Operator različito (nije jednako) "!=".
	 */
	public static final IComparisonOperator NOT_EQUALS 
				= (v1, v2) -> !v1.equals(v2);
				
	/**
	 * Operator "LIKE" koji provjerava zadovoljava li izraz na lijevoj strani 
	 * uzorku specificiran desnom stranom.<br>
	 * Taj uzorak može sadržavati najviše jedan zamjenski znak "*" koji predstavlja 
	 * zamjenu za bilo koji niz od 0 ili više znakova. Ako takvih znakova ima više 
	 * od jednog, izaziva se iznimka.
	 */
	public static final IComparisonOperator LIKE = (v1, v2) -> {
		char wildcard = '*';
		long wildcardCount = v2.chars().filter(ch -> ch == wildcard).count();
		if (wildcardCount > 1L)
			throw new QueryException("Desna strana operatora LIKE ima više od 1 zamjenskog znaka " + wildcard);
		
		String regex = v2.replace("*", ".*");
		return v1.matches(regex);
	};
	
	/**
	 * Preslikavanje (mapa) zapis operatora -> objekt operatora.
	 */
	public static final Map<String, IComparisonOperator> OPERATOR_MAP
				= Map.of("<", LESS,
						"<=", LESS_OR_EQUALS,
						">", GREATER,
						">=", GREATER_OR_EQUALS,
						"=", EQUALS,
						"!=", NOT_EQUALS,
						"LIKE", LIKE);
	
	/**
	 * Onemogući stvaranje instanci ovog razreda jer nema smisla.
	 */
	private ComparisonOperators() {}
	
}
