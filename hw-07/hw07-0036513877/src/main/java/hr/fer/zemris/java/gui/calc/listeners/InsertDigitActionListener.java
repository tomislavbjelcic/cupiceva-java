package hr.fer.zemris.java.gui.calc.listeners;

import java.awt.event.ActionEvent;

import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.calc.model.CalculatorInputException;

public class InsertDigitActionListener extends CalculatorActionListener {

	private int digit;
	
	public InsertDigitActionListener(CalcModel model, int digit) {
		super(model);
		this.digit = digit;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (!model.isEditable()) {
			model.clear();
		}
		
		try {
			model.insertDigit(digit);
		} catch (CalculatorInputException ex) {
			// ex.printStackTrace();
		}
	}
}
