package hr.fer.oprpp1.custom.scripting.lexer;

import static hr.fer.oprpp1.custom.scripting.lexer.SmartScriptTokenType.ELEMENT_TEXT;
import static hr.fer.oprpp1.custom.scripting.lexer.SmartScriptTokenType.EOF;
import static hr.fer.oprpp1.custom.scripting.lexer.SmartScriptTokenType.TAG_ELEMENT_CONSTANT_DOUBLE;
import static hr.fer.oprpp1.custom.scripting.lexer.SmartScriptTokenType.TAG_ELEMENT_CONSTANT_INTEGER;
import static hr.fer.oprpp1.custom.scripting.lexer.SmartScriptTokenType.TAG_ELEMENT_FUNCTION;
import static hr.fer.oprpp1.custom.scripting.lexer.SmartScriptTokenType.TAG_ELEMENT_OPERATOR;
import static hr.fer.oprpp1.custom.scripting.lexer.SmartScriptTokenType.TAG_ELEMENT_STRING;
import static hr.fer.oprpp1.custom.scripting.lexer.SmartScriptTokenType.TAG_ELEMENT_VARIABLE_OR_NAME;

import java.util.Objects;

import hr.fer.oprpp1.custom.collections.ArrayIndexedCollection;
import hr.fer.oprpp1.custom.collections.Collection;


/**
 * Model leksičkog analizatora (lexera) za jezik SmartScript.<br>
 * Leksički analizator je sposoban dohvaćati leksičke jedinke (tokene) iz ulaznog programa sve dok se 
 * ne dohvati token koji označava kraj ulaznog programa.
 * 
 * @author Tomislav Bjelčić
 *
 */
public class SmartScriptLexer {
	
	/**
	 * Predstavlja stanje u kojem se nalazi ovaj leksički analizator, ovisno o tome je li leksički 
	 * analizator trenutno unutar ili izvan taga.
	 * 
	 * @author Tomislav Bjelčić
	 *
	 */
	private enum State {
		/**
		 * Stanje koje označuje da se leksički analizator "nalazi" izvan taga.
		 */
		OUTSIDE_TAG,
		/**
		 * Stanje koje označuje da se leksički analizator "nalazi" unutar taga.
		 */
		INSIDE_TAG
	}
	
	/**
	 * Znak koji omogućuje mehanizam escapeanja drugih posebnih znakova jezika SmartScript.
	 */
	public static final char ESCAPE = '\\';
	/**
	 * Znak koji označava početak otvaranja novog taga.
	 */
	public static final char TAG_OPEN = '{';
	/**
	 * Znak koji označava kraj zatvaranja novog taga.
	 */
	public static final char TAG_CLOSE = '}';
	/**
	 * Dodatni znak korišten prilikon otvaranja i zatvaranja novih tagova.
	 */
	public static final char DOLLAR = '$';
	private static final char UNDERSCORE = '_';
	private static final char DOT = '.';
	private static final char MINUS = '-';
	/**
	 * Znak koji označuje početak i završetak izraza znakovnog niza.
	 */
	public static final char STRING_SYMBOL = '\"';
	/**
	 * Znak koji označuje ime taga koji označuje čvor tipa EchoNode.
	 */
	public static final char ECHO_TAG_NAME = '=';
	/**
	 * Znak koji označuje početak izraza funkcije.
	 */
	public static final char FUNCTION_SYMBOL = '@';
	/**
	 * Skup (kolekcija) svih podržanih operatora.
	 */
	private static final Collection OPERATORS;
	static {
		char[] ops = {'+', '-', '*', '/', '^'};
		OPERATORS = new ArrayIndexedCollection(ops.length);
		for (char op : ops)
			OPERATORS.add(op);
	}
	/**
	 * Leksička jedinka (token) koji označava otvaranje novog taga.
	 */
	public static final SmartScriptToken TOKEN_TAG_OPEN;
	/**
	 * Leksička jedinka (token) koji označava zatvaranje novog taga.
	 */
	public static final SmartScriptToken TOKEN_TAG_CLOSE;
	static {
		TOKEN_TAG_OPEN = new SmartScriptToken(SmartScriptTokenType.TAG_OPEN, "{$");
		TOKEN_TAG_CLOSE = new SmartScriptToken(SmartScriptTokenType.TAG_CLOSE, "$}");
	}
	
