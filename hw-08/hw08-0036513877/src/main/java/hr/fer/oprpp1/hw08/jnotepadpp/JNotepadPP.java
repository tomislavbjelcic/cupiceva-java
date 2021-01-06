package hr.fer.oprpp1.hw08.jnotepadpp;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import hr.fer.oprpp1.hw08.jnotepadpp.components.CloseTabButton;
import hr.fer.oprpp1.hw08.jnotepadpp.icons.SaveIcons;

public class JNotepadPP extends JFrame {
	
	public JNotepadPP() {
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setSize(500, 500);
		this.initGUI();
	}
	
	private void initGUI() {
		Container cp = this.getContentPane();
		cp.setLayout(new FlowLayout());
		
		Action ac = new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.out.println("o");
			}
			
		};
		
		CloseTabButton ctb = new CloseTabButton(ac);
		Icon icon = SaveIcons.RED_SAVE_ICON;
		
		cp.add(new JLabel(icon));
		
		
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			JNotepadPP frame = new JNotepadPP();
			frame.setVisible(true);
		});
	}
	
}
