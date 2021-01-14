package hr.fer.oprpp1.custom.scripting.nodes;

import java.util.Objects;

import hr.fer.oprpp1.custom.scripting.elems.Element;
import hr.fer.oprpp1.custom.scripting.elems.ElementVariable;
import hr.fer.oprpp1.custom.scripting.lexer.SmartScriptLexer;

/**
 * Čvor koji predstavlja konstrukt FOR petlje jezika SmartScript.
 * 
 * @author Tomislav Bjelčić
 *
 */
public class ForLoopNode extends Node {
	
	/**
	 * Varijabla FOR petlje.
	 */
	private ElementVariable variable;
	/**
	 * Početni izraz FOR petlje.
	 */
	private Element startExpression;
	/**
	 * Završni izraz FOR petlje.
	 */
	private Element endExpression;
	/**
	 * Korak FOR petlje.
	 */
	private Element stepExpression;
	
	/**
	 * Stvara novi čvor FOR petlje sa specificiranim parametrima. Korak FOR petlje može biti i {@code null}.
	 * 
	 * @param variable varijabla FOR petlje.
	 * @param startExpression početni izraz FOR petlje.
	 * @param endExpression završni izraz FOR petlje.
	 * @param stepExpression korak FOR petlje. Može biti i {@code null}.
	 * @throws NullPointerException ako su varijabla, početni ili završni izraz {@code null}.
	 */
	public ForLoopNode(ElementVariable variable, Element startExpression, Element endExpression, Element stepExpression) {
		this.variable = Objects.requireNonNull(variable);
		this.startExpression = Objects.requireNonNull(startExpression);
		this.endExpression = Objects.requireNonNull(endExpression);
		this.stepExpression = stepExpression;
	}

	/**
	 * Vraća varijablu FOR petlje.
	 * 
	 * @return varijabla FOR petlje.
	 */
	public ElementVariable getVariable() {
		return variable;
	}

	/**
	 * Vraća početni izraz FOR petlje.
	 * 
	 * @return početni izraz FOR petlje.
	 */
	public Element getStartExpression() {
		return startExpression;
	}

	/**
	 * Vraća završni izraz FOR petlje.
	 * 
	 * @return završni izraz FOR petlje.
	 */
	public Element getEndExpression() {
		return endExpression;
	}

	/**
	 * Vraća korak FOR petlje. Ako nije specificiran, vraća {@code null}.
	 * 
	 * @return korak FOR petlje ako je specificiran, inače {@code null}.
	 */
	public Element getStepExpression() {
		return stepExpression;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(SmartScriptLexer.TAG_OPEN)
			.append(SmartScriptLexer.DOLLAR)
			.append("FOR").append(' ')
			.append(variable).append(' ')
			.append(startExpression).append(' ')
			.append(endExpression).append(' ')
			.append(stepExpression == null ? "" : stepExpression)
			.append(SmartScriptLexer.DOLLAR)
			.append(SmartScriptLexer.TAG_CLOSE)
			.append(super.toString())
			.append(SmartScriptLexer.TAG_OPEN)
			.append(SmartScriptLexer.DOLLAR)
			.append("END")
			.append(SmartScriptLexer.DOLLAR)
			.append(SmartScriptLexer.TAG_CLOSE);
		return sb.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean supEquals = super.equals(obj);
		if (!supEquals)
			return false;
		
		ForLoopNode forLoopObj = (ForLoopNode) obj;
		int thisElems = this.stepExpression == null ? 3 : 4;
		int objElems = forLoopObj.stepExpression == null ? 3 : 4;
		if (thisElems != objElems)
			return false;
		
		boolean stepEq = Objects.equals(this.stepExpression, forLoopObj.stepExpression);
		boolean allEq = this.variable.equals(forLoopObj.variable)
				&& this.startExpression.equals(forLoopObj.startExpression)
				&& this.endExpression.equals(forLoopObj.endExpression)
				&& stepEq;
		return allEq;
	}
	
}
