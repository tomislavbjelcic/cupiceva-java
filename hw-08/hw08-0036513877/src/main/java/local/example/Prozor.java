package local.example;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import hr.fer.oprpp1.hw08.jnotepadpp.local.FormLocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.local.ILocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.local.LocalizationProvider;

public class Prozor extends JFrame {

	private FormLocalizationProvider flp;

	public Prozor() {
		flp = new FormLocalizationProvider(LocalizationProvider.getInstance(), this);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Demo");
		initGUI();
		pack();
	}

	private void initGUI() {
		getContentPane().setLayout(new FlowLayout());



		JButton gumbYes = new JButton(new LocalizableAction(flp, "yes") {
			@Override
			public void actionPerformed(ActionEvent e) {}
		});
		JButton gumbNo = new JButton(new LocalizableAction(flp, "no") {
			@Override
			public void actionPerformed(ActionEvent e) {}
		});
		getContentPane().add(gumbYes);
		getContentPane().add(gumbNo);

		createMenuBar();
	}

	private void createMenuBar() {
		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu(new LocalizableAction(flp, "language") {
			@Override
			public void actionPerformed(ActionEvent e) {}
		});
		menuBar.add(fileMenu);

		class LAbstractAction extends LocalizableAction {
			

			public LAbstractAction(ILocalizationProvider provider, String key) {
				super(provider, key);
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				LocalizationProvider.getInstance().setLanguage(key);
			}

		}
		Action acEn = new LAbstractAction(flp, "en");
		Action acHr = new LAbstractAction(flp, "hr");
		Action acDe = new LAbstractAction(flp, "de");

		fileMenu.add(new JMenuItem(acEn));
		fileMenu.add(new JMenuItem(acHr));
		fileMenu.add(new JMenuItem(acDe));

		this.setJMenuBar(menuBar);
	}

	public static void main(String[] args) {

		SwingUtilities.invokeLater(() -> {
			Prozor p = new Prozor();
			p.setVisible(true);
		});
	}
}
