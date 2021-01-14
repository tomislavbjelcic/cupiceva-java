package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.lsystems.impl.TurtleState;

/**
 * Predstavlja akciju rotiranja kornjače za specificirani broj stupnjeva.
 * 
 * @author Tomislav Bjelčić
 *
 */
public class RotateCommand implements Command {
	
	/**
	 * Kut rotacije kornjače u radijanima.
	 */
	private double angleRad;
	
	/**
	 * Stvara novu akciju rotiranja kornjače za {@code angleDeg} stupnjeva.
	 * 
	 * @param angleDeg kut rotacije kornjače u stupnjevima.
	 */
	public RotateCommand(double angleDeg) {
		angleRad = Math.toRadians(angleDeg);
	}
	
	@Override
	public void execute(Context ctx, Painter painter) {
		TurtleState current = ctx.getCurrentState();
		current.getAngle().rotate(angleRad);
	}
	
	

}
