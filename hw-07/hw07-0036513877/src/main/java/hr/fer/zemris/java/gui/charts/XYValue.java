package hr.fer.zemris.java.gui.charts;

import java.util.Objects;

public class XYValue {
	
	private int x;
	private int y;
	
	public XYValue(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public static XYValue parse(String s) {
		Objects.requireNonNull(s, "Predani String je null.");
		
		String comma = ",";
		String[] splitted = s.split(comma);
		if (splitted.length != 2)
			throw new IllegalArgumentException("String \""+s+"\" nije u formatu X,Y");
		
		int[] xy = new int[2];
		for (int i=0; i<2; i++) {
			String v = splitted[i];
			int val = 0;
			try {
				val = Integer.parseInt(v);
			} catch (NumberFormatException ex) {
				throw new IllegalArgumentException(ex.getMessage());
			}
			xy[i] = val;
		}
		
		int x = xy[0];
		int y = xy[1];
		return new XYValue(x, y);
	}
	
	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
	
}
