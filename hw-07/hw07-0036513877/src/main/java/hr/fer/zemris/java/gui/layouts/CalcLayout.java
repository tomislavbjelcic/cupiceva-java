package hr.fer.zemris.java.gui.layouts;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.awt.Rectangle;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.IntBinaryOperator;

public class CalcLayout implements LayoutManager2 {
	
	private static final int MIN_ROW = 1;
	private static final int MAX_ROW = 5;
	private static final int MIN_COL = 1;
	private static final int MAX_COL = 7;
	private static final RCPosition TOP_LEFT_POS = new RCPosition(1, 1);
	private static final int TOP_LEFT_POS_COLSPAN = 5;
	private static final int DEFAULT_GAP = 0;
	
	private int gap;
	private Map<Component, RCPosition> constraints = new HashMap<>();
	
	private Dimension minimum = new Dimension();
	private Dimension preferred = new Dimension();
	private Dimension maximum = new Dimension();
	
	public CalcLayout() {
		this(DEFAULT_GAP);
	}
	
	public CalcLayout(int gap) {
		if (gap < 0)
			throw new IllegalArgumentException("Negativan razmak.");
		this.gap = gap;
	}
	
	private RCPosition getConstraint(Component comp) {
		RCPosition constraint = constraints.get(comp);
		return constraint;
	}
	
	private void updateSizes(Container parent) {
		Component[] comps = parent.getComponents();
		Insets ins = parent.getInsets();
		
		// resetiraj dimenzije
		Dimension zero = new Dimension(0, 0);
		Dimension compMin = new Dimension(zero);
		Dimension compPref = new Dimension(zero);
		Dimension compMax = new Dimension(zero);
		
		IntBinaryOperator ceil = (n, divisor) -> n % divisor == 0 ? n/divisor : n/divisor + 1;
		
		for (Component c : comps) {
			RCPosition pos = getConstraint(c);
			
			// dohvati dimenzije
			Dimension min = Objects.requireNonNullElse(c.getMinimumSize(), zero);
			Dimension pref = Objects.requireNonNullElse(c.getPreferredSize(), zero);
			Dimension max = Objects.requireNonNullElse(c.getMaximumSize(), zero);
			
			if (pos != null && pos.equals(TOP_LEFT_POS)) {
				// uzmi u obzir samo dio sirine
				min.width = ceil.applyAsInt(min.width - (TOP_LEFT_POS_COLSPAN - 1) * gap, TOP_LEFT_POS_COLSPAN);
				pref.width = ceil.applyAsInt(pref.width - (TOP_LEFT_POS_COLSPAN - 1) * gap, TOP_LEFT_POS_COLSPAN);
				max.width = ceil.applyAsInt(max.width - (TOP_LEFT_POS_COLSPAN - 1) * gap, TOP_LEFT_POS_COLSPAN);
				
			}
			
			compMin.setSize(Math.max(compMin.width, min.width), Math.max(compMin.height, min.height));
			compPref.setSize(Math.max(compPref.width, pref.width), Math.max(compPref.height, pref.height));
			compMax.setSize(Math.max(compMax.width, max.width), Math.max(compMax.height, max.height));
		}
		
		int l = ins.left;
		int r = ins.right;
		int t = ins.top;
		int b = ins.bottom;
		int hgapSum = gap * (MAX_COL - 1);
		int vgapSum = gap * (MAX_ROW - 1);
		
		minimum.setSize(l+r+compMin.width*MAX_COL+hgapSum, t+b+compMin.height*MAX_ROW+vgapSum);
		preferred.setSize(l+r+compPref.width*MAX_COL+hgapSum, t+b+compPref.height*MAX_ROW+vgapSum);
		maximum.setSize(l+r+compMax.width*MAX_COL+hgapSum, t+b+compMax.height*MAX_ROW+vgapSum);
	}
	
	private RCPosition validateConstraint(Object constraint) {
		Objects.requireNonNull(constraint);
		
		RCPosition pos = null;
		if (constraint instanceof RCPosition)
			pos = (RCPosition) constraint;
		else if (constraint instanceof String)
			pos = RCPosition.parse((String) constraint);
		else
			throw new IllegalArgumentException("Ograničenje krivog tipa.");
		
		int r = pos.getRow();
		int c = pos.getColumn();
		if (r<MIN_ROW || r>MAX_ROW || c<MIN_COL || c>MAX_COL
			|| r == 1 && c > 1 && c < 6)
			throw new CalcLayoutException("Neispravno ograničenje: " + pos);
		if (constraints.containsValue(pos))
			throw new CalcLayoutException("Postoji komponenta sa ograničenjem " + pos);
		
		return pos;
	}

