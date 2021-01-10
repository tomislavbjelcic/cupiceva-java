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

import hr.fer.oprpp1.hw08.jnotepadpp.actions.LocalizableAction;
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


		LocalizableAction acY = new LocalizableAction(flp) {
			@Override
			public void actionPerformed(ActionEvent e) {}
		};
		acY.putLocalizedValue(Action.NAME, "yes");
		LocalizableAction acN = new LocalizableAction(flp) {
			@Override
			public void actionPerformed(ActionEvent e) {}
		};
		acN.putLocalizedValue(Action.NAME, "no");
		JButton gumbYes = new JButton(acY);
		JButton gumbNo = new JButton(acN);
		getContentPane().add(gumbYes);
		getContentPane().add(gumbNo);

		createMenuBar();
	}

	private void createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		
		LocalizableAction acFileMenu = new LocalizableAction(flp) {
			@Override
			public void actionPerformed(ActionEvent e) {}
		};
		acFileMenu.putLocalizedValue(Action.NAME, "language");
		JMenu fileMenu = new JMenu(acFileMenu);
		menuBar.add(fileMenu);

		class LAbstractAction extends LocalizableAction {
			
			private String lng;

			public LAbstractAction(ILocalizationProvider provider, String lng) {
				super(provider);
				this.putLocalizedValue(Action.NAME, lng);
				this.lng = lng;
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				LocalizationProvider.getInstance().setLanguage(lng);
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
