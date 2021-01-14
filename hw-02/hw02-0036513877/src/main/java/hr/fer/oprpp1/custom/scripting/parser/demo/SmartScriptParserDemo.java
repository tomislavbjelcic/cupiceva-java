package hr.fer.oprpp1.custom.scripting.parser.demo;

import hr.fer.oprpp1.custom.scripting.nodes.DocumentNode;
import hr.fer.oprpp1.custom.scripting.parser.SmartScriptParser;

public class SmartScriptParserDemo {

	public static void main(String[] args) {
		
		String doc = "This is sample text.\n"
				+ "{$ FOR i 1 10 1 $}\n"
				+ "This is {$= i $}-th time this message is generated.\n"
				+ "{$END$}\n"
				+ "{$FOR i 0 10 2 $}\n"
				+ "sin({$=i$}^2) = {$= i i * @sin \"0.000\" @decfmt $}\n"
				+ "{$END$}";
		SmartScriptParser parser = new SmartScriptParser(doc);
		DocumentNode document = parser.getDocumentNode();
		System.out.println(document);
		
	}

}
