package hr.fer.oprpp1.hw01;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.sqrt;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ComplexNumberTest {
	
	private static final String[] VALID_STRINGS = {"3.51", "-3.17", "-2.71i", "i", "1",
			"-2.71-3.15i", "351", "-317", "3.51", "-3.17",
			"351i", "-317i", "3.51i", "-3.17i", "i", "+i", "-i",
			"-2.71-3.15i",
			"31+24i", "-1-i", "+2.71", "+2.71+3.15i", "1+i", "1-i"
	};
	private static final String[] INVALID_STRINGS = {
			"i351" , "-i317" , "i3.51" , "-i3.17",
			"-+2.71" , "--2.71" , "-2.71+-3.15i",
			"+2.71-+3.15i" , "-+2.71"
	};
	private static final ComplexNumber C1 = new ComplexNumber(-2, 1);
	private static final ComplexNumber C2 = new ComplexNumber(5, -2);
	private static final ComplexNumber C3 = new ComplexNumber(1, 1);
	private static final ComplexNumber C4 = new ComplexNumber(-8, -8 * sqrt(3));
	private static final double EPSILON = 1e-6;
	
	@Test
	public void testFromReal() {
		var z1 = ComplexNumber.fromReal(2.25);
		var z2 = new ComplexNumber(2.25, 0.0);
		assertEquals(z2, z1);
	}
	
	@Test
	public void testFromImaginary() {
		var z1 = ComplexNumber.fromImaginary(1.25);
		var z2 = new ComplexNumber(0.0, 1.25);
		assertEquals(z2, z1);
	}
	
	@Test
	public void testFromMagnitudeAndAngleThrowsIllegalArgumentException() {
		assertThrows(IllegalArgumentException.class,
				() -> ComplexNumber.fromMagnitudeAndAngle(-3, PI));
	}
	
	@Test
	public void testFromMagnitudeAndAngle() {
		var expected = new ComplexNumber(-1, -sqrt(3));
		var actual = ComplexNumber.fromMagnitudeAndAngle(2, (4*PI)/3 - 60*PI);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testParseThrowsNullPointerException() {
		assertThrows(NullPointerException.class, 
				() -> ComplexNumber.parse(null));
	}
	
	@Test
	public void testParseDoesNotThrow() {
		assertDoesNotThrow(() -> {
			for (String s : VALID_STRINGS) {
				ComplexNumber.parse(s);
			}
		});
	}
	
	@Test
	public void testParseThrowsNumberFormatException() {
		for (String s : INVALID_STRINGS) {
			assertThrows(NumberFormatException.class, 
					() -> ComplexNumber.parse(s));
		}
	}
	
	@Test
	public void testParse() {
		String s = "2.5-i";
		
		var expected = new ComplexNumber(2.5, -1);
		var actual = ComplexNumber.parse(s);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testOperationsThrowsNullPointerException() {
		ComplexNumber c = new ComplexNumber(1, 1);
		assertThrows(NullPointerException.class, () -> c.add(null));
		assertThrows(NullPointerException.class, () -> c.sub(null));
		assertThrows(NullPointerException.class, () -> c.mul(null));
		assertThrows(NullPointerException.class, () -> c.div(null));
	}
	
	@Test
	public void testAdd() {
		var expected = new ComplexNumber(3, -1);
		var actual = C1.add(C2);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testSub() {
		var expected = new ComplexNumber(-7, 3);
		var actual = C1.sub(C2);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testMul() {
		var expected = new ComplexNumber(-8, 9);
		var actual = C1.mul(C2);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testDiv() {
		var expected = new ComplexNumber(-12./29, 1./29);
		var actual = C1.div(C2);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testPowerThrowsIllegalArgumentException() {
		assertThrows(IllegalArgumentException.class, 
				() -> C1.power(-2));
	}
	
	@Test
	public void testPower() {
		var expected = new ComplexNumber(0, -8);
		var actual = C3.power(6);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testRoot() {
		double rt3 = sqrt(3);
		double one = 1;
		
		ComplexNumber[] expected = {
			new ComplexNumber(one, rt3),
			new ComplexNumber(-rt3, one),
			new ComplexNumber(-one, -rt3),
			new ComplexNumber(rt3, -one),
		};
		ComplexNumber[] actual = C4.root(4);
		
		assertArrayEquals(expected, actual);
	}
	
	@Test
	public void testRootThrowsIllegalArgumentException() {
		assertThrows(IllegalArgumentException.class, 
				() -> C1.root(0));
	}
	
	@Test
	public void testAngle() {
		double expected = (4*PI)/3;
		double actual = C4.getAngle();
		double diff = abs(expected - actual);
		assertTrue(diff < EPSILON);
	}
	
	@Test
	public void testMagnitude() {
		double expected = 16;
		double actual = C4.getMagnitude();
		double diff = abs(expected - actual);
		assertTrue(diff < EPSILON);
	}
	
}
