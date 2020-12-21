package hr.fer.zemris.java.gui.prim;

import javax.swing.SwingUtilities;

public class PrimDemo {
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			PrimDemoFrame frame = new PrimDemoFrame();
			frame.setVisible(true);
		});
	}
	
}
