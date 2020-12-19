package hr.fer.zemris.java.gui.calc.model;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.LinkedList;
import java.util.List;
import java.util.function.DoubleBinaryOperator;

public class CalcModelImpl implements CalcModel {
	
	public static final DecimalFormat df1 = getDecimalFormat(1);
	public static final DecimalFormat df2 = getDecimalFormat(0);
	
	private boolean editable = true;
	
	private String input;
	private double inputValue;
	private boolean positive;
	private boolean leadingZero;
	private boolean dotPresent;
	private double activeOperand;
	private boolean activeOperandSet;
	private DoubleBinaryOperator pendingBinaryOperation;
	
	private String frozen;
	
	private List<CalcValueListener> listeners = new LinkedList<>();
	
	{
		clearAll();
	}
	
	private static DecimalFormat getDecimalFormat(int mandatoryDecimalCount) {
		int maxNumberOfDecimals = 9;
		char separator = '.';
		String pattern = "0" + "." + "0".repeat(mandatoryDecimalCount) + "#".repeat(maxNumberOfDecimals-mandatoryDecimalCount);
		DecimalFormat fmt = new DecimalFormat(pattern);
	    DecimalFormatSymbols sym = DecimalFormatSymbols.getInstance();
	    sym.setDecimalSeparator(separator);
	    sym.setInfinity("Infinity");
	    
	    fmt.setDecimalFormatSymbols(sym);
	    return fmt;
	}
	
	private void notifyListeners() {
		listeners.forEach(l -> l.valueChanged(this));
	}

	@Override
	public void addCalcValueListener(CalcValueListener l) {
		listeners.add(l);
	}

	@Override
	public void removeCalcValueListener(CalcValueListener l) {
		listeners.remove(l);
	}

	@Override
	public double getValue() {
		return inputValue;
	}

	@Override
	public void setValue(double value) {
		inputValue = value;
		input = df1.format(Math.abs(value));
		if (Double.isNaN(value))
			positive = true;
		else {
			double sgn = Math.signum(value);
			positive = sgn >= 0.0;
		}
		editable = false;
		notifyListeners();
	}

	@Override
	public boolean isEditable() {
		return editable;
	}

	@Override
	public void clear() {
		input = "";
		inputValue = 0.0;
		positive = true;
		leadingZero = false;
		dotPresent = false;
		editable = true;
		notifyListeners();
	}

	@Override
	public void clearAll() {
		clearActiveOperand();
		setPendingBinaryOperation(null);
		freezeValue(null);
		clear();
	}

	@Override
	public void swapSign() throws CalculatorInputException {
		if (!editable)
			throw new CalculatorInputException();
		
		positive = !positive;
		inputValue *= -1.0;
		frozen = null;
		notifyListeners();
	}

	@Override
	public void insertDecimalPoint() throws CalculatorInputException {
		if (!editable || dotPresent || !leadingZero && inputValue == 0.0)
			throw new CalculatorInputException();
		if (leadingZero && inputValue == 0.0)
			input = "0";
		dotPresent = true;
		input = input.concat(".");
		notifyListeners();
	}

	@Override
	public void insertDigit(int digit) throws CalculatorInputException, IllegalArgumentException {
		if (!editable)
			throw new CalculatorInputException();
		if (digit < 0 || digit > 9)
			throw new IllegalArgumentException();
		
		if (inputValue == 0.0 && digit == 0 && !dotPresent) {
			leadingZero = true;
			frozen = null;
			notifyListeners();
			return;
		}
		String addedDigit = input.concat(Integer.toString(digit));
		double addedValue = 0.0;
		try {
			addedValue = Double.parseDouble(addedDigit);
		} catch (NumberFormatException ex) {
			throw new CalculatorInputException(ex.getMessage());
		}
		
		if (Double.isInfinite(addedValue))
			throw new CalculatorInputException();
		input = addedDigit;
		if (!positive)
			addedValue *= -1.0;
		inputValue = addedValue;
		frozen = null;
		notifyListeners();
	}
	

	@Override
	public boolean isActiveOperandSet() {
		return activeOperandSet;
	}

	@Override
	public double getActiveOperand() throws IllegalStateException {
		if (!isActiveOperandSet())
			throw new IllegalStateException();
		return activeOperand;
	}

	@Override
	public void setActiveOperand(double activeOperand) {
		this.activeOperand = activeOperand;
		activeOperandSet = true;
		notifyListeners();
	}

	@Override
	public void clearActiveOperand() {
		activeOperandSet = false;
		notifyListeners();
	}

	@Override
	public DoubleBinaryOperator getPendingBinaryOperation() {
		return pendingBinaryOperation;
	}

	@Override
	public void setPendingBinaryOperation(DoubleBinaryOperator op) {
		pendingBinaryOperation = op;
		notifyListeners();
	}

	@Override
	public void freezeValue(String value) {
		frozen = value;
		notifyListeners();
	}

	@Override
	public boolean hasFrozenValue() {
		return frozen != null;
	}
	
	@Override
	public String toString() {
		if (frozen != null)
			return frozen;
		String prefix = positive ? "" : "-";
		String str = input.isEmpty() ? "0" : input;
		return prefix + str;
	}

}
