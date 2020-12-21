package hr.fer.zemris.java.gui.calc;

import java.util.function.DoubleBinaryOperator;

public class BinaryOperatorJButton extends OperatorJButton {
	
	private DoubleBinaryOperator originalOperator;
	private DoubleBinaryOperator reversedOperator;
	
	private DoubleBinaryOperator currentOperator;
	
	public BinaryOperatorJButton(DoubleBinaryOperator originalOperator, DoubleBinaryOperator reversedOperator,
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
	
	public DoubleBinaryOperator getBinaryOperator() {
		return currentOperator;
	}
	
}
