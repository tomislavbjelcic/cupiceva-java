package hr.fer.zemris.java.gui.charts;

import java.awt.Graphics;
import java.util.Objects;

import javax.swing.JComponent;

public class BarChartComponent extends JComponent {
	
	private BarChart chart;
	
	public BarChartComponent(BarChart chart) {
		this.chart = Objects.requireNonNull(chart);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		
	}
	
}
