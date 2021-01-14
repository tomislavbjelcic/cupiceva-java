package hr.fer.oprpp1.custom.collections.demo;

import hr.fer.oprpp1.custom.collections.Tester;

public class EvenIntegerTester implements Tester<Integer> {
	
	@Override
	public boolean test(Integer i) {
		return i % 2 == 0;
	}
	
	public static void main(String[] args) {
		Tester<Integer> t = new EvenIntegerTester();
		//compiler error: System.out.println(t.test("Ivo"));
		System.out.println(t.test(22));
		System.out.println(t.test(3));
	}
	
}
