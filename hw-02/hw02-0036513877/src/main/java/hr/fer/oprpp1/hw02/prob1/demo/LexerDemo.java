package hr.fer.oprpp1.hw02.prob1.demo;

import hr.fer.oprpp1.hw02.prob1.Lexer;
import hr.fer.oprpp1.hw02.prob1.Token;
import hr.fer.oprpp1.hw02.prob1.TokenType;

public class LexerDemo {

	public static void main(String[] args) {
		String sequence = "Ovo je\r\t 123ica, ab57.\nKraj";

		Lexer lexer = new Lexer(sequence);
		Token token = null;
		
		do {
			token = lexer.nextToken();
			System.out.println(token);
		} while (token.getType() != TokenType.EOF);
	}

}
