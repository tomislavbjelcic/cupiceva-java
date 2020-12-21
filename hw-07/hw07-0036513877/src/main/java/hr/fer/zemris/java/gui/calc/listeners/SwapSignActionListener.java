package hr.fer.zemris.java.gui.calc.listeners;

import java.awt.event.ActionEvent;

import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.calc.model.CalculatorInputException;

public class SwapSignActionListener extends CalculatorActionListener {

	public SwapSignActionListener(CalcModel model) {
		super(model);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			model.swapSign();
		} catch (CalculatorInputException ex) {}
	}
	
}
