package hr.fer.oprpp1.hw08.jnotepadpp;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

public class Main {
	
	public static void main(String[] args) {
		Comparator<Object> cmp = Collator.getInstance(new Locale("hr"));
		int r = cmp.compare("đešnjak", "kinja");
		System.out.println(r);
	}
	
}
