package hr.fer.oprpp1.custom.collections.demo;

import java.util.Arrays;
import java.util.Iterator;

import hr.fer.oprpp1.custom.collections.SimpleHashtable;

public class SimpleHashtableDemo {

	public static void main(String[] args) {
		// create collection:
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
		// fill data:
		examMarks.put("Ivana", 2);
		examMarks.put("Ante", 2);
		examMarks.put("Jasna", 2);
		examMarks.put("Kristina", 5);
		examMarks.put("Ivana", 5); // overwrites old grade for Ivana
		examMarks.put("Paško", 3);
		examMarks.put("Zvonko", null);
		examMarks.put("Tomislav", 5);
		examMarks.put("Patrik", null);

		// query collection:
		Integer kristinaGrade = examMarks.get("Kristina");
		System.out.println("Kristina's exam grade is: " + kristinaGrade); // writes: 5
		// What is collection's size? Must be 8!
		System.out.println("Number of stored pairs: " + examMarks.size()); // writes: 8

		System.out.println(examMarks);
		int oldAnte = examMarks.put("Ante", 3);
		System.out.println(oldAnte);

		for(SimpleHashtable.TableEntry<String,Integer> pair1 : examMarks) {
			for(SimpleHashtable.TableEntry<String,Integer> pair2 : examMarks) {
				System.out.printf(
						"(%s => %d) - (%s => %d)%n",
						pair1.getKey(), pair1.getValue(),
						pair2.getKey(), pair2.getValue()
						);
			}
		}
		
		// removes Ivana
		Iterator<SimpleHashtable.TableEntry<String,Integer>> iter = examMarks.iterator();
		while(iter.hasNext()) {
			SimpleHashtable.TableEntry<String,Integer> pair = iter.next();
			if(pair.getKey().equals("Ivana")) {
				iter.remove(); // sam iterator kontrolirano uklanja trenutni element
			}
		}
		System.out.println(examMarks);
		var arr = examMarks.toArray();
		String strArr = Arrays.toString(arr);
		System.out.println(strArr);
		
		examMarks.clear();
		System.out.printf("Veličina: %d%n", examMarks.size());
		System.out.println(examMarks);

	}

}
