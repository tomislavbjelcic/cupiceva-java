package hr.fer.zemris.java.gui.calc.listeners;

import java.awt.event.ActionEvent;
import java.util.Deque;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import hr.fer.zemris.java.gui.calc.model.CalcModel;

public class PushPopActionListener extends CalculatorActionListener {
	
	private String dialogTitle;
	private Deque<Double> stack;
	private JFrame frame;
	private boolean push;
	
	public PushPopActionListener(CalcModel model, Deque<Double> stack, JFrame frame, boolean push) {
		super(model);
		this.stack = stack;
		this.frame = frame;
		this.push = push;
		dialogTitle = (push ? "Push" : "Pop") + " button povratna informacija";
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (push)
			pushAction();
		else
			popAction();
	}
	
	private void pushAction() {
		if (model.hasFrozenValue()) {
			showDialog("Ništa nije stavljeno na stog.", true);
			return;
		}
		
		double val = model.getValue();
		String valStr = model.toString();
		stack.push(val);
		showDialog("Stavljen broj " + valStr + " na stog.", false);
	}
	
	private void popAction() {
		if (stack.isEmpty()) {
			showDialog("Stog je prazan.", true);
			return;
		}
		
		double top = stack.pop();
		model.setValue(top);
		model.freezeValue(null);
	}
	
	private void showDialog(String msg, boolean err) {
		int type = err ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE;
		JOptionPane.showMessageDialog(frame, msg, dialogTitle, type);
	}
	
	
	
}
