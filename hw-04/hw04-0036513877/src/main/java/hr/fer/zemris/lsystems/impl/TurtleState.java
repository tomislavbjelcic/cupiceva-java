package hr.fer.zemris.lsystems.impl;

import java.awt.Color;

import hr.fer.oprpp1.math.Vector2D;

/**
 * Predstavlja trenutno stanje kornjače tijekom iscrtavanja fraktala na crtaćoj površini.
 * 
 * @author Tomislav Bjelčić
 *
 */
public class TurtleState {
	
	/**
	 * Točka (vektor) trenutne pozicije kornjače na crtaćoj površini.<br>
	 * Takva točka je dio dvodimenzionalnog koordinatnog sustava čije koordinate 
	 * se kreću u intervalima od 0 do 1. 
	 */
	private Vector2D pos;
	/**
	 * Trenutni, jedinični vektor smjera kornjače (norma takvog vektora je 1). 
	 */
	private Vector2D angle;
	/**
	 * Trenutna boja kojom kornjača iscrtava po crtaćoj površini.
	 */
	private Color color;
	/**
	 * Efektivna duljina jediničnog pomaka kornjače za akcije draw i skip.
	 */
	private double offsetLength;
	
	/**
	 * Stvara novo stanje kornjače.
	 * 
	 * @param pos početna pozicija.
	 * @param angle početni jedinični vektor smjera.
	 * @param color početna boja.
	 * @param offsetLength početna efektivna duljina jediničnog pomaka.
	 */
	public TurtleState(Vector2D pos, Vector2D angle, Color color, double offsetLength) {
		this.pos = pos;
		this.angle = angle;
		this.color = color;
		this.offsetLength = offsetLength;
	}

	/**
	 * Dohvaća trenutnu efektivnu duljinu jediničnog pomaka kornjače za akcije draw i skip..
	 * 
	 * @return trenutnu efektivnu duljinu jediničnog pomaka.
	 */
	public double getOffsetLength() {
		return offsetLength;
	}

	/**
	 * Postavlja efektivnu duljinu jediničnog pomaka kornjače 
	 * na predanu vrijednost {@code offsetLength}.
	 * 
	 * @param offsetLength nova efektivna duljina jediničnog pomaka.
	 */
	public void setOffsetLength(double offsetLength) {
		this.offsetLength = offsetLength;
	}

	/**
	 * Dohvaća trenutnu poziciju kornjače.
	 * 
	 * @return trenutnu poziciju kornjače.
	 */
	public Vector2D getPos() {
		return pos;
	}

	/**
	 * Dohvaća trenutni vektor smjera kornjače.
	 * 
	 * @return trenutni vektor smjera kornjače.
	 */
	public Vector2D getAngle() {
		return angle;
	}
	
	/**
	 * Postavlja boju kojom će kornjača iscrtavati na predanu boju {@code color}.
	 * 
	 * @param color nova boja iscrtavanja kornjače.
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * Dohvaća trenutnu boju iscrtavanja kornjače na crtaćoj površini.
	 * 
	 * @return trenutnu boju iscrtavanja kornjače.
	 */
	public Color getColor() {
		return color;
	}
	
	/**
	 * Stvara i vraća kopiju ovog stanja kornjače.
	 * 
	 * @return kopija ovog stanja kornjače.
	 */
	public TurtleState copy() {
		Vector2D posCopy = pos.copy();
		Vector2D angCopy = angle.copy();
		Color colorCopy = color; // java.awt.Color je immutable
		double offLenCopy = offsetLength;
		return new TurtleState(posCopy, angCopy, colorCopy, offLenCopy);
	}
	
}
