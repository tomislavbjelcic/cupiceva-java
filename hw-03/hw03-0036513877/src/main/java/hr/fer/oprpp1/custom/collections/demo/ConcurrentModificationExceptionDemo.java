package hr.fer.oprpp1.custom.collections.demo;

import hr.fer.oprpp1.custom.collections.ArrayIndexedCollection;
import hr.fer.oprpp1.custom.collections.Collection;
import hr.fer.oprpp1.custom.collections.ElementsGetter;

public class ConcurrentModificationExceptionDemo {
	
	public static void main(String[] args) {
		Collection<String> col = new ArrayIndexedCollection<>();
		col.add("Ivo");
		col.add("Ana");
		col.add("Jasna");
		ElementsGetter<String> getter = col.createElementsGetter();
		System.out.println("Jedan element: " + getter.getNextElement());
		System.out.println("Jedan element: " + getter.getNextElement());
		col.clear();
		System.out.println("Jedan element: " + getter.getNextElement());
	}
	
}
