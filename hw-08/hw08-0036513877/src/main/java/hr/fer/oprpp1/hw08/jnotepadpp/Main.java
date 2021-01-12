package hr.fer.oprpp1.hw08.jnotepadpp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {
	
	public static void main(String[] args) {
		String pattern = "yyyy/MM/dd HH:mm:ss";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		String t = formatter.format(LocalDateTime.now());
		System.out.println(t);
	}
	
}
