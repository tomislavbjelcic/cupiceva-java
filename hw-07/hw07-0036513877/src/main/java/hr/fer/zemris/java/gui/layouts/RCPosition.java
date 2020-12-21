package hr.fer.zemris.java.gui.layouts;

import java.util.Objects;

public class RCPosition {
	
	private int row;
	private int column;
	
	public RCPosition(int row, int column) {
		this.row = row;
		this.column = column;
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}
	
	public static RCPosition parse(String text) {
		Objects.requireNonNull(text);
		
		if (text.isBlank())
			throw new IllegalArgumentException("Prazan tekst.");
		
		String regex = ",";
		String[] splitted = text.split(regex);
		if (splitted.length != 2)
			throw new IllegalArgumentException();
		
		int row = 0;
		int column = 0;
		try {
			row = Integer.parseInt(splitted[0].strip());
			column = Integer.parseInt(splitted[1].strip());
		} catch (NumberFormatException ex) {
			throw new IllegalArgumentException(ex.getMessage());
		}
		
		return new RCPosition(row, column);
	}
	

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (this == obj)
			return true;
		if (this.getClass() != obj.getClass())
			return false;
		RCPosition other = (RCPosition) obj;
		return this.row == other.row && this.column == other.column;
	}

	@Override
	public String toString() {
		return "(" + row + ", " + column + ")";
	}
	
}
