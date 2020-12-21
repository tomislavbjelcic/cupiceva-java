package hr.fer.zemris.java.gui.calc;

import java.util.function.DoubleUnaryOperator;


public class UnaryOperatorJButton extends OperatorJButton {
	
	private DoubleUnaryOperator originalOperator;
	private DoubleUnaryOperator reversedOperator;
	
	private DoubleUnaryOperator currentOperator;
	
	public UnaryOperatorJButton(DoubleUnaryOperator originalOperator, DoubleUnaryOperator reversedOperator,
			String originalOperatorText, String reversedOperatorText) {
		super(originalOperatorText, reversedOperatorText);
		this.originalOperator = originalOperator;
		this.reversedOperator = reversedOperator;
		setOperator(true);
	}

	@Override
	public void setOperator(boolean original) {
		currentOperator = original ? originalOperator : reversedOperator;
	}
	
	public DoubleUnaryOperator getUnaryOperator() {
		return currentOperator;
	}

	
	
	
	
}
