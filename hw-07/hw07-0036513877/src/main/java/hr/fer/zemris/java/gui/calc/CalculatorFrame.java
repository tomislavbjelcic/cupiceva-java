package hr.fer.zemris.java.gui.calc;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.calc.model.CalcModelImpl;
import hr.fer.zemris.java.gui.layouts.CalcLayout;
import hr.fer.zemris.java.gui.layouts.RCPosition;

public class CalculatorFrame extends JFrame {
	
	private static final int GAP = 10;
	private static final String TITLE = "Java Calculator v1.0";
	private static final Color BTN_COLOR = new Color(221, 221, 255);
	private static final Color SCREEN_COLOR = Color.YELLOW;
	private static final float SMALLER_FONT_SIZE = 20.0f;
	private static final float BIGGER_FONT_SIZE = 50.0f;
	
	private JLabel screen;
	
	private JButton sinBtn;
	private JButton cosBtn;
	private JButton tanBtn;
	private JButton ctgBtn;
	private JButton reciprocalBtn;
	private JButton logBtn;
	private JButton lnBtn;
	private JButton powBtn;
	
	private JButton[] digitButtons = new JButton[10];
	
	private JButton swapSignBtn;
	private JButton dotBtn;
	private JButton eqBtn;
	private JButton divBtn;
	private JButton mulBtn;
	private JButton subBtn;
	private JButton addBtn;
	
	private JButton clrBtn;
	private JButton pushBtn;
	private JButton popBtn;
	private JButton resetBtn;
	
	private JCheckBox invCheckBox;
	
	private CalcModel model;
	
	public CalculatorFrame() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(1000, 500);
		this.setLocation(100, 100);
		initGUI();
	}
	
	private void initGUI() {
		setTitle(TITLE);
		Container cp = getContentPane();
		cp.setLayout(new CalcLayout(GAP));
		initGUIComponents();
		initListeners();
	}
	
	private void initGUIComponents() {
		Container cp = getContentPane();
		
		screen = makeScreen(); cp.add(screen, "1, 1");
		eqBtn = b("=", false); cp.add(eqBtn, "1, 6");
		clrBtn = b("clr", false); cp.add(clrBtn, "1, 7");
		
		reciprocalBtn = b("1/x", false); cp.add(reciprocalBtn, "2, 1");
		sinBtn = b("sin", false); cp.add(sinBtn, "2, 2");
		divBtn = b("/", false); cp.add(divBtn, "2, 6");
		resetBtn = b("reset", false); cp.add(resetBtn, "2, 7");
		
		logBtn = b("log", false); cp.add(logBtn, "3, 1");
		cosBtn = b("cos", false); cp.add(cosBtn, "3, 2");
		mulBtn = b("*", false); cp.add(mulBtn, "3, 6");
		pushBtn = b("push", false); cp.add(pushBtn, "3, 7");
		
		lnBtn = b("ln", false); cp.add(lnBtn, "4, 1");
		tanBtn = b("tan", false); cp.add(tanBtn, "4, 2");
		subBtn = b("-", false); cp.add(subBtn, "4, 6");
		popBtn = b("pop", false); cp.add(popBtn, "4, 7");
		
		powBtn = b("x^n", false); cp.add(powBtn, "5, 1");
		ctgBtn = b("ctg", false); cp.add(ctgBtn, "5, 2");
		swapSignBtn = b("+/-", false); cp.add(swapSignBtn, "5, 4");
		dotBtn = b(".", false); cp.add(dotBtn, "5, 5");
		addBtn = b("+", false); cp.add(addBtn, "5, 6");
		
		invCheckBox = makeInvCheckBox(); cp.add(invCheckBox, "5, 7");
		
		for (int i=0; i<digitButtons.length; i++) {
			String istr = Integer.toString(i);
			int row = 0;
			int col = 0;
			if (i==0) {
				row = 5;
				col = 3;
			} else {
				int div = (i-1)/3;
				int remainder = (i-1) % 3;
				row = 4 - div;
				col = 3 + remainder;
			}
			digitButtons[i] = b(istr, true); cp.add(digitButtons[i], new RCPosition(row, col));
		}
	}
	
	private void initListeners() {
		
	}
	
	private void initCalcModel() {
		model = new CalcModelImpl();
	}
	
	private static JButton b(String text, boolean isDigitBtn) {
		JButton btn = new JButton(text);
		btn.setBackground(BTN_COLOR);
		float size = isDigitBtn ? BIGGER_FONT_SIZE : SMALLER_FONT_SIZE;
		Font f = btn.getFont().deriveFont(size);
		
		btn.setFont(f);
		return btn;
	}
	
	private static JLabel makeScreen() {
		JLabel screen = new JLabel();
		
		screen.setOpaque(true);
		screen.setHorizontalAlignment(SwingConstants.RIGHT);
		screen.setBackground(SCREEN_COLOR);
		screen.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		screen.setFont(screen.getFont().deriveFont(BIGGER_FONT_SIZE));
		
		return screen;
	}
	
	private static JCheckBox makeInvCheckBox() {
		JCheckBox cb = new JCheckBox("Inv");
		cb.setFont(cb.getFont().deriveFont(SMALLER_FONT_SIZE));
		return cb;
	}
	
}
