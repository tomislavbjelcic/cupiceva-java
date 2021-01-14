package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.oprpp1.math.Vector2D;
import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.lsystems.impl.TurtleState;

/**
 * Predstavlja akciju preskakanja (premještanja) trenutne pozicije kornjače 
 * u smjeru u kojem vektor smjera kornjače pokazuje za specificirani broj 
 * efektivnih duljina jediničnog pomaka.
 * 
 * @author Tomislav Bjelčić
 *
 */
public class SkipCommand implements Command {

	/**
	 * Broj efektivnih duljina jediničnog pomaka kornjače za koju duljinu će se 
	 * premjestiti trenutna pozicija kornjače.
	 */
	private double step;

	/**
	 * Stvara novu akciju premještanja kornjače za broj koraka {@code step}.
	 * 
	 * @param step broj koraka, odnosno efektivnih duljina jediničnog pomaka kornjače 
	 * za koju duljinu će se premjestiti kornjača.
	 */
	public SkipCommand(double step) {
		this.step = step;
	}

	@Override
	public void execute(Context ctx, Painter painter) {
		TurtleState current = ctx.getCurrentState();
		Vector2D currentPos = current.getPos();

		double oldX = currentPos.getX();
		double oldY = currentPos.getY();

		double drawLen = current.getOffsetLength() * step;
		Vector2D addition = current.getAngle().scaled(drawLen);

		currentPos.add(addition);
		double newX = currentPos.getX();
		double newY = currentPos.getY();

	}

}
