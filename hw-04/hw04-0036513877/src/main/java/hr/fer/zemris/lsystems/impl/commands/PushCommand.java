package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.lsystems.impl.TurtleState;

/**
 * Predstavlja akciju stavljanja kopije trenutnog stanja kornjače na vrh stoga 
 * konteksta iscrtavanja.
 * 
 * @author Tomislav Bjelčić
 *
 */
public class PushCommand implements Command {

	@Override
	public void execute(Context ctx, Painter painter) {
		TurtleState current = ctx.getCurrentState();
		TurtleState copy = current.copy();
		ctx.pushState(copy);
	}

}
