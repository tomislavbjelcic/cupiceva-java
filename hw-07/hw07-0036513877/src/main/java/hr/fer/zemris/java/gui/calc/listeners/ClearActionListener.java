package hr.fer.zemris.java.gui.calc.listeners;

import java.awt.event.ActionEvent;

import hr.fer.zemris.java.gui.calc.model.CalcModel;

public class ClearActionListener extends CalculatorActionListener {

	public ClearActionListener(CalcModel model) {
		super(model);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		model.clear();
	}
}
