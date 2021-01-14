package hr.fer.oprpp1.custom.scripting.lexer.demo;

import hr.fer.oprpp1.custom.scripting.lexer.SmartScriptLexer;
import hr.fer.oprpp1.custom.scripting.lexer.SmartScriptToken;
import hr.fer.oprpp1.custom.scripting.lexer.SmartScriptTokenType;

public class SmartScriptLexerDemo {
	
	public static void main(String[] args) {
		String doc = "Ovo je isto OK {$ = \"Str\\\\ing ide\\n"
				+ "u \\\"više\\\" \\nredaka\n"
				+ "ovdje a stvarno četiri\" $}";
		
		SmartScriptLexer ssl = new SmartScriptLexer(doc);
		SmartScriptToken sstoken = null;
		
		do {
			sstoken = ssl.nextToken();
			System.out.println(sstoken);
		} while (sstoken.getType() != SmartScriptTokenType.EOF);
	}
	
}