	/**
	 * Zadnji dohvaćeni token.
	 */
	private SmartScriptToken lastToken = null;
	/**
	 * Trenutno stanje ovog leksičkog analizatora.
	 */
	private State state = State.OUTSIDE_TAG;
	/**
	 * Ulazni izvorni program jezika SmartScript nad kojim se obavlja leksička analiza.
	 */
	private String document;
	/**
	 * Polje znakova izvornog programa.
	 */
	private char[] documentData;
	/**
	 * Pozicija na kojem se trenutno nalazi leksički analizator.
	 */
	private int currentIndex = 0;
	
	
	
	/**
	 * Stvara novi leksički analizator jezika SmartScript sa ulaznim programom {@code document}.
	 * 
	 * @param document ulazni program nad kojim se obavlja leksička analiza.
	 * @throws NullPointerException ako je predani ulazni program {@code null}.
	 */
	public SmartScriptLexer(String document) {
		this.document = Objects.requireNonNull(document);
		documentData = document.toCharArray();
	}
	
	/**
	 * Dohvaća sljedeću leksičku jedinku te ju svrstava u jedan od tipova leksičkih 
	 * jedinki {@link SmartScriptTokenType}. <br>
	 * Ako prilikom dohvaćanja sljedećeg tokena se ustanovi da pravila ovog 
	 * leksičkog analizatora nisu zadovoljena, metoda će izazvati {@code SmartScriptLexerException}.<br> 
	 * Ako je pročitan cijeli ulazni niz, vratit će se 
	 * token kojeg je tipa {@code EOF}. U tom će slučaju sljedeći poziv završiti iznimkom 
	 * {@code SmartScriptLexerException}.
	 * 
	 * @return sljedeću leksičku jedinku prikladnog tipa, ili tipa {@code EOF} 
	 * ako leksički analizator dođe do kraja ulaznog niza.
	 * @throws SmartScriptLexerException ako ulazni niz znakova ne zadovoljava pravila leksičkog 
	 * analizatora ili ako se više od jednom pokuša dohvatiti token poslije kraja ulaznog 
	 * niza znakova.
	 */
	public SmartScriptToken nextToken() {
		if (lastToken != null && lastToken.getType() == EOF)
			throw new SmartScriptLexerException("Leksički analizator došao je do kraja ulaznog niza.");
		
		return (state == State.INSIDE_TAG ? nextTokenInsideTag() : nextTokenOutsideTag());
	}
	
	private char getChar(int index) {
		return documentData[index];
	}
	
	private void switchState() {
		state = (state == State.INSIDE_TAG ? State.OUTSIDE_TAG : State.INSIDE_TAG);
	}
	
	private SmartScriptToken nextTokenOutsideTag() {
		SmartScriptTokenType possibleTokenType = null;
		int lastCharTokenIndex = currentIndex;
		boolean escape = false;
		boolean stringOpen = false;
		final int len = documentData.length;
		
		while (currentIndex < len && lastCharTokenIndex < len) {
			char ch = getChar(lastCharTokenIndex);
			
			if (escape && !isEscapable(state, ch))
				throw new SmartScriptLexerException("Neispravna escape sekvenca \"" + ESCAPE + ch
						+ "\" na poziciji " + lastCharTokenIndex);
			
			if (ch == TAG_OPEN) {
				if (escape) {
					escape = !escape;
					possibleTokenType = ELEMENT_TEXT;
					lastCharTokenIndex++;
					continue;
				}
				
				int next = lastCharTokenIndex+1;
				if (next == len) {
					lastCharTokenIndex++;
					break;
				}
				char nextCh = getChar(next);
				if (nextCh != DOLLAR) {
					lastCharTokenIndex++;
					continue;
				}
				
				if (possibleTokenType != null)
					break;
				
				switchState();
				currentIndex = lastCharTokenIndex = next+1;
				lastToken = TOKEN_TAG_OPEN;
				return lastToken;
				
				
			}
			
			if (ch == ESCAPE) {
				escape = !escape;
				possibleTokenType = ELEMENT_TEXT;
				lastCharTokenIndex++;
				continue;
			}
			
			possibleTokenType = ELEMENT_TEXT;
			lastCharTokenIndex++;
		}
		
		if (escape)
			throw new SmartScriptLexerException("Nedovršeni escape znak "
					+ ESCAPE + "\"" + " na kraju dokumenta.");
		
		int from = currentIndex;
		int to = lastCharTokenIndex;
		currentIndex = lastCharTokenIndex;
		possibleTokenType = possibleTokenType == null ? EOF : possibleTokenType;
		lastToken = extractToken(from, to, possibleTokenType);
		return lastToken;
	}
	
