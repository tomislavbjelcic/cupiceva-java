package hr.fer.zemris.math;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

public class ComplexTest {
	
	private static final String[] VALID_STRINGS = {"3.51", " -3.17\n", "- i2.71", "i", "1",
			" -  2.71 -  i3.15", "i351", "- 317", "3.51", "-3.17",
			"i351", "-i317", "i3.51", " -i3.17", "i", "+i", "-i",
			"-2.71-i3.15",
			"31+i24", "-1-i", "+2.71", "  +2.71  + i3.15  ", "1+i", "1-i"
	};
	private static final String[] INVALID_STRINGS = {
			"351i" , "317i" , "3.51i" , "-3.17i",
			"-+2.71" , "--2.71" , "-2.71+-i3.15",
			"+2.71-+i3.15" , "-+2.71"
	};
	
	private static final Complex C1 = new Complex(-2, 1);
	private static final Complex C2 = new Complex(5, -2);
	private static final Complex C3 = new Complex(1, 1);
	private static final Complex C4 = new Complex(-8, -8 * sqrt(3));
	private static final double EPSILON = 1e-6;
	
	@Test
	public void testOperationsThrowsNullPointerException() {
		Complex c = new Complex(1, 1);
		assertThrows(NullPointerException.class, () -> c.add(null));
		assertThrows(NullPointerException.class, () -> c.sub(null));
		assertThrows(NullPointerException.class, () -> c.multiply(null));
		assertThrows(NullPointerException.class, () -> c.divide(null));
	}
	
	@Test
	public void testAdd() {
		var expected = new Complex(3, -1);
		var actual = C1.add(C2);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testSub() {
		var expected = new Complex(-7, 3);
		var actual = C1.sub(C2);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testMultiply() {
		var expected = new Complex(-8, 9);
		var actual = C1.multiply(C2);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testDivide() {
		var expected = new Complex(-12./29, 1./29);
		var actual = C1.divide(C2);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testPowerThrowsIllegalArgumentException() {
		assertThrows(IllegalArgumentException.class, 
				() -> C1.power(-2));
	}
	
	@Test
	public void testPower() {
		var expected = new Complex(0, -8);
		var actual = C3.power(6);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testRoot() {
		double rt3 = sqrt(3);
		double one = 1;
		
		List<Complex> expected = List.of(
				new Complex(one, rt3),
				new Complex(-rt3, one),
				new Complex(-one, -rt3),
				new Complex(rt3, -one)
		);
		List<Complex> actual = C4.root(4);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testRootThrowsIllegalArgumentException() {
		assertThrows(IllegalArgumentException.class, 
				() -> C1.root(0));
	}
	
	@Test
	public void testModule() {
		double expected = 16;
		double actual = C4.module();
		double diff = abs(expected - actual);
		assertTrue(diff < EPSILON);
	}
	
	@Test
	public void testParseThrowsNullPointerException() {
		assertThrows(NullPointerException.class, 
				() -> Complex.parse(null));
	}
	
	@Test
	public void testParseDoesNotThrow() {
		assertDoesNotThrow(() -> {
			for (String s : VALID_STRINGS) {
				Complex.parse(s);
			}
		});
	}
	
	@Test
	public void testParseThrowsNumberFormatException() {
		for (String s : INVALID_STRINGS) {
			assertThrows(NumberFormatException.class, 
					() -> Complex.parse(s));
		}
	}
	
	@Test
	public void testParse() {
		String s = "2.5-i";
		
		var expected = new Complex(2.5, -1);
		var actual = Complex.parse(s);
		
		assertEquals(expected, actual);
	}
	
}
