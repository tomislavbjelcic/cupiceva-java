package hr.fer.zemris.java.gui.calc.listeners;

import java.awt.event.ActionListener;

import hr.fer.zemris.java.gui.calc.model.CalcModel;

public abstract class CalculatorActionListener implements ActionListener {
	
	protected CalcModel model;

	public CalculatorActionListener(CalcModel model) {
		this.model = model;
	}
	
}
