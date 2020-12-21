package hr.fer.zemris.java.gui.calc;

import javax.swing.JButton;

public abstract class OperatorJButton extends JButton {
	
	private String originalOperatorText;
	private String reversedOperatorText;
	
	private String currentOperatorText;

	protected OperatorJButton(String originalOperatorText, String reversedOperatorText) {
		super();
		this.originalOperatorText = originalOperatorText;
		this.reversedOperatorText = reversedOperatorText;
		setOperatorText(true);
	}
	
	protected void setOperatorText(boolean original) {
		currentOperatorText = original ? originalOperatorText : reversedOperatorText;
		setText(currentOperatorText);
	}
	
	public abstract void setOperator(boolean original);
	
	public void setOperatorButton(boolean original) {
		setOperator(original);
		setOperatorText(original);
	}
	
	
}
