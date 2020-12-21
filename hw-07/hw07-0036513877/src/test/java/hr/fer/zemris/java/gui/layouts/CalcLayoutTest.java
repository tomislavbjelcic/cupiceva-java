package hr.fer.zemris.java.gui.layouts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.junit.jupiter.api.Test;

public class CalcLayoutTest {
	
	@Test
	public void testPreferredSize1() {
		JPanel p = new JPanel(new CalcLayout(2));
		JLabel l1 = new JLabel(""); l1.setPreferredSize(new Dimension(10,30));
		JLabel l2 = new JLabel(""); l2.setPreferredSize(new Dimension(20,15));
		p.add(l1, new RCPosition(2,2));
		p.add(l2, new RCPosition(3,3));
		Dimension actual = p.getPreferredSize();
		Dimension expected = new Dimension(152, 158);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testPreferredSize2() {
		JPanel p = new JPanel(new CalcLayout(2));
		JLabel l1 = new JLabel(""); l1.setPreferredSize(new Dimension(108,15));
		JLabel l2 = new JLabel(""); l2.setPreferredSize(new Dimension(16,30));
		p.add(l1, "1, 1");
		p.add(l2, "3, 3");
		Dimension actual = p.getPreferredSize();
		Dimension expected = new Dimension(152, 158);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testAddLayoutComponentIllegalConstraint() {
		String[] illegalConstraints = {"0,4", "0,0", "2,8", "-4,3", "10,10",
										"1,2", "1,3", "1,4", "1,5"};
		
		for (String c : illegalConstraints) {
			assertThrows(CalcLayoutException.class, ()->{
				JPanel p = new JPanel(new CalcLayout());
				p.add(new JLabel(), c);
			});
		}
	}
	
	@Test
	public void testAddLayoutComponentSameConstraint() {
		assertThrows(CalcLayoutException.class, () -> {
			String valid = "2,2";
			JPanel p = new JPanel(new CalcLayout());
			JLabel l1 = new JLabel();
			JLabel l2 = new JLabel();
			p.add(l1, valid);
			p.add(l2, valid);
		});
	}
	
	
}
