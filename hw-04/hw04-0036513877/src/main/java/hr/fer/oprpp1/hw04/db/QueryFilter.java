package hr.fer.oprpp1.hw04.db;

import java.util.List;

/**
 * Predstavlja implementaciju filtera studentskih zapisa koji na temelju specificiranih 
 * uvjetnih izraza provjerava zadovoljava li studentski zapis svaki uvjetni izraz (operacija I).
 * 
 * @author Tomislav Bjelčić
 *
 */
public class QueryFilter implements IFilter {
	
	/**
	 * Lista uvjetnih izraza.
	 */
	private List<ConditionalExpression> exprs;
	
	/**
	 * Stvara novi filter zapisa na temelju liste uvjetnih izraza {@code exprs}.
	 * 
	 * @param exprs lista uvjetnih izraza.
	 */
	public QueryFilter(List<ConditionalExpression> exprs) {
		this.exprs = exprs;
	}
	
	
	@Override
	public boolean accepts(StudentRecord record) {
		for (ConditionalExpression expr : exprs) {
			IFieldValueGetter getter = expr.getFieldGetter();
			IComparisonOperator oper = expr.getComparisonOperator();
			String literal = expr.getStringLiteral();
			
			String attribute = getter.get(record);
			boolean b = oper.satisfied(attribute, literal);
			if (!b)
				return false;
		}
		return true;
	}
		
}