	private SmartScriptToken nextTokenInsideTag() {
		SmartScriptTokenType possibleTokenType = null;
		int lastCharTokenIndex = currentIndex;
		boolean escape = false;
		boolean stringStarted = false;
		final int len = documentData.length;
		
		while (currentIndex < len && lastCharTokenIndex < len) {
			char ch = getChar(lastCharTokenIndex);
			
			if (escape) {
				if (!isEscapable(state, ch))
					throw new SmartScriptLexerException("Neispravna escape sekvenca \"" + ESCAPE + ch
						+ "\" na poziciji " + lastCharTokenIndex);
				
				escape = !escape;
				lastCharTokenIndex++;
				continue;
			}
			
			if (ch == STRING_SYMBOL) {
				if (!stringStarted) {
					if (possibleTokenType != null)
						break;
					possibleTokenType = TAG_ELEMENT_STRING;
					stringStarted = !stringStarted;
					lastCharTokenIndex++;
					continue;
				}
				
				if (escape) {
					escape = !escape;
					lastCharTokenIndex++;
					continue;
				}
				
				lastCharTokenIndex++;
				break;
			}
			
			if (ch == ESCAPE) {
				if (!stringStarted)
					throw new SmartScriptLexerException("Nedozvoljena escape "
							+ "sekvenca izvan Stringa na poziciji " + lastCharTokenIndex);
				
				escape = !escape;
				lastCharTokenIndex++;
				continue;
			}
			
			if (stringStarted) {
				lastCharTokenIndex++;
				continue;
			}
			
			if (ch == DOLLAR) {
				if (possibleTokenType != null)
					break;
				
				int next = lastCharTokenIndex+1;
				if (next == len)
					throw new SmartScriptLexerException("Nedovršen završetak taga na kraju dokumenta.");
				
				char nextCh = getChar(next);
				if (nextCh != TAG_CLOSE)
					throw new SmartScriptLexerException("Neispravan završetak taga "
							+ "na poziciji " + lastCharTokenIndex);
				
				if (stringStarted)
					throw new SmartScriptLexerException("Postoji nedovršen string na kraju taga");
				switchState();
				currentIndex = lastCharTokenIndex = next+1;
				lastToken = TOKEN_TAG_CLOSE;
				return lastToken;
			}
			
			if (Character.isWhitespace(ch)) {
				if (possibleTokenType == null) {
					currentIndex++;
					lastCharTokenIndex = currentIndex;
					continue;
				}
				
				if (stringStarted) {
					lastCharTokenIndex++;
					continue;
				} else
					break;
			}
			
			if (Character.isLetter(ch)) {
				if (possibleTokenType != null && !isLetterAllowed(possibleTokenType))
					break;
				
				if (possibleTokenType == null)
					possibleTokenType = TAG_ELEMENT_VARIABLE_OR_NAME;
				
				lastCharTokenIndex++;
				continue;
			}
			
			if (Character.isDigit(ch)) {
				if (possibleTokenType != null && !isDigitAllowed(possibleTokenType)) {
					
					break;
				}
				
				if (possibleTokenType == null)
					possibleTokenType = TAG_ELEMENT_CONSTANT_INTEGER;
				
				lastCharTokenIndex++;
				continue;
			}
			
			if (OPERATORS.contains(ch)) {
				if (possibleTokenType != null && !isOperatorAllowed(possibleTokenType))
					break;
				
				possibleTokenType = TAG_ELEMENT_OPERATOR;
				if (ch == MINUS) {
					int next = lastCharTokenIndex + 1;
					if (next == len) {
						lastCharTokenIndex++;
						break;
					}
					char nextCh = getChar(next);
					if (Character.isDigit(nextCh)) {
						possibleTokenType = TAG_ELEMENT_CONSTANT_INTEGER;
						lastCharTokenIndex++;
						continue;
					}
				}
				
				lastCharTokenIndex++;
				break;
			}
			
			if (ch == DOT) {
				if (possibleTokenType == null || !isDotAllowed(possibleTokenType))
					throw new SmartScriptLexerException("Nedozvoljen znak \""
							+ ch + "\" na poziciji " + lastCharTokenIndex);
				
				if (possibleTokenType == TAG_ELEMENT_CONSTANT_INTEGER)
					possibleTokenType = TAG_ELEMENT_CONSTANT_DOUBLE;
				lastCharTokenIndex++;
				continue;
			}
			
			if (ch == UNDERSCORE) {
				if (possibleTokenType != null && !isUnderscoreAllowed(possibleTokenType))
					break;
				
				if (possibleTokenType == null)
					possibleTokenType = TAG_ELEMENT_VARIABLE_OR_NAME;
				
				lastCharTokenIndex++;
				continue;
			}
			
			if (ch == ECHO_TAG_NAME) {
				if (possibleTokenType != null && isEchoTagNameAllowed(possibleTokenType))
					throw new SmartScriptLexerException("Nedozvoljen znak \""
							+ ch + "\" na poziciji " + lastCharTokenIndex);
				
				if (possibleTokenType == null)
					possibleTokenType = TAG_ELEMENT_VARIABLE_OR_NAME;
				lastCharTokenIndex++;
				break;
			}
			
			if (ch == FUNCTION_SYMBOL) {
				if (possibleTokenType != null)
					break;
				
				int next = lastCharTokenIndex + 1;
				if (next == len)
					throw new SmartScriptLexerException("Neispravan naziv funkcije, ono ne smije biti prazno.");
				char nextCh = getChar(next);
				if (!Character.isLetter(nextCh))
					throw new SmartScriptLexerException("Neispravan naziv funkcije, ono mora početi sa slovom.");
				
				lastCharTokenIndex++;
				possibleTokenType = TAG_ELEMENT_FUNCTION;
				continue;
			}
			
			throw new SmartScriptLexerException("Nedozvoljen znak na "
					+ "poziciji " + lastCharTokenIndex);
		}
		
		if (possibleTokenType == null)
			throw new SmartScriptLexerException("Postoji nezatvoreni tag u dokumentu.");
		
		int from = currentIndex;
		int to = lastCharTokenIndex;
		currentIndex = lastCharTokenIndex;
		lastToken = extractToken(from, to, possibleTokenType);
		return lastToken;
	}
	
