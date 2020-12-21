package hr.fer.zemris.java.gui.calc;

import javax.swing.SwingUtilities;

public class Calculator {
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			CalculatorFrame frame = new CalculatorFrame();
			frame.setVisible(true);
		});
	}
	
}