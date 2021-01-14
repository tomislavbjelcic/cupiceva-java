package hr.fer.oprpp1.custom.scripting.parser;

import static hr.fer.oprpp1.custom.scripting.lexer.SmartScriptTokenType.EOF;
import static hr.fer.oprpp1.custom.scripting.lexer.SmartScriptTokenType.TAG_ELEMENT_CONSTANT_DOUBLE;
import static hr.fer.oprpp1.custom.scripting.lexer.SmartScriptTokenType.TAG_ELEMENT_CONSTANT_INTEGER;
import static hr.fer.oprpp1.custom.scripting.lexer.SmartScriptTokenType.TAG_ELEMENT_STRING;
import static hr.fer.oprpp1.custom.scripting.lexer.SmartScriptTokenType.TAG_ELEMENT_VARIABLE_OR_NAME;

import java.util.Objects;

import hr.fer.oprpp1.custom.collections.ArrayIndexedCollection;
import hr.fer.oprpp1.custom.collections.Collection;
import hr.fer.oprpp1.custom.collections.ObjectStack;
import hr.fer.oprpp1.custom.scripting.elems.Element;
import hr.fer.oprpp1.custom.scripting.elems.ElementConstantDouble;
import hr.fer.oprpp1.custom.scripting.elems.ElementConstantInteger;
import hr.fer.oprpp1.custom.scripting.elems.ElementFunction;
import hr.fer.oprpp1.custom.scripting.elems.ElementOperator;
import hr.fer.oprpp1.custom.scripting.elems.ElementString;
import hr.fer.oprpp1.custom.scripting.elems.ElementVariable;
import hr.fer.oprpp1.custom.scripting.lexer.SmartScriptLexer;
import hr.fer.oprpp1.custom.scripting.lexer.SmartScriptLexerException;
import hr.fer.oprpp1.custom.scripting.lexer.SmartScriptToken;
import hr.fer.oprpp1.custom.scripting.nodes.DocumentNode;
import hr.fer.oprpp1.custom.scripting.nodes.EchoNode;
import hr.fer.oprpp1.custom.scripting.nodes.ForLoopNode;
import hr.fer.oprpp1.custom.scripting.nodes.Node;
import hr.fer.oprpp1.custom.scripting.nodes.TextNode;

/**
 * Model sintaksnog analizatora (parsera) za jezik SmartScript.<br>
 * Ovaj parser je sposoban odmah po stvaranju objekta analizirati cijeli ulazni program i stvoriti 
 * sintaksno stablo.
 * 
 * @author Tomislav Bjelčić
 *
 */
public class SmartScriptParser {
	
	/**
	 * Skup (kolekcija) svih podržanih imena tagova.
	 */
	private static final Collection ALL_TAG_NAMES;
	static {
		String[] allTagNames = {"=", "FOR", "END"};
		ALL_TAG_NAMES = new ArrayIndexedCollection(allTagNames.length);
		for (String s : allTagNames)
			ALL_TAG_NAMES.add(s);
	}
	
	
	/**
	 * Korijen sintaksnog stabla kojeg gradi ovaj parser.
	 */
	private DocumentNode document;
	
	/**
	 * Stvara novi sintaksni analizator (parser) i odmah gradi sintaksno stablo iz predanog 
	 * ulaznog programa {@code document}.
	 * 
	 * @param document ulazni program jezika SmartScript.
	 * @throws NullPointerException ako je ulazni program {@code null}.
	 * @throws SmartScriptParserException ako ulazni program ne zadovoljava pravila ovog sintaksnog analizatora.
	 */
	public SmartScriptParser(String document) {
		Objects.requireNonNull(document, "Predani dokument za parsiranje je null.");
		this.document = new DocumentNode();
		parse(document);
	}
	
