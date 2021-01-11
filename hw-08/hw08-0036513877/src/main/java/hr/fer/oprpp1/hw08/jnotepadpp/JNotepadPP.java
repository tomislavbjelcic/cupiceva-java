package hr.fer.oprpp1.hw08.jnotepadpp;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import hr.fer.oprpp1.hw08.jnotepadpp.actions.Clipboard;
import hr.fer.oprpp1.hw08.jnotepadpp.actions.CloseDocumentAction;
import hr.fer.oprpp1.hw08.jnotepadpp.actions.CreateNewDocumentAction;
import hr.fer.oprpp1.hw08.jnotepadpp.actions.LanguageChangeAction;
import hr.fer.oprpp1.hw08.jnotepadpp.actions.LocalizableAction;
import hr.fer.oprpp1.hw08.jnotepadpp.actions.OpenDocumentAction;
import hr.fer.oprpp1.hw08.jnotepadpp.actions.QuitAction;
import hr.fer.oprpp1.hw08.jnotepadpp.actions.SaveAsDocumentAction;
import hr.fer.oprpp1.hw08.jnotepadpp.actions.SaveDocumentAction;
import hr.fer.oprpp1.hw08.jnotepadpp.actions.StatisticalInfoAction;
import hr.fer.oprpp1.hw08.jnotepadpp.local.FormLocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.local.ILocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.local.LocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.models.DefaultMultipleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.models.MultipleDocumentAdapter;
import hr.fer.oprpp1.hw08.jnotepadpp.models.MultipleDocumentListener;
import hr.fer.oprpp1.hw08.jnotepadpp.models.MultipleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.models.SingleDocumentModel;

public class JNotepadPP extends JFrame {
	
	private static final String APP_NAME = "JNotepad++";
	private static final LocalizationProvider lp = LocalizationProvider.getInstance();
	private FormLocalizationProvider flp;
	private MultipleDocumentModel model;
	
	private Action newDocAction;
	private Action openAction;
	private Action saveAction;
	private Action saveAsAction;
	private Action closeAction;
	private Action quitAction;
	
	private Action statInfoAction;
	
	
	private static final String[] languages = {"en", "hr"};
	
	private Clipboard clipboard;
	
	private static class MenuAction extends LocalizableAction {
		
		public MenuAction(ILocalizationProvider provider, String nameKey) {
			super(provider);
			this.putLocalizedValue(Action.NAME, nameKey);
		}
		@Override
		public void actionPerformed(ActionEvent e) {}
		
		
	}
	
	public JNotepadPP() {
		flp = new FormLocalizationProvider(lp, this);
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.setSize(1000, 700);
		this.initGUI();
	}
	
	private void initGUI() {
		Container cp = this.getContentPane();
		cp.setLayout(new BorderLayout());
		
		DefaultMultipleDocumentModel jTabbedPane = new DefaultMultipleDocumentModel(this, flp);
		this.model = jTabbedPane;
		MultipleDocumentListener mdl = new MultipleDocumentAdapter() {
			@Override
			public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
				String title = currentModel == null ? APP_NAME :
					(currentModel.getFullPathString() + " - " + APP_NAME);
				JNotepadPP.this.setTitle(title);
			}
		};
		mdl.currentDocumentChanged(null, null);
		this.model.addMultipleDocumentListener(mdl);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				quitAction.actionPerformed(null);
			}
		});
		cp.add(jTabbedPane, BorderLayout.CENTER);
		
		this.createActions();
		this.createMenus();
		this.createToolBar();
		
	}
	
	private void createActions() {
		this.newDocAction = new CreateNewDocumentAction(this, flp, model);
		this.openAction = new OpenDocumentAction(this, flp, model);
		this.saveAction = new SaveDocumentAction(this, flp, model);
		this.saveAsAction = new SaveAsDocumentAction(this, flp, model);
		this.closeAction = new CloseDocumentAction(this, flp, model);
		this.quitAction = new QuitAction(this, flp, model);
		this.statInfoAction = new StatisticalInfoAction(this, flp, model);
	}
	
	private void createMenus() {
		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu(new MenuAction(flp, "file"));
		menuBar.add(fileMenu);
		fileMenu.add(new JMenuItem(newDocAction));
		fileMenu.add(new JMenuItem(openAction));
		fileMenu.addSeparator();
		fileMenu.add(new JMenuItem(saveAction));
		fileMenu.add(new JMenuItem(saveAsAction));
		fileMenu.addSeparator();
		fileMenu.add(new JMenuItem(closeAction));
		fileMenu.addSeparator();
		fileMenu.add(new JMenuItem(quitAction));
		
		JMenu editMenu = new JMenu(new MenuAction(flp, "edit"));
		menuBar.add(editMenu);
		clipboard = new Clipboard(this, flp, model);
		editMenu.add(new JMenuItem(clipboard.getCopyAction()));
		editMenu.add(new JMenuItem(clipboard.getCutAction()));
		editMenu.add(new JMenuItem(clipboard.getPasteAction()));
		
		JMenu toolsMenu = new JMenu(new MenuAction(flp, "tools"));
		menuBar.add(toolsMenu);
		toolsMenu.add(new JMenuItem(statInfoAction));
		JMenu changeCaseSubMenu = new JMenu(clipboard.getChangeCaseAction());
		changeCaseSubMenu.add(new JMenuItem(clipboard.getToLowerCaseAction()));
		changeCaseSubMenu.add(new JMenuItem(clipboard.getToUpperCaseAction()));
		changeCaseSubMenu.add(new JMenuItem(clipboard.getInvertCaseAction()));
		toolsMenu.add(changeCaseSubMenu);
		
		JMenu langMenu = new JMenu(new MenuAction(flp, "language"));
		menuBar.add(langMenu);
		for (String lang : languages)
			langMenu.add(new JMenuItem(new LanguageChangeAction(flp, lp, lang)));
		
		this.setJMenuBar(menuBar);
	}
	
	public Action getSaveAction() {
		return saveAction;
	}
	
	private void createToolBar() {
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(true);
		
		toolBar.add(new JButton(newDocAction));
		toolBar.add(new JButton(openAction));
		toolBar.addSeparator();
		toolBar.add(new JButton(saveAction));
		toolBar.add(new JButton(saveAsAction));
		toolBar.addSeparator();
		toolBar.add(new JButton(closeAction));
		toolBar.addSeparator();
		toolBar.add(new JButton(clipboard.getCopyAction()));
		toolBar.add(new JButton(clipboard.getCutAction()));
		toolBar.add(new JButton(clipboard.getPasteAction()));
		toolBar.addSeparator();
		toolBar.add(new JButton(statInfoAction));
		toolBar.add(new JButton(quitAction));
		
		Container cp = this.getContentPane();
		cp.add(toolBar, BorderLayout.PAGE_START);
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			JNotepadPP frame = new JNotepadPP();
			frame.setVisible(true);
		});
	}
	
}
