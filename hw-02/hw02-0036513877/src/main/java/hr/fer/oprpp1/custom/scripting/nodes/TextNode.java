package hr.fer.oprpp1.custom.scripting.nodes;

import java.util.Objects;

import hr.fer.oprpp1.custom.scripting.lexer.SmartScriptLexer;

/**
 * Čvor koji u sebi sadrži tekstualni zapis izvan tagova SmartScript izvornog programa.
 * 
 * @author Tomislav Bjelčić
 *
 */
public class TextNode extends Node {
	
	/**
	 * Tekstualni zapis ovog čvora.
	 */
	private String text;
	
	/**
	 * Stvara novi čvor sa tekstualnim zapisom {@code text}.
	 * 
	 * @param text tekstualni zapis čvora.
	 */
	public TextNode(String text) {
		this.text = Objects.requireNonNull(text);
	}
	
	/**
	 * Vraća tekstualni zapis ovog čvora.
	 * 
	 * @return tekstualni zapis ovog čvora.
	 */
	public String getText() {
		return text;
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean supEquals = super.equals(obj);
		if (!supEquals)
			return false;
		
		TextNode textNodeObj = (TextNode) obj;
		return this.text.equals(textNodeObj.text);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i=0, len=text.length(); i<len; i++) {
			char ch = text.charAt(i);
			if (isEscaped(ch))
				sb.append(SmartScriptLexer.ESCAPE);
			sb.append(ch);
		}
		return sb.toString();
	}
	
	private static boolean isEscaped(char ch) {
		boolean result = switch(ch) {
			case SmartScriptLexer.ESCAPE,
				SmartScriptLexer.TAG_OPEN -> true;
			default -> false;
		};
		return result;
	}
}
