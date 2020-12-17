package hr.fer.zemris.java.gui.calc;

import java.util.function.DoubleUnaryOperator;

import javax.swing.JButton;

public class UnaryOperatorJButton extends JButton {
	
	private DoubleUnaryOperator originalOperator;
	private DoubleUnaryOperator reversedOperator;
	private String originalOperatorName;
	private String reversedOperatorName;
	
	public UnaryOperatorJButton(DoubleUnaryOperator originalOperator, DoubleUnaryOperator reversedOperator,
			String originalOperatorName, String reversedOperatorName) {
		super();
		this.originalOperator = originalOperator;
		this.reversedOperator = reversedOperator;
		this.originalOperatorName = originalOperatorName;
		this.reversedOperatorName = reversedOperatorName;
	}
	
	
	
}
