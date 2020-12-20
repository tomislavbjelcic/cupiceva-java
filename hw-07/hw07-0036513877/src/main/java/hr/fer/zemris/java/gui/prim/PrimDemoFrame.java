package hr.fer.zemris.java.gui.prim;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

public class PrimDemoFrame extends JFrame {
	
	private PrimListModel model;
	private JList<Integer> leftList;
	private JList<Integer> rightList;
	private JButton nextBtn;
	
	public PrimDemoFrame() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(200, 200);
		setTitle("Prime Demo");
		initGUI();
	}
	
	private void initGUI() {
		model = new PrimListModel();
		
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		
		JPanel listPanel = new JPanel();
		listPanel.setLayout(new GridLayout(1, 0));
		leftList = new JList<>(model);
		rightList = new JList<>(model);
		listPanel.add(new JScrollPane(leftList));
		listPanel.add(new JScrollPane(rightList));
		cp.add(listPanel, BorderLayout.CENTER);
		
		nextBtn = new JButton("sljedeći");
		cp.add(nextBtn, BorderLayout.PAGE_END);
		
		nextBtn.addActionListener(e -> model.next());
	}
	
}