	private SmartScriptToken extractToken(int from, int to, SmartScriptTokenType type) {
		Object tokenValue = null;
		switch(type) {
			default -> {}
			case ELEMENT_TEXT -> {
				StringBuilder sb = new StringBuilder();
				boolean skip = false;
				for (int i=from; i<to; i++) {
					char ch = getChar(i);
					skip = ch == ESCAPE && !skip;
					if (!skip)
						sb.append(ch);
				}
				tokenValue = sb.toString();
			}
			case TAG_ELEMENT_CONSTANT_DOUBLE -> {
				String str = String.valueOf(documentData, from, to-from);
				double d;
				try {
					d = Double.parseDouble(str);
				} catch (NumberFormatException ex) {
					throw new SmartScriptLexerException(str + "se ne može prikazati kao Double.");
				}
				tokenValue = Double.valueOf(d);
			}
			case TAG_ELEMENT_CONSTANT_INTEGER -> {
				String str = String.valueOf(documentData, from, to-from);
				int i;
				try {
					i = Integer.parseInt(str);
				} catch (NumberFormatException ex) {
					throw new SmartScriptLexerException(str + "se ne može prikazati kao Integer.");
				}
				tokenValue = Integer.valueOf(i);
			}
			case TAG_ELEMENT_FUNCTION -> {
				String str = String.valueOf(documentData, from+1, to-from-1);
				tokenValue = str;
			}
			case TAG_ELEMENT_OPERATOR,
				TAG_ELEMENT_VARIABLE_OR_NAME -> {
				String str = String.valueOf(documentData, from, to-from);
				tokenValue = str;
			}
			case TAG_ELEMENT_STRING -> {
				StringBuilder sb = new StringBuilder();
				for (int i=from+1; i<to-1; i++) {
					char ch = getChar(i);
					if (ch == ESCAPE) {
						i++;
						char nextCh = getChar(i);
						nextCh = switch(nextCh) {
							case 't' -> '\t';
							case 'r' -> '\r';
							case 'n' -> '\n';
							default -> nextCh;
						};
						sb.append(nextCh);
					} else
						sb.append(ch);
				}
				tokenValue = sb.toString();
			}
			case TAG_OPEN -> {
				return TOKEN_TAG_OPEN;
			}
			case TAG_CLOSE -> {
				return TOKEN_TAG_CLOSE;
			}
			
			
		}
		return new SmartScriptToken(type, tokenValue);
	}
	
