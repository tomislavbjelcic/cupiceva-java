package hr.fer.zemris.java.gui.calc.listeners;

import java.awt.event.ActionEvent;
import java.util.function.DoubleBinaryOperator;

import hr.fer.zemris.java.gui.calc.model.CalcModel;

public class EqualsActionListener extends CalculatorActionListener {

	public EqualsActionListener(CalcModel model) {
		super(model);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (model.hasFrozenValue())
			return;
		
		DoubleBinaryOperator op = model.getPendingBinaryOperation();
		double val = model.getValue();
		if (op == null) {
			model.setValue(val);
		} else {
			double left = model.getActiveOperand();
			double right = val;
			double result = op.applyAsDouble(left, right);
			model.setValue(result);
		}
		
		model.clearActiveOperand();
		model.setPendingBinaryOperation(null);
	}
	
}
