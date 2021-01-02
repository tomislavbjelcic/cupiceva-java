package hr.fer.oprpp1.hw08.jnotepadpp;

import java.util.ArrayList;
import java.util.List;

public class Main {
	
	public static void main(String[] args) {
		List<Object> l = new ArrayList<>();
		l.add(null);
		l.add(3);
		l.add(null);
		l.add("lmao");
		l.add(new ArrayList<Object>(l));
		System.out.println(l);
	}
	
}