	private static boolean isLetterAllowed(SmartScriptTokenType type) {
		boolean result = switch(type) {
			case ELEMENT_TEXT, 
				TAG_ELEMENT_STRING,
				TAG_ELEMENT_VARIABLE_OR_NAME, 
				TAG_ELEMENT_FUNCTION -> true;
			default -> false;
		};
		return result;
	}
	
	private static boolean isDigitAllowed(SmartScriptTokenType type) {
		boolean result = switch(type) {
			case ELEMENT_TEXT,
				TAG_ELEMENT_CONSTANT_DOUBLE,
				TAG_ELEMENT_CONSTANT_INTEGER,
				TAG_ELEMENT_FUNCTION,
				TAG_ELEMENT_VARIABLE_OR_NAME,
				TAG_ELEMENT_STRING -> true;	
			default -> false;
		};
		return result;
	}
	
	private static boolean isEscapable(State state, char ch) {
		boolean result = false;
		switch(state) {
			case OUTSIDE_TAG -> {
				result = switch(ch) {
					case TAG_OPEN, ESCAPE -> true;
					default -> false;
				};
			}
			case INSIDE_TAG -> {
				result = switch(ch) {
					case ESCAPE,
						STRING_SYMBOL,
						't', 'n', 'r' -> true;
					default -> false;
				};
			}
		}
		return result;
	}
	
	private static boolean isUnderscoreAllowed(SmartScriptTokenType type) {
		boolean result = switch(type) {
			case ELEMENT_TEXT,
				TAG_ELEMENT_FUNCTION,
				TAG_ELEMENT_VARIABLE_OR_NAME,
				TAG_ELEMENT_STRING-> true;
			default -> false;
		};
		return result;
	}
	
	private static boolean isOperatorAllowed(SmartScriptTokenType type) {
		boolean result = switch(type) {
			case ELEMENT_TEXT,
				TAG_ELEMENT_STRING-> true;
			default -> false;
		};
		return result;
	}
	
	private static boolean isDotAllowed(SmartScriptTokenType type) {
		boolean result = switch(type) {
			case ELEMENT_TEXT,
				TAG_ELEMENT_CONSTANT_INTEGER,
				TAG_ELEMENT_STRING -> true;
			default -> false;
		};
		return result;
	}
	
	private static boolean isEchoTagNameAllowed(SmartScriptTokenType type) {
		boolean result = switch(type) {
			case ELEMENT_TEXT,
				TAG_ELEMENT_STRING -> true;
			default -> false;
		};
		return result;
	}
	
	/**
	 * Vraća zadnju pročitanu leksičku jedinku (token).<br>
	 * Prije početka leksičke analize je {@code null}, a na kraju je tipa {@code EOF}.
	 * To znači da će poziv ove metode, odmah nakon stvaranja leksičkog analizatora, 
	 * vratiti {@code null}.
	 * @return zadnja pročitana leksička jedinka.
	 */
	public SmartScriptToken getToken() {
		return lastToken;
	}
	
}
