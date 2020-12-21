package hr.fer.zemris.java.gui.calc.listeners;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import hr.fer.zemris.java.gui.calc.OperatorJButton;

public class CalculatorInvCheckBoxItemListener implements ItemListener {
	
	private OperatorJButton btn;
	
	public CalculatorInvCheckBoxItemListener(OperatorJButton btn) {
		this.btn = btn;
	}
	
	@Override
	public void itemStateChanged(ItemEvent e) {
		boolean original = e.getStateChange() == ItemEvent.DESELECTED;
		btn.setOperatorButton(original);
	}

}
