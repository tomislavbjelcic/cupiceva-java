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

import hr.fer.oprpp1.hw08.jnotepadpp.actions.CreateNewDocumentAction;
import hr.fer.oprpp1.hw08.jnotepadpp.local.FormLocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.local.LocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.models.DefaultMultipleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.models.MultipleDocumentListener;
import hr.fer.oprpp1.hw08.jnotepadpp.models.MultipleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.models.SingleDocumentModel;

public class JNotepadPP extends JFrame {
	
	private FormLocalizationProvider flp;
	
	public JNotepadPP() {
		flp = new FormLocalizationProvider(LocalizationProvider.getInstance(), this);
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
		
		
		DefaultMultipleDocumentModel paneModel = new DefaultMultipleDocumentModel(flp);
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
		
		JButton addTabBtn = new JButton(new CreateNewDocumentAction(this, flp, model));
		cp.add(addTabBtn, BorderLayout.PAGE_END);
		

		/*
		Container cp = this.getContentPane();
		cp.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));
		
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
		cp.add(new JLabel("README"));
		cp.add(ctb);
		*/
		
		
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			JNotepadPP frame = new JNotepadPP();
			frame.setVisible(true);
		});
	}
	
}
