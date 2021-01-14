package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.lsystems.impl.TurtleState;

/**
 * Predstavlja akciju skaliranja efektivne duljine jediničnog pomaka kornjače za 
 * specificirani faktor.
 * 
 * @author Tomislav Bjelčić
 *
 */
public class ScaleCommand implements Command {
	
	/**
	 * Faktor skaliranja efektivne duljine jediničnog pomaka kornjače.
	 */
	private double factor;
	
	/**
	 * Stvara novu akciju skaliranja sa faktorom {@code factor}.
	 * 
	 * @param factor faktor skaliranja efektivne duljine jediničnog pomaka kornjače.
	 */
	public ScaleCommand(double factor) {
		this.factor = factor;
	}

	@Override
	public void execute(Context ctx, Painter painter) {
		TurtleState current = ctx.getCurrentState();
		double offLen = current.getOffsetLength() * factor;
		current.setOffsetLength(offLen);
	}
	
}
