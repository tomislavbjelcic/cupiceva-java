package hr.fer.oprpp1.hw08.jnotepadpp;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import hr.fer.oprpp1.hw08.jnotepadpp.models.DefaultMultipleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.models.MultipleDocumentListener;
import hr.fer.oprpp1.hw08.jnotepadpp.models.MultipleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.models.SingleDocumentModel;

public class JNotepadPP extends JFrame {
	
	public JNotepadPP() {
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setSize(500, 500);
		this.initGUI();
	}
	
	private void initGUI() {
		Container cp = this.getContentPane();
		cp.setLayout(new BorderLayout());
		
		JPanel panel = new JPanel();
		JButton remTabBtn = new JButton("Ukloni");
		JTextField tf = new JTextField();
		Dimension tfpref = tf.getPreferredSize();
		tfpref.width = 50;
		tf.setPreferredSize(tfpref);
		panel.add(tf); panel.add(remTabBtn);
		cp.add(panel, BorderLayout.PAGE_START);
		
		JButton addTabBtn = new JButton("Dodaj");
		cp.add(addTabBtn, BorderLayout.PAGE_END);
		
		DefaultMultipleDocumentModel paneModel = new DefaultMultipleDocumentModel();
		MultipleDocumentModel model = paneModel;
		model.addMultipleDocumentListener(new MultipleDocumentListener() {

			@Override
			public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
				System.out.println("cdc");
			}

			@Override
			public void documentAdded(SingleDocumentModel model) {
				System.out.println("docAdded");
			}

			@Override
			public void documentRemoved(SingleDocumentModel model) {
				System.out.println("docRemoved");
			}
			
		});
		
		cp.add(paneModel, BorderLayout.CENTER);
		
		remTabBtn.addActionListener(e -> {
			String input = tf.getText();
			if (input.isBlank()) {
				System.out.println("Blank input");
				return;
			}
			int idx = Integer.parseInt(input);
			SingleDocumentModel doc = model.getDocument(idx);
			model.closeDocument(doc);
		});
		
		addTabBtn.addActionListener(e -> {
			model.createNewDocument();
		});
		
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			JNotepadPP frame = new JNotepadPP();
			frame.setVisible(true);
		});
	}
	
}
