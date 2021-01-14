package hr.fer.oprpp1.hw02.prob1;

import static hr.fer.oprpp1.hw02.prob1.TokenType.*;
import java.util.Objects;

/**
 * Jednostavan model leksičkog analizatora (lexera) sposoban za dohvaćanje leksičkih jedinki, 
 * (tokena) iz znakovnog niza u obliku Stringa.<br>
 * Pravila ovog modela leksičkog analizatora su unaprijed definirana i moguć je rad 
 * u dva različita načina rada (stanja) koja je moguće mijenjati.
 * 
 * @author Tomislav Bjelčić
 *
 */
public class Lexer {
	
	/**
	 * Escape znak korišten za zapisivanje znamenaka kao dijelova riječi.<br>
	 * Koristi se u osnovnom načinu rada.
	 */
	public static final char ESCAPE = '\\';
	/**
	 * Znak koji naznačava promjenu načina rada.
	 */
	public static final char SWITCH_STATE_SYMBOL = '#';
	/**
	 * Niz ulaznih znakova leksičkog analizatora.
	 */
	private char[] data;
	/**
	 * Zadnja pročitana leksička jedinka.<br> 
	 */
	private Token token = null;
	/**
	 * Početna pozicija potencijalne sljedeće leksičke jedinke.
	 */
	private int currentIndex = 0;
	/**
	 * Stanje, odnosno način rada ovog leksičkog analizatora.
	 */
	private LexerState state = LexerState.BASIC;
	
	/**
	 * Stvara novi leksički analizator sa ulaznim nizom znakova {@code text}.<br>
	 * Inicijalno je ovaj leksički analizator u osnovnom načinu rada.
	 * 
	 * @param text ulazni niz znakova
	 * @throws NullPointerException ako je ulazni niz znakova {@code null}.
	 */
	public Lexer(String text) {
		Objects.requireNonNull(text, "Ulazni niz predan za leksičku analizu je null");
		data = text.toCharArray();
	}
	
