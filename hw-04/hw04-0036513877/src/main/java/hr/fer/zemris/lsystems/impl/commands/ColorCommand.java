package hr.fer.zemris.lsystems.impl.commands;

import java.awt.Color;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

/**
 * Predstavlja akciju promjene boje kornjače.
 * 
 * @author Tomislav Bjelčić
 *
 */
public class ColorCommand implements Command {
	
	/**
	 * Boja iscrtavanja kornjače koja će se postaviti izvršavanjem ove akcije.
	 */
	private Color color;
	
	/**
	 * Stvara novu akciju koja će po izvršavanju promijeniti boju kornjače na 
	 * {@code color}.
	 * 
	 * @param color nova boja kornjače.
	 */
	public ColorCommand(Color color) {
		this.color = color;
	}
	
	@Override
	public void execute(Context ctx, Painter painter) {
		ctx.getCurrentState().setColor(color);
	}

}
