package hr.fer.zemris.java.gui.calc.listeners;

import java.awt.event.ActionEvent;
import java.util.function.DoubleBinaryOperator;

import hr.fer.zemris.java.gui.calc.BinaryOperatorJButton;
import hr.fer.zemris.java.gui.calc.model.CalcModel;

public class BinaryOperatorActionListener extends CalculatorActionListener {
	
	private BinaryOperatorJButton btn;

	public BinaryOperatorActionListener(CalcModel model, BinaryOperatorJButton btn) {
		super(model);
		this.btn = btn;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		DoubleBinaryOperator op = btn.getBinaryOperator();
		
		boolean operandSet = model.isActiveOperandSet();
		boolean hasFrozen = model.hasFrozenValue();
		
		if (!operandSet && !hasFrozen) { // za operand postavi postojecu vrijednost
			model.setActiveOperand(model.getValue());
		}
		
		if (hasFrozen) {	// operator je unesen drugi put za redom
			model.setPendingBinaryOperation(op);
			return;
		}
		
		// od ovog trenutka aktivni operand je postavljen i nema zamrznute vrijednosti
		
		DoubleBinaryOperator pending = model.getPendingBinaryOperation();
		if (pending != null) {
			// izvedi operaciju na cekanju
			double right = model.getValue();
			double left = model.getActiveOperand();
			double result = pending.applyAsDouble(left, right);
			model.setValue(result);
		}
		
		String str = model.toString();
		double activeOperand = model.getValue();
		model.setActiveOperand(activeOperand);
		model.setPendingBinaryOperation(op);
		model.freezeValue(str);
		model.clear();
	}
	
}
