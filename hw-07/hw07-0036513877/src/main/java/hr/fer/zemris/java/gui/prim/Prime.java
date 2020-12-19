package hr.fer.zemris.java.gui.prim;

public class Prime {
	
	private Prime() {}
	
	public static boolean isPrime(long n) {
		if (n<2L)
			return false;
		if (n<4L)
			return true;
		
		long from = 2L;
		long to = Math.round(Math.sqrt((double) n));
		for (long i=from; i<=to; i++) {
			if (n % i == 0L)
				return false;
		}
		return true;
	}
	
	public static boolean isPrime(int n) {
		return isPrime((long) n);
	}
}