	/**
	 * Metoda kojoj se delegira parsiranje ulaznog programa i izgradnja sintaksnog stabla.<br>
	 * Ako se tijekom parsiranja ustanovi da ulazni dokument ne zadovoljava pravila ovog sintaksnog analizatora, 
	 * metoda izaziva {@link SmartScriptParserException}.
	 * 
	 * @param document ulazni program jezika SmartScript.
	 */
	private void parse(String document) {
		SmartScriptLexer lexer = new SmartScriptLexer(document);
		
		ObjectStack stack = new ObjectStack();
		stack.push(this.document);
		
		while (true) {
			var token = getNextToken(lexer);
			
			if (token == null || token.getType() == EOF)
				break;
			
			var tokenType = token.getType();
			Node topNode = (Node) stack.peek();
			
			switch(tokenType) {
				default -> throw new SmartScriptParserException("Greška lexera.");
				case ELEMENT_TEXT -> {
					TextNode text = new TextNode((String) token.getValue());
					topNode.addChildNode(text);
				}
				case TAG_OPEN -> {
					var followingToken = getNextToken(lexer);
					String tagName = ((String) followingToken.getValue()).toUpperCase();
					if (tagName.equals(SmartScriptLexer.TOKEN_TAG_CLOSE.getValue()))
						throw new SmartScriptParserException("Tag bez imena i sadržaja.");
					if (!isTagName(tagName))
						throw new SmartScriptParserException("Nepodržan tag " + tagName);
					switch(tagName) {
						case "FOR" -> {
							var varToken = getNextToken(lexer);
							if (varToken.getType() != TAG_ELEMENT_VARIABLE_OR_NAME)
								throw new SmartScriptParserException("Iza FOR taga mora slijediti ime varijable.");
							ElementVariable varElem = new ElementVariable((String)varToken.getValue());
							
							int maxExprCount = 3;
							Element[] exprs = new Element[maxExprCount];
							for (int i=0; i<maxExprCount; i++) {
								var exprToken = getNextToken(lexer);
								if (exprToken == SmartScriptLexer.TOKEN_TAG_CLOSE) {
									if (i == maxExprCount - 1) {
										exprs[i] = null;
										break;
									} else
										throw new SmartScriptParserException("Premalo izraza u FOR tagu.");
								}
								var exprTokenType = exprToken.getType();
								if (exprTokenType != TAG_ELEMENT_CONSTANT_INTEGER
										&& exprTokenType != TAG_ELEMENT_CONSTANT_DOUBLE
										&& exprTokenType != TAG_ELEMENT_STRING)
									throw new SmartScriptParserException("Neočekivan token " + exprToken + " unutar FOR taga, mora ");
								boolean isExprString = false;
								Integer ie = null;
								Double de = null;
								Object exprTokenVal = exprToken.getValue();
								Element elem = null;
								if (exprTokenType == TAG_ELEMENT_STRING)
									isExprString = true;
								if (isExprString) {
									String expr = (String) exprTokenVal;
									int intExpr;
									double doubleExpr;
									try {
										ie = intExpr = Integer.parseInt(expr);
										elem = new ElementConstantInteger(intExpr);
									} catch (NumberFormatException ex) {}
									try {
										if (ie == null) {
											de = doubleExpr = Double.parseDouble(expr);
											elem = new ElementConstantDouble(doubleExpr);
										}
									} catch (NumberFormatException ex) {}
									if (ie==null && de==null)
										throw new SmartScriptParserException(expr + "se ne može prikazati ni kao Integer ni kao Double.");
								} else {
									if (exprTokenType == TAG_ELEMENT_CONSTANT_INTEGER) {
										elem = new ElementConstantInteger(((Integer) exprTokenVal).intValue());
									}
									else {
										elem = new ElementConstantDouble(((Double) exprTokenVal).doubleValue());
									}
								}
								exprs[i] = elem;
								
								
							}
							
							if (exprs[maxExprCount - 1] != null) {
								if (getNextToken(lexer) != SmartScriptLexer.TOKEN_TAG_CLOSE)
									throw new SmartScriptParserException("Nezatvoren FOR tag.");
							}
							
							ForLoopNode fln = new ForLoopNode(
									varElem,
									exprs[0],
									exprs[1],
									exprs[2]);
							topNode.addChildNode(fln);
							stack.push(fln);
							
						}
						case "=" -> {
							Collection elems = new ArrayIndexedCollection();
							while (true) {
								var elemToken = getNextToken(lexer);
								var elemTokenType = elemToken.getType();
								var elemTokenVal = elemToken.getValue();
								if (elemToken == SmartScriptLexer.TOKEN_TAG_CLOSE) {
									break;
								}
								Element elem = switch (elemTokenType) {
									case TAG_ELEMENT_CONSTANT_DOUBLE -> 
										new ElementConstantDouble((Double) elemTokenVal);
									case TAG_ELEMENT_CONSTANT_INTEGER ->
										new ElementConstantInteger((Integer) elemTokenVal);
									case TAG_ELEMENT_FUNCTION ->
										new ElementFunction((String) elemTokenVal);
									case TAG_ELEMENT_OPERATOR ->
										new ElementOperator((String) elemTokenVal);
									case TAG_ELEMENT_VARIABLE_OR_NAME ->
										new ElementVariable((String) elemTokenVal);
									case TAG_ELEMENT_STRING ->
										new ElementString((String) elemTokenVal);
									default -> throw new SmartScriptParserException("Greška lexera.");
								};
								elems.add(elem);
							}
							Object[] objElems = elems.toArray();
							Element[] elements = new Element[objElems.length];
							for (int i=0, len=objElems.length; i<len; i++) {
								elements[i] = (Element) objElems[i];
							}
							EchoNode en = new EchoNode(elements);
							topNode.addChildNode(en);
						}
						case "END" -> {
							var shouldBeClosed = getNextToken(lexer);
							if (shouldBeClosed != SmartScriptLexer.TOKEN_TAG_CLOSE)
								throw new SmartScriptParserException("Nezatvoren END tag.");
							
							stack.pop();
							if (stack.isEmpty())
								throw new SmartScriptParserException("Dokument sadrži previše END tagova.");
						}
					}
				}
			}
			
			
		}
	}
	
	
	
	private SmartScriptToken getNextToken(SmartScriptLexer lexer) {
		SmartScriptToken token = null;
		try {
			token = lexer.nextToken();
		} catch (SmartScriptLexerException ex) {
			String msg = ex.getMessage();
			throw new SmartScriptParserException(msg);
		}
		return token;
	}
	
	private static boolean isTagName(String name) {
		return ALL_TAG_NAMES.contains(name);
	}
	
	/**
	 * Vraća korijen izgrađenog sintaksnog stabla.
	 * 
	 * @return korijen sintaksnog stabla.
	 */
	public DocumentNode getDocumentNode() {
		return document;
	}
	
}
