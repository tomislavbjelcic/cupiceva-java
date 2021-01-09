package hr.fer.oprpp1.hw08.jnotepadpp;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import hr.fer.oprpp1.hw08.jnotepadpp.actions.CreateNewDocumentAction;
import hr.fer.oprpp1.hw08.jnotepadpp.actions.LocalizableAction;
import hr.fer.oprpp1.hw08.jnotepadpp.actions.OpenDocumentAction;
import hr.fer.oprpp1.hw08.jnotepadpp.actions.SaveAsDocumentAction;
import hr.fer.oprpp1.hw08.jnotepadpp.actions.SaveDocumentAction;
import hr.fer.oprpp1.hw08.jnotepadpp.local.FormLocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.local.ILocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.local.LocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.models.DefaultMultipleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.models.MultipleDocumentModel;

public class JNotepadPP extends JFrame {
	
	private FormLocalizationProvider flp;
	private MultipleDocumentModel model;
	
	private Action newDocAction;
	private Action openAction;
	private Action saveAction;
	private Action saveAsAction;
	private Action closeAction;
	private Action quitAction;
	
	private JToolBar toolBar;
	
	public JNotepadPP() {
		flp = new FormLocalizationProvider(LocalizationProvider.getInstance(), this);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setSize(1000, 700);
		this.initGUI();
	}
	
	private void initGUI() {
		Container cp = this.getContentPane();
		cp.setLayout(new BorderLayout());
		
		DefaultMultipleDocumentModel jTabbedPane = new DefaultMultipleDocumentModel(flp);
		this.model = jTabbedPane;
		cp.add(jTabbedPane, BorderLayout.CENTER);
		
		this.createActions();
		this.createMenus();
		
		/*
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
		*/
		
	}
	
	private void createActions() {
		this.newDocAction = new CreateNewDocumentAction(this, flp, model);
		this.openAction = new OpenDocumentAction(this, flp, model);
		this.saveAction = new SaveDocumentAction(this, flp, model);
		this.saveAsAction = new SaveAsDocumentAction(this, flp, model);
	}
	
	private void createMenus() {
		JMenuBar menuBar = new JMenuBar();
		class MenuAction extends LocalizableAction {
			public MenuAction(ILocalizationProvider provider, String nameKey) {
				super(provider);
				this.putLocalizedValue(Action.NAME, nameKey);
			}
			@Override
			public void actionPerformed(ActionEvent e) {}
			
		}

		JMenu fileMenu = new JMenu(new MenuAction(flp, "file"));
		menuBar.add(fileMenu);

		fileMenu.add(new JMenuItem(newDocAction));
		fileMenu.add(new JMenuItem(openAction));
		fileMenu.addSeparator();
		fileMenu.add(new JMenuItem(saveAction));
		fileMenu.add(new JMenuItem(saveAsAction));

		this.setJMenuBar(menuBar);
	}
	
	private void createToolBars() {
		
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			JNotepadPP frame = new JNotepadPP();
			frame.setVisible(true);
		});
	}
	
}