	/**
	 * Dohvaća sljedeću leksičku jedinku te ju svrstava u jedan od tipova leksičkih 
	 * jedinki {@link TokenType}. <br>
	 * Ako prilikom dohvaćanja sljedećeg tokena se ustanovi da pravila ovog 
	 * leksičkog analizatora nisu zadovoljena, metoda će izazvati {@code LexerException}.<br> 
	 * Ako je pročitan cijeli ulazni niz, vratit će se 
	 * token kojeg je tipa {@code EOF}. U tom će slučaju sljedeći poziv završiti iznimkom 
	 * {@code LexerException}.
	 * 
	 * @return sljedeću leksičku jedinku prikladnog tipa, ili tipa {@code EOF} 
	 * ako leksički analizator dođe do kraja ulaznog niza.
	 * @throws LexerException ako ulazni niz znakova ne zadovoljava pravila leksičkog 
	 * analizatora ili ako se više od jednom pokuša dohvatiti token poslije kraja ulaznog 
	 * niza znakova.
	 */
	public Token nextToken() {
		if (token != null && token.getType() == EOF)
			throw new LexerException("Leksički analizator došao je do kraja ulaznog niza.");
		
		
		TokenType possibleTokenType = null;	// vrsta tokena čije je čitanje u tijeku
		int lastCharTokenIndex = currentIndex;	// pozicija zadnje pročitanog znaka + 1
		boolean escape = false;	// prekidač koji označuje da je "aktivan" escape znak
		final int len = data.length;	// duljina ulaznog niza znakova
		
		switch(state) {
			case BASIC -> {
				while (currentIndex < len && lastCharTokenIndex < len) {
					char ch = getChar(lastCharTokenIndex);
					
					if (escape && ch != ESCAPE && !Character.isDigit(ch))
						throw new LexerException("Neispravna escape sekvenca \"" + ESCAPE + ch
												+ "\" na poziciji " + lastCharTokenIndex);
					
					if (Character.isWhitespace(ch)) {
						if (possibleTokenType == null) {	// nije započelo čitanje tokena
							currentIndex++;
							lastCharTokenIndex = currentIndex;
							continue;
						} else {	// nešto se dosad čitalo i sada je bjelina prekinula to
							break;	// prekini daljnje čitanje znakova
						}
					}
					
					if (Character.isDigit(ch)) {
						if (possibleTokenType != NUMBER && possibleTokenType != null && !escape) {
							// ako je u tijeku čitanje nekog tokena i zadnji znak nije bio broj
							// i ako se ne radi o escape sekvenci
							// prekini daljnje čitanje jer je token pročitan
							break;
						}
						
						if (escape) {
							possibleTokenType = WORD;
							escape = !escape;
						} else
							possibleTokenType = NUMBER;
						
						lastCharTokenIndex++;
						continue;
					}
					
					
					if (Character.isLetter(ch)) {
						if (possibleTokenType != WORD && possibleTokenType != null)
							break;
						
						possibleTokenType = WORD;
						lastCharTokenIndex++;
						continue;
					}
					
					if (ch == ESCAPE) {
						if (possibleTokenType != WORD && possibleTokenType != null)
							break;
						
						possibleTokenType = WORD;
						escape = !escape;	// toggle escape prekidača, moguće jer je dozvoljeno \\
						lastCharTokenIndex++;
						continue;
					}
					
					// nije slovo, bjelina, znamenka niti escape znak
					// ili je prekinuto dosadašnje čitanje ili je simbol
					if (possibleTokenType != null)
						break;
					possibleTokenType = SYMBOL;
					lastCharTokenIndex++;
					break;
					
				}
			}
			
			case EXTENDED -> {
				while (currentIndex < len && lastCharTokenIndex < len) {
					char ch = getChar(lastCharTokenIndex);
					
					if (Character.isWhitespace(ch)) {
						if (possibleTokenType == null) {	// nije započelo čitanje tokena
							currentIndex++;
							lastCharTokenIndex = currentIndex;
							continue;
						} else {	// nešto se dosad čitalo i sada je bjelina prekinula to
							break;	// prekini daljnje čitanje znakova
						}
					}
					
					if (ch == SWITCH_STATE_SYMBOL) {
						if (possibleTokenType == null) {
							possibleTokenType = SYMBOL;
							lastCharTokenIndex++;
						}
						break;	
					}
					
					possibleTokenType = WORD;
					lastCharTokenIndex++;
				}
			}
		}
		
		if (escape) {	// ako je escape znak aktivan, znači da je zadnji znak
			throw new LexerException("Nedovršeni escape znak "
					+ ESCAPE + "\"" + " na kraju ulaznog niza.");
		}
		
		possibleTokenType = possibleTokenType == null ? EOF : possibleTokenType;
		
		Object tokenValue = null;
		// zbog nekog internal compiler errora na eclipseu nisam mogao koristiti switch expressione
		// čudno...
		switch(possibleTokenType) {
			case EOF -> tokenValue = null;
			case SYMBOL -> {
				char sym = getChar(currentIndex);
				Character c = Character.valueOf(sym);
				tokenValue = c;
			}
			case NUMBER -> {
				int digitCount = lastCharTokenIndex - currentIndex;
				String longStr = String.valueOf(data, currentIndex, digitCount);
				long l = 0L;
				try {
					l = Long.parseLong(longStr);
				} catch (NumberFormatException ex) {
					throw new LexerException(longStr + "se ne može prikazati kao Long.");
				}
				Long num = Long.valueOf(l);
				tokenValue = num;
			}
			case WORD -> {
				StringBuilder sb = new StringBuilder();
				boolean skip = false;
				for (int i=currentIndex; i<lastCharTokenIndex; i++) {
					// izbaci escape znakove, ali ne i escapane escape znakove
					char letter = getChar(i);
					skip = letter == ESCAPE && !skip && state == LexerState.BASIC;
					if (!skip)
						sb.append(letter);
				}
				String word = sb.toString();
				
				tokenValue = word;
			}
		};
		
		currentIndex = lastCharTokenIndex;
		token = new Token(possibleTokenType, tokenValue);
		return token;
	}
	
	/**
	 * Vraća zadnju pročitanu leksičku jedinku (token).<br>
	 * Prije početka leksičke analize je {@code null}, a na kraju je tipa {@code EOF}.
	 * To znači da će poziv ove metode, odmah nakon stvaranja leksičkog analizatora, 
	 * vratiti {@code null}.
	 * @return zadnja pročitana leksička jedinka.
	 */
	public Token getToken() {
		return token;
	}
	
	/**
	 * Jednostavna pomoćna metoda za dohvaćanje znaka na poziciji {@code index} 
	 * iz niza ulaznih znakova.
	 * 
	 * @param index pozicija znaka kojeg dohvaćamo iz niza ulaznih znakova.
	 * @return ulazni znak na poziciji {@code index}.
	 */
	private char getChar(int index) {
		return data[index];
	}
	
	/**
	 * Postavlja ovaj leksički analizator na specificirani način rada (stanje).
	 * 
	 * @param state način rada leksičkog analizatora.
	 * @throws NullPointerException ako je stanje {@code null}.
	 */
	public void setState(LexerState state) {
		this.state = Objects.requireNonNull(state, "Predano novo stanje je null.");
	}
}
