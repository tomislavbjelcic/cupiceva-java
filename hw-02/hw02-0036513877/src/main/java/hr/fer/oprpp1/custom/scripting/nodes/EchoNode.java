package hr.fer.oprpp1.custom.scripting.nodes;

import java.util.Arrays;
import java.util.Objects;

import hr.fer.oprpp1.custom.scripting.elems.Element;
import hr.fer.oprpp1.custom.scripting.lexer.SmartScriptLexer;

/**
 * Čvor koji u sebi sadrži izraze.
 * 
 * @author Tomislav Bjelčić
 *
 */
public class EchoNode extends Node {
	
	/**
	 * Skup (polje) izraza ovog čvora.
	 */
	private Element[] elements;
	
	/**
	 * Stvara novi čvor sa izrazima {@code elements}.
	 * 
	 * @param elements izrazi koji će se pohraniti u ovom čvoru.
	 * @throws NullPointerException ako je bilo koji od predanih izraza {@code null}.
	 */
	public EchoNode(Element... elements) {
		for (Element e : elements)
			Objects.requireNonNull(e);
		this.elements = elements;
	}
	
	/**
	 * Dohvaća kopiju skupa izraza ovog čvora.
	 * 
	 * @return kopija skupa izraza ovog čvora.
	 */
	public Element[] getElements() {
		return Arrays.copyOf(elements, elements.length);
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean supEquals = super.equals(obj);
		if (!supEquals)
			return false;
		
		EchoNode echoNodeObj = (EchoNode) obj;
		int elemsThis = this.elements.length;
		int elemsObj = echoNodeObj.elements.length;
		if (elemsThis != elemsObj)
			return false;
		
		for (int i=0; i<elemsThis; i++) {
			if (!this.elements[i].equals(echoNodeObj.elements[i]))
				return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(SmartScriptLexer.TAG_OPEN)
			.append(SmartScriptLexer.DOLLAR)
			.append(SmartScriptLexer.ECHO_TAG_NAME);
		
		for (Element e : elements)
			sb.append(e).append(' ');
		
		sb.append(SmartScriptLexer.DOLLAR)
		.append(SmartScriptLexer.TAG_CLOSE);
		return sb.toString();
	}
	
}
