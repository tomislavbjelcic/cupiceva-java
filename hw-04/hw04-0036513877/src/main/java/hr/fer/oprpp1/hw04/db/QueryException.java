package hr.fer.oprpp1.hw04.db;

/**
 * Iznimka koja se baca kada format upita baze nije ispravan.
 * 
 * @author Tomislav Bjelčić
 *
 */
public class QueryException extends RuntimeException {
	
	/**
	 * Stvara novu iznimku bez poruke.
	 */
	public QueryException() {}
	
	/**
	 * Stvara novu iznimku sa porukom {@code msg}.
	 * 
	 * @param msg poruka iznimke.
	 */
	public QueryException(String msg) {
		super(msg);
	}
	
}
