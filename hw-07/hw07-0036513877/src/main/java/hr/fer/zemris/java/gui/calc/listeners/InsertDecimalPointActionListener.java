package hr.fer.zemris.java.gui.calc.listeners;

import java.awt.event.ActionEvent;

import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.calc.model.CalculatorInputException;

public class InsertDecimalPointActionListener extends CalculatorActionListener {

	public InsertDecimalPointActionListener(CalcModel model) {
		super(model);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			model.insertDecimalPoint();
		} catch (CalculatorInputException ex) {}
	}
	
}
