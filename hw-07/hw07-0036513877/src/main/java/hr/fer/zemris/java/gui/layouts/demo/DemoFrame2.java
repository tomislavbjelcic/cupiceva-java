package hr.fer.zemris.java.gui.layouts.demo;

import java.awt.Color;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import hr.fer.zemris.java.gui.layouts.CalcLayout;
import hr.fer.zemris.java.gui.layouts.RCPosition;

public class DemoFrame2 extends JFrame {
	
	public DemoFrame2() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(500, 500);
		initGUI();
	}
	private void initGUI() {
		Container cp = getContentPane();cp.setLayout(new CalcLayout(3));
		for (int i=1; i<=5; i++) {
			for (int j=1; j<=7; j++) {
				String text = "" + i + ", " + j;
				try {
					RCPosition pos = RCPosition.parse(text);
					cp.add(l(text), pos);
				} catch (RuntimeException ex) {
					continue;
				}
			}
		}
		
		
	}
	private JLabel l(String text) {
		JLabel l = new JLabel(text);
		l.setBackground(Color.YELLOW);
		l.setOpaque(true);
		return l;
	}
	public static void main(String[] args) {
		SwingUtilities.invokeLater(()->{
			new DemoFrame2().setVisible(true);
		});
	}
	
}
