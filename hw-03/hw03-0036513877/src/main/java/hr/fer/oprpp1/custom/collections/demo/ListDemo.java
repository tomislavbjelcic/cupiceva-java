package hr.fer.oprpp1.custom.collections.demo;

import hr.fer.oprpp1.custom.collections.ArrayIndexedCollection;
import hr.fer.oprpp1.custom.collections.Collection;
import hr.fer.oprpp1.custom.collections.LinkedListIndexedCollection;
import hr.fer.oprpp1.custom.collections.List;
import hr.fer.oprpp1.custom.collections.Processor;

public class ListDemo {
	
	public static void main(String[] args) {
		List<String> col1 = new ArrayIndexedCollection<>();
		List<String> col2 = new LinkedListIndexedCollection<>();
		
		col1.add("Ivana");
		col2.add("Jasna");
		
		Collection<String> col3 = col1;
		Collection<String> col4 = col2;
		
		col1.get(0);
		col2.get(0);
		
		//col3.get(0);
		//col4.get(0);
		Processor<Object> printer = System.out::println;
		col1.forEach(printer);
		col2.forEach(printer);
		col3.forEach(printer);
		col4.forEach(printer);
	}
	
}
