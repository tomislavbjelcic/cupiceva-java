package hr.fer.oprpp1.hw08.jnotepadpp;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import hr.fer.oprpp1.hw08.jnotepadpp.models.DefaultMultipleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.models.MultipleDocumentModel;

public class JNotepadPP extends JFrame {
	
	public JNotepadPP() {
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setSize(500, 500);
		this.initGUI();
	}
	
	private void initGUI() {
		Container cp = this.getContentPane();
		cp.setLayout(new BorderLayout());
		
		
		DefaultMultipleDocumentModel m = new DefaultMultipleDocumentModel();
		cp.add(m, BorderLayout.CENTER);
		MultipleDocumentModel mdm = m;
		
		JButton addBtn = new JButton("nova");
		addBtn.addActionListener(e -> mdm.createNewDocument());
		cp.add(addBtn, BorderLayout.PAGE_END);
		
		JButton remSel = new JButton("izbrisi odabranu");
		remSel.addActionListener(e -> mdm.closeDocument(mdm.getDocument(m.getSelectedIndex())));
		cp.add(remSel, BorderLayout.PAGE_START);
		
		/*
		JTabbedPane tp = new JTabbedPane();
		tp.addTab("od", new JButton("aa"));
		tp.addTab("oddada", new JButton("aafff"));
		tp.addChangeListener(e -> System.out.println(e));
		cp.add(tp, BorderLayout.CENTER);
		
		JButton b = new JButton("lmaobruh");
		b.addActionListener(e -> {
			tp.removeTabAt(0);
		});
		cp.add(b, BorderLayout.PAGE_END);
		*/
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			JNotepadPP frame = new JNotepadPP();
			frame.setVisible(true);
		});
	}
	
}