	@Override
	public void addLayoutComponent(String name, Component comp) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeLayoutComponent(Component comp) {
		constraints.remove(comp);
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		updateSizes(parent);
		Dimension d = new Dimension(preferred);
		return d;
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		updateSizes(parent);
		Dimension d = new Dimension(minimum);
		return d;
	}

	@Override
	public void layoutContainer(Container parent) {
		Component[] comps = parent.getComponents();
		Insets ins = parent.getInsets();
		int l = ins.left;
		int r = ins.right;
		int t = ins.top;
		int b = ins.bottom;
		double epsilon = 1e-6;
		Comparator<Double> dblCmp = new Comparator<>() {
			
			@Override
			public int compare(Double o1, Double o2) {
				return comparePrimitive(o1.doubleValue(), o2.doubleValue());
			}
			
			private int comparePrimitive(double d1, double d2) {
				double diff = Math.abs(d1 - d2);
				if (diff < epsilon)
					return 0;
				return Double.compare(d1, d2);
			}
		};
		

		Dimension parentDim = parent.getSize();
		int rows = MAX_ROW;
		int cols = MAX_COL;
		int w = parentDim.width - (l+r+(cols-1)*gap);
		int h = parentDim.height - (t+b+(rows-1)*gap);
		int wcolFloor = w / cols;
		int hrowFloor = h / rows;
		double werr = Math.abs(((double) w) / cols - wcolFloor);
		double herr = Math.abs(((double) h) / rows - hrowFloor);
		Double one = 1.0;
		double o = 1.0;
		
		int[] colWidths = new int[cols];
		int[] rowHeights = new int[rows];
		double err = 0;
		for (int i=0; i<cols; i++) {
			err += werr;
			int val = wcolFloor;
			if (dblCmp.compare(err, one) >= 0) {
				err = Math.abs(err - o);
				val++;
			}
			colWidths[i] = val;
		}
		err = 0;
		for (int i=0; i<rows; i++) {
			err += herr;
			int val = hrowFloor;
			if (dblCmp.compare(err, one) >= 0) {
				err = Math.abs(err - o);
				val++;
			}
			rowHeights[i] = val;
		}
		
		Rectangle[][] bounds = new Rectangle[rows][cols];
		int cumulativeH = t; 
		for (int i=0; i<rows; i++) {
			int cumulativeW = l;
			boolean isTopLeftRow = i==0;
			for (int j=0; j<cols; j++) {
				Rectangle rect = null;
				int x=i;
				int y=j;
				boolean skip = isTopLeftRow && j < TOP_LEFT_POS_COLSPAN;
				if (!skip) {
					rect = new Rectangle(cumulativeW, cumulativeH, colWidths[j], rowHeights[i]);
				} else {
					boolean make = j == TOP_LEFT_POS_COLSPAN-1;
					if (make) {
						rect = new Rectangle(t, l, cumulativeW + colWidths[j], cumulativeH + rowHeights[i]);
						x=y=0;
					}
				}
				bounds[x][y] = rect;
				cumulativeW += colWidths[j] + gap;
			}
			cumulativeH += rowHeights[i] + gap;
		}
		
		for (Component c : comps) {
			RCPosition pos = getConstraint(c);
			if (pos == null)
				continue;
			Rectangle rect = bounds[pos.getRow() - 1][pos.getColumn() - 1];
			c.setBounds(rect);
		}
	}

	@Override
	public void addLayoutComponent(Component comp, Object constraints) {
		RCPosition pos = validateConstraint(constraints);
		this.constraints.put(comp, pos);
	}

	@Override
	public Dimension maximumLayoutSize(Container target) {
		updateSizes(target);
		Dimension d = new Dimension(preferred);
		return d;
	}

	@Override
	public float getLayoutAlignmentX(Container target) {
		return 0.0f;
	}

	@Override
	public float getLayoutAlignmentY(Container target) {
		return 0.0f;
	}

	@Override
	public void invalidateLayout(Container target) {}
	
	
}
