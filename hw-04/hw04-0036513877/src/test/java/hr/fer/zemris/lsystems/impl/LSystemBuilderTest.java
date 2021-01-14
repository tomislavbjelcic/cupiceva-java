package hr.fer.zemris.lsystems.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import hr.fer.zemris.lsystems.LSystem;

public class LSystemBuilderTest {

	private LSystem lsystem = makeSimpleLSystem();

	private LSystem makeSimpleLSystem() {
		return new LSystemBuilderImpl()
				.setAxiom("F")
				.registerProduction('F', "F+F--F+F")
				.build();
	}

	@Test
	public void testGenerateLevelZero() {
		int level = 0;

		String expected = "F";
		String actual = lsystem.generate(level);

		assertEquals(expected, actual);
	}

	@Test
	public void testGenerateLevelOne() {
		int level = 1;

		String expected = "F+F--F+F";
		String actual = lsystem.generate(level);

		assertEquals(expected, actual);
	}
	
	@Test
	public void testGenerateLevelTwo() {
		int level = 2;

		String expected = "F+F--F+F+F+F--F+F--F+F--F+F+F+F--F+F";
		String actual = lsystem.generate(level);

		assertEquals(expected, actual);
	}

}
