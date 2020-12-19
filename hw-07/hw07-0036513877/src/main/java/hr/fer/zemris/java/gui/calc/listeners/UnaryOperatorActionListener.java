package hr.fer.zemris.java.gui.calc.listeners;

import java.awt.event.ActionEvent;
import java.util.function.DoubleUnaryOperator;

import hr.fer.zemris.java.gui.calc.UnaryOperatorJButton;
import hr.fer.zemris.java.gui.calc.model.CalcModel;

public class UnaryOperatorActionListener extends CalculatorActionListener {
	
	private UnaryOperatorJButton btn;
	
	public UnaryOperatorActionListener(CalcModel model, UnaryOperatorJButton btn) {
		super(model);
		this.btn = btn;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (model.hasFrozenValue())
			return;
		DoubleUnaryOperator op = btn.getUnaryOperator();
		double oldVal = model.getValue();
		double newVal = op.applyAsDouble(oldVal);
		model.setValue(newVal);
	}

}
