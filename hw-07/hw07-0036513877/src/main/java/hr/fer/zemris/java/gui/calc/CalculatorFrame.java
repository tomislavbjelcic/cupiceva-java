package hr.fer.zemris.java.gui.calc;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.util.Deque;
import java.util.LinkedList;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import hr.fer.zemris.java.gui.calc.listeners.BinaryOperatorActionListener;
import hr.fer.zemris.java.gui.calc.listeners.CalculatorInvCheckBoxItemListener;
import hr.fer.zemris.java.gui.calc.listeners.ClearActionListener;
import hr.fer.zemris.java.gui.calc.listeners.EqualsActionListener;
import hr.fer.zemris.java.gui.calc.listeners.InsertDecimalPointActionListener;
import hr.fer.zemris.java.gui.calc.listeners.InsertDigitActionListener;
import hr.fer.zemris.java.gui.calc.listeners.PushPopActionListener;
import hr.fer.zemris.java.gui.calc.listeners.ResetActionListener;
import hr.fer.zemris.java.gui.calc.listeners.SwapSignActionListener;
import hr.fer.zemris.java.gui.calc.listeners.UnaryOperatorActionListener;
import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.calc.model.CalcModelImpl;
import hr.fer.zemris.java.gui.calc.model.CalcValueListener;
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
	
	private UnaryOperatorJButton sinBtn;
	private UnaryOperatorJButton cosBtn;
	private UnaryOperatorJButton tanBtn;
	private UnaryOperatorJButton ctgBtn;
	private UnaryOperatorJButton reciprocalBtn;
	private UnaryOperatorJButton logBtn;
	private UnaryOperatorJButton lnBtn;
	private BinaryOperatorJButton powBtn;
	
	private JButton[] digitButtons = new JButton[10];
	
	private JButton swapSignBtn;
	private JButton dotBtn;
	private JButton eqBtn;
	private BinaryOperatorJButton divBtn;
	private BinaryOperatorJButton mulBtn;
	private BinaryOperatorJButton subBtn;
	private BinaryOperatorJButton addBtn;
	
	private JButton clrBtn;
	private JButton pushBtn;
	private JButton popBtn;
	private JButton resetBtn;
	
	private JCheckBox invCheckBox;
	private OperatorJButton[] reversibles;
	private UnaryOperatorJButton[] unaryOperatorButtons;
	private BinaryOperatorJButton[] binaryOperatorButtons;
	
	private CalcModel model = new CalcModelImpl();
	private Deque<Double> stack = new LinkedList<>();
	
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
		
		reciprocalBtn = uob(x->1/x, x->1/x, "1/x", "1/x"); cp.add(reciprocalBtn, "2, 1");
		sinBtn = uob(Math::sin, Math::asin, "sin", "arcsin"); cp.add(sinBtn, "2, 2");
		divBtn = bob((a,b)->a/b, null, "/", null); cp.add(divBtn, "2, 6"); //drugi argument kao null znaci da nema smisla inverzna operacija
		resetBtn = b("reset", false); cp.add(resetBtn, "2, 7");
		
		logBtn = uob(Math::log10, x->Math.pow(10, x), "log", "10^x"); cp.add(logBtn, "3, 1");
		cosBtn = uob(Math::cos, Math::acos, "cos", "arccos"); cp.add(cosBtn, "3, 2");
		mulBtn = bob((a,b)->a*b, null, "*", null); cp.add(mulBtn, "3, 6");
		pushBtn = b("push", false); cp.add(pushBtn, "3, 7");
		
		lnBtn = uob(Math::log, Math::exp, "ln", "e^x"); cp.add(lnBtn, "4, 1");
		tanBtn = uob(Math::tan, Math::atan, "tan", "arctan"); cp.add(tanBtn, "4, 2");
		subBtn = bob((a,b)->a-b, null, "-", null); cp.add(subBtn, "4, 6");
		popBtn = b("pop", false); cp.add(popBtn, "4, 7");
		
		powBtn = bob(Math::pow, (x,n)->Math.pow(x, 1/n), "x^n", "x^(1/n)"); cp.add(powBtn, "5, 1");
		ctgBtn = uob(x->1/Math.tan(x), x->Math.atan(1/x), "ctg", "arcctg"); cp.add(ctgBtn, "5, 2");
		swapSignBtn = b("+/-", false); cp.add(swapSignBtn, "5, 4");
		dotBtn = b(".", false); cp.add(dotBtn, "5, 5");
		addBtn = bob(Double::sum, null, "+", null); cp.add(addBtn, "5, 6");
		
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
		
		reversibles = new OperatorJButton[] {sinBtn, cosBtn, tanBtn, ctgBtn, powBtn, lnBtn, logBtn, reciprocalBtn};
		unaryOperatorButtons = new UnaryOperatorJButton[] {sinBtn, cosBtn, tanBtn, ctgBtn, lnBtn, logBtn, reciprocalBtn};
		binaryOperatorButtons = new BinaryOperatorJButton[] {addBtn, subBtn, mulBtn, divBtn, powBtn};
	}
	
	private void initListeners() {
		// inicijaliziraj listenere inv checkboxa
		for (OperatorJButton b : reversibles) {
			ItemListener il = new CalculatorInvCheckBoxItemListener(b);
			invCheckBox.addItemListener(il);
		}
		
		// inicijaliziraj listenere CalcModela.
		model.addCalcValueListener(new CalcValueListener() {
			@Override
			public void valueChanged(CalcModel model) {
				screen.setText(model.toString());
			}
		});
		model.clearAll(); //svojevrsna inicijalizacija za zuti ekran JLabel
		
		// inicijaliziraj listenere gumba brojeva
		for (int i=0; i<digitButtons.length; i++) {
			final int digit = i;
			ActionListener al = new InsertDigitActionListener(model, i);
			digitButtons[i].addActionListener(al);
		}
		
		// inicijaliziraj listenere gumba binarnih operatora
		for (BinaryOperatorJButton b : binaryOperatorButtons) {
			b.addActionListener(new BinaryOperatorActionListener(model, b));
		}
		
		// inicijaliziraj listenere gumba unarnih operatora
		for (UnaryOperatorJButton b : unaryOperatorButtons) {
			b.addActionListener(new UnaryOperatorActionListener(model, b));
		}
		
		//inicijaliziraj listenera gumba reset
		resetBtn.addActionListener(new ResetActionListener(model));
		
		//inicijaliziraj listenera gumba točke
		dotBtn.addActionListener(new InsertDecimalPointActionListener(model));
		
		//inicijaliziraj listenera gumba promjene predznaka
		swapSignBtn.addActionListener(new SwapSignActionListener(model));
		
		//inicijaliziraj listenera gumba clear
		clrBtn.addActionListener(new ClearActionListener(model));
		
		//inicijaliziraj listenera gumba =
		eqBtn.addActionListener(new EqualsActionListener(model));
		
		//inicijaliziraj listenera gumba push
		pushBtn.addActionListener(new PushPopActionListener(model, stack, this, true));
		
		//inicijaliziraj listenera gumba pop
		popBtn.addActionListener(new PushPopActionListener(model, stack, this, false));
		
	}
	
	
	private static UnaryOperatorJButton uob(DoubleUnaryOperator originalOperator, DoubleUnaryOperator reversedOperator,
			String originalOperatorText, String reversedOperatorText) {
		UnaryOperatorJButton btn = new UnaryOperatorJButton(originalOperator, reversedOperator, originalOperatorText,
				reversedOperatorText);
		decorateCalcButton(btn, false);
		return btn;
	}
	
	private static BinaryOperatorJButton bob(DoubleBinaryOperator originalOperator, DoubleBinaryOperator reversedOperator,
			String originalOperatorText, String reversedOperatorText) {
		BinaryOperatorJButton btn = new BinaryOperatorJButton(originalOperator, reversedOperator, originalOperatorText,
				reversedOperatorText);
		decorateCalcButton(btn, false);
		return btn;
	}
	
	private static JButton b(String text, boolean isDigitBtn) {
		JButton btn = new JButton(text);
		decorateCalcButton(btn, isDigitBtn);
		return btn;
	}
	
	private static void decorateCalcButton(JButton btn, boolean isDigitBtn) {
		btn.setBackground(BTN_COLOR);
		float size = isDigitBtn ? BIGGER_FONT_SIZE : SMALLER_FONT_SIZE;
		Font f = btn.getFont().deriveFont(size);
		
		btn.setFont(f);
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
