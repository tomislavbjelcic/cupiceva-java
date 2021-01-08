package hr.fer.oprpp1.hw08.jnotepadpp;

class A {
	void m() {
		System.out.println("ma");
	}
}

class B extends A {
	@Override
	void m() {
		super.m();
		System.out.println("mb");
	}
	
	void lel() {
		super.m();
	}
}

public class Main {
	
	public static void main(String[] args) {
		B b = new B();
		b.lel();
	}
	
}
