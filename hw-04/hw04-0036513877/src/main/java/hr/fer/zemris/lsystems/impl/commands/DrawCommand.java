package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.oprpp1.math.Vector2D;
import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.lsystems.impl.TurtleState;

/**
 * Predstavlja akciju iscrtavanja linije kornjače za specificirani broj efektivnih 
 * duljina jediničnog pomaka.
 * 
 * @author Tomislav Bjelčić
 *
 */
public class DrawCommand implements Command {
	
	/**
	 * Broj efektivnih duljina jediničnog pomaka koliko će biti dugačka iscrtana 
	 * linija tijekom izvršavanja ove akcije.
	 */
	private double step;
	
	/**
	 * Stvara novu akciju iscrtavanja linije kornjače za duljinu {@code step} 
	 * efektivnih duljina jediničnog pomaka.
	 * 
	 * @param step broj efektivnih duljina jediničnog pomaka.
	 */
	public DrawCommand(double step) {
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
		
		painter.drawLine(oldX, oldY, newX, newY, current.getColor(), 1.0f);
		
	}
	
}
