package hr.fer.zemris.lsystems.impl;

import hr.fer.zemris.lsystems.Painter;

/**
 * Predstavlja općenitu akciju koja kornjača mora obaviti tijekom iscrtavanja 
 * fraktala.
 * 
 * @author Tomislav Bjelčić
 *
 */
public interface Command {
	
	/**
	 * Izvršava ovu akciju u kontekstu {@code ctx} tijekom iscrtavanja fraktala 
	 * na crtaćoj površini {@code painter}.
	 * 
	 * @param ctx kontekst iscrtavanja fraktala.
	 * @param painter crtaća površina.
	 */
	void execute(Context ctx, Painter painter);
	
}
