package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

/**
 * Predstavlja akciju uklanjanja trenutnog stanja kornjače sa konteksta iscrtavanja.
 * 
 * @author Tomislav Bjelčić
 *
 */
public class PopCommand implements Command {

	@Override
	public void execute(Context ctx, Painter painter) {
		ctx.popState();
	}

}
